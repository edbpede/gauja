#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Check inventory consistency, existing specifications and auth acceptance links."""
import argparse
from pathlib import Path
import re


def anchor_name(heading):
    return re.sub(r"[^\w\- ]", "", heading.lower()).replace(" ", "-")


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
            anchors = {anchor_name(heading) for heading in headings}
            if anchor not in anchors:
                raise ValueError(f"{path.name}: broken anchor {link}")


def contract_text(path, anchor):
    text = path.read_text()
    if not anchor:
        return text
    headings = list(re.finditer(r"^(#+) (.+)$", text, re.M))
    for index, heading in enumerate(headings):
        if anchor_name(heading[2]) == anchor:
            end = next((item.start() for item in headings[index + 1:] if len(item[1]) <= len(heading[1])), len(text))
            return text[heading.end():end]
    raise ValueError(f"{path.name}: missing contract anchor {anchor}")


def check(root):
    screens = root / "design/screens"
    inventory = (screens / "INVENTORY.md").read_text()
    rows = re.findall(r"^\| `([^`]+\.md(?:#[^`]+)?)` \| ([^|]+) \| ([SML]) \| ([^|]+) \| ([^|]+) \|$", inventory, re.M)
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
    component_names = re.findall(r"^\| \[([^\]]+)\]\(([^)]+\.md(?:#[^)]+)?)\) \|", components, re.M)
    names, targets = zip(*component_names) if component_names else ((), ())
    if not names or len(set(names)) != len(names) or len(set(targets)) != len(targets):
        raise ValueError("Missing or duplicate component identities")
    contracts = [(screens / "components" / target.partition("#")[0], target.partition("#")[2]) for target in targets]
    screen_contracts = [(screens / row[0].partition("#")[0], row[0].partition("#")[2]) for row in rows]
    for path, anchor in screen_contracts:
        if path.is_file() or path.parent.name in {"auth", "servers"}:
            contracts.append((path, anchor))
    paths = {path for path, _ in contracts}
    detailed = set(screens.rglob("*.md")) - {screens / "TEMPLATE.md"}
    for path in detailed:
        check_links(path)
        if path.name not in {"INVENTORY.md", "MATRIX.md"} and path not in paths:
            raise ValueError(f"{path.name}: spec missing from inventory")
    for path, anchor in contracts:
        text = contract_text(path, anchor)
        # Behavior is required; a fixed set of sections is not. Review decides
        # which states, permissions and interactions apply to each contract.
        acceptance = re.search(r"^(?:#{2,6} Acceptance criteria\n|\*\*Acceptance criteria:\*\*)", text, re.M)
        content = text[acceptance.end():].strip() if acceptance else ""
        if not content or content.startswith(("#", "**")):
            raise ValueError(f"{path.name}: missing Acceptance criteria")
        references = set(re.findall(r"\b[AS]\d{2}\b", text))
        if not references <= cases:
            raise ValueError(f"{path.name}: unknown matrix row")
        if path.parent.name != "components":
            referenced |= references
    if referenced != cases:
        raise ValueError("An auth matrix row is not owned by a screen spec")
    return len(rows), len(contracts), len(cases)


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
