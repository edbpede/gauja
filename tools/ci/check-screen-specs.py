#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Check Phase 2 inventory sizing, component contracts and auth acceptance links."""
import argparse
from pathlib import Path
import re

COMPONENTS = "TitleCard MediaSlider RequestCard RequestBlock RequestButton IssueBlock StatusBadge AirDateBadge PersonCard CompanyCard GenreCard GenreTag KeywordTag DownloadBlock ExternalLinkBlock BlocklistedTagsBadge PermissionEdit PermissionOption QuotaSelector NotificationTypeSelector JSONEditor".split()
SECTIONS = ["Contract", "Content", "States", "Actions", "Adaptive behavior", "Accessibility", "Endpoints", "Permissions", "Acceptance criteria"]


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
    if totals != [14, 50, 31, 95]:
        raise ValueError("Inventory sizing differs from Phase 2 contract")
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
    paths = [screens / "components" / (name + ".md") for name in COMPONENTS]
    paths += [screens / row[0] for row in rows if row[0].startswith(("auth/", "servers/"))]
    for name in COMPONENTS:
        if f"[{name}]({name}.md)" not in components:
            raise ValueError(f"Missing component inventory link: {name}")
    for path in paths:
        text = path.read_text()
        sections = SECTIONS + ([] if path.parent.name == "components" else ["Content components"])
        for section in sections:
            if f"## {section}\n" not in text:
                raise ValueError(f"{path.name}: missing {section}")
        for state in ("Loading", "Empty", "Error", "Offline", "Permission-denied"):
            if f"**{state}:**" not in text:
                raise ValueError(f"{path.name}: missing state {state}")
        references = set(re.findall(r"\b[AS]\d{2}\b", text))
        if not references <= cases:
            raise ValueError(f"{path.name}: unknown matrix row")
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
