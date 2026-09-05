#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Reject generated API references outside the API/Data boundary and test support."""
import argparse
from pathlib import Path
import re


def violations(root):
    for platform, extension, expression, allowed in [
        ("android", "*.kt", r"\bapp\.gauja\.core\.api\b", ("core/api/", "core/data/", "core/testing/")),
        ("ios", "*.swift", r"\bimport\s+SeerrAPI\b", ("Packages/SeerrAPI/", "Packages/Data/", "Packages/Testing/")),
    ]:
        tree = root / "apps" / platform
        for path in tree.rglob(extension):
            relative = path.relative_to(tree).as_posix()
            if any(part in {"build", ".build", ".gradle", "DerivedData"} for part in path.parts):
                continue
            source = re.sub(r"/\*.*?\*/|//[^\n]*", "", path.read_text(), flags=re.S)
            if re.search(r"(?:@_exported\s+|public\s+)import\s+SeerrAPI", source):
                yield path, "Data must not re-export SeerrAPI"
            elif re.search(expression, source) and not relative.startswith(allowed):
                yield path, "generated API reference outside API/Data/test support"


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[2])
    args = parser.parse_args()
    errors = list(violations(args.root))
    for path, reason in errors:
        print(f"api-boundary: {path}: {reason}")
    if errors:
        parser.exit(1)
    print("api-boundary: valid")


if __name__ == "__main__":
    main()
