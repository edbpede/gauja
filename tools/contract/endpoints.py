#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Render the endpoint checklist from contract and editable coverage metadata."""
import argparse
from collections import defaultdict
import json
from pathlib import Path
from operations import operations
from validate import load_contract, validate_coverage


def render(spec, coverage):
    validate_coverage(spec, coverage)
    groups = defaultdict(list)
    ids = {op["operationId"] for _, _, op in operations(spec)}
    for path, method, op in operations(spec):
        entry = coverage[op["operationId"]]
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
            summary = op.get("summary", "").rstrip(".")
            description = ". ".join(part for part in [summary, entry.get("note", "").rstrip(".")] if part)
            details = f" {description}." if description else ""
            lines.append(f"- [{mark}] `{method.upper()} {path}` — `{op['operationId']}`; phase {entry['phase']}; {entry['status']}.{details}")
    lines += ["", "## Outside the OpenAPI document", "",
              "Plex PIN endpoints and the configured image proxy/CDN are outside this OpenAPI document.",
              "Their purposes and allowed peers are specified in auth and component specs.", ""]
    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, help="Write a local report instead of stdout")
    parser.add_argument("--api", type=Path, default=Path(__file__).resolve().parents[2] / "api")
    args = parser.parse_args()
    output = render(load_contract(args.api), json.loads((args.api / "coverage.json").read_text()))
    if args.output:
        args.output.write_text(output)
    else:
        print(output, end="")


if __name__ == "__main__":
    main()
