#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Check inventory consistency, existing specifications and auth acceptance links."""
import argparse
from pathlib import Path
import re

SECTIONS = ["Contract", "Content", "Actions", "Endpoints", "Permissions", "Acceptance criteria"]


def check_links(path):
    for link in re.findall(r"\[[^\]]+\]\(([^)]+)\)", path.read_text()):
        if "://" in link or link.startswith("mailto:"):
            continue
        target, _, anchor = link.partition("#")
        destination = path.parent / target if target else path
        if not destination.is_file():
            raise ValueError(f"{path.name}: broken link {link}")
        if anchor and destination.suffix == ".md":
            headings = re.findall(r"^#+ (.+)$", destination.read_text(), re.M)
            anchors = {re.sub(r"[^\w\- ]", "", heading.lower()).replace(" ", "-") for heading in headings}
            if anchor not in anchors:
                raise ValueError(f"{path.name}: broken anchor {link}")


def check(root):
    screens = root / "design/screens"
    inventory = (screens / "INVENTORY.md").read_text()
    rows = re.findall(r"^\| `([^`]+\.md)` \| ([^|]+) \| ([SML]) \| ([^|]+) \| ([^|]+) \|$", inventory, re.M)
    if not rows or len({r[0] for r in rows}) != len(rows):
        raise ValueError("Missing or duplicate screen identities")
    areas = {row[0].split("/")[0] for row in rows}
    if not {"auth", "servers", "discover", "search", "media", "requests", "issues", "watchlist", "profile", "users", "settings", "about"} <= areas:
        raise ValueError("Inventory omits a required area")
    totals = [sum(row[2] == size for row in rows) for size in "SML"] + [len(rows)]
    expected = "| **Total** | " + " | ".join(f"**{n}**" for n in totals) + " |"
    if expected not in inventory:
        raise ValueError("Inventory sizing totals differ from rows")
    components = (screens / "components/INVENTORY.md").read_text()
    matrix = (screens / "auth/MATRIX.md").read_text()
    case_ids = re.findall(r"^\| ([AS]\d+) \|", matrix, re.M)
    if len(case_ids) != len(set(case_ids)):
        raise ValueError("Duplicate auth matrix IDs")
    cases = set(case_ids)
    referenced = set()
    component_names = re.findall(r"^\| \[([^\]]+)\]\(([^)]+\.md)\) \|", components, re.M)
    names, targets = zip(*component_names) if component_names else ((), ())
    if not names or len(set(names)) != len(names) or len(set(targets)) != len(targets):
        raise ValueError("Missing or duplicate component identities")
    paths = [screens / "components" / target for target in targets]
    screen_paths = [screens / row[0] for row in rows]
    for path in screen_paths:
        if path.is_file() or path.parent.name in {"auth", "servers"}:
            paths.append(path)
    detailed = set(screens.rglob("*.md")) - {screens / "TEMPLATE.md"}
    for path in detailed:
        check_links(path)
        if path.name not in {"INVENTORY.md", "MATRIX.md"} and path not in paths:
            raise ValueError(f"{path.name}: spec missing from inventory")
    for path in paths:
        text = path.read_text()
        sections = SECTIONS + ([] if path.parent.name == "components" else ["States", "Adaptive behavior", "Accessibility", "Content components"])
        for section in sections:
            if f"## {section}\n" not in text:
                raise ValueError(f"{path.name}: missing {section}")
        references = set(re.findall(r"\b[AS]\d{2}\b", text))
        if not references <= cases:
            raise ValueError(f"{path.name}: unknown matrix row")
        if path.parent.name != "components":
            referenced |= references
    if referenced != cases:
        raise ValueError("An auth matrix row is not owned by a screen spec")
    return len(rows), len(paths), len(cases)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[2])
    args = parser.parse_args()
    try:
        rows, specs, cases = check(args.root)
        print(f"screen-specs: {rows} sized screens, {specs} detailed contracts, {cases} auth cases")
    except (ValueError, OSError) as error:
        parser.exit(1, f"screen-specs: {error}\n")


if __name__ == "__main__":
    main()
