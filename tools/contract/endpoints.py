#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Render the endpoint checklist from contract and editable coverage metadata."""
import argparse
from collections import defaultdict
import json
from pathlib import Path
from operations import operations
from validate import load_contract


def render(spec, coverage):
    groups = defaultdict(list)
    ids = {op["operationId"] for _, _, op in operations(spec)}
    if set(coverage) != ids:
        raise ValueError("coverage.json must cover exactly the effective operation IDs")
    for path, method, op in operations(spec):
        entry = coverage[op["operationId"]]
        if entry.get("status") not in {"planned", "implemented", "excluded"} or not entry.get("phase") or not entry.get("note"):
            raise ValueError(f"Invalid coverage metadata: {op['operationId']}")
        groups[op["tags"][0]].append((path, method, op, entry))
    lines = ["<!--", "SPDX-FileCopyrightText: 2026 Gauja contributors",
             "SPDX-License-" + "Identifier: AGPL-3.0-or-later", "-->", "", "# Endpoint inventory", "",
             "GENERATED — do not edit. Update `api/coverage.json`, then run",
             "`tools/contract/python.sh tools/contract/endpoints.py`.", "",
             f"Pinned Seerr contract: **{len(spec['paths'])} paths / {len(ids)} operations**, relative to `/api/v1`.", "",
             "Counts use the first operation tag; paths shared between tags are counted in each group.",
             "Excluded operations remain generated but are never invoked by Gauja.", "",
             "| Tag | Paths | Operations |", "|---|---:|---:|"]
    for tag, rows in sorted(groups.items()):
        lines.append(f"| {tag} | {len({r[0] for r in rows})} | {len(rows)} |")
    for tag, rows in sorted(groups.items()):
        lines += ["", f"## {tag}", ""]
        for path, method, op, entry in rows:
            mark = "x" if entry["status"] == "implemented" else " "
            lines.append(f"- [{mark}] `{method.upper()} {path}` — `{op['operationId']}`; phase {entry['phase']}; {entry['status']}. {entry['note']}")
    lines += ["", "## Outside this pin", "",
              "Develop adds `/settings/{plex|jellyfin}/library/{libraryId}` and",
              "`/settings/{plex|jellyfin}/library/sync` (four paths). These need a later contract sync.",
              "The full v1 inventory retains library management using this release's operations.", "",
              "Plex PIN endpoints and the configured image proxy/CDN are outside this OpenAPI document.",
              "Their purposes and allowed peers are specified in auth and component specs.", ""]
    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    parser.add_argument("--api", type=Path, default=Path(__file__).resolve().parents[2] / "api")
    args = parser.parse_args()
    output = render(load_contract(args.api), json.loads((args.api / "coverage.json").read_text()))
    path = args.api / "ENDPOINTS.md"
    if args.check:
        if not path.exists() or path.read_text() != output:
            parser.exit(1, "endpoints: index differs; regenerate it\n")
    else:
        path.write_text(output)


if __name__ == "__main__":
    main()
