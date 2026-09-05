#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Read-only online verification of the vendored specification and MIT license."""
import argparse
from pathlib import Path
import re
import urllib.error
import urllib.request


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--api", type=Path, default=Path(__file__).resolve().parents[2] / "api")
    parser.add_argument("--license", type=Path, help="Seerr MIT notice (default: API_DIR/../LICENSES/MIT.txt)")
    args = parser.parse_args()
    try:
        lines = (args.api / "UPSTREAM_COMMIT").read_text().splitlines()
        if not lines or not re.fullmatch(r"[0-9a-f]{40}", lines[0]):
            parser.exit(1, "upstream: invalid SHA\n")
        base = f"https://raw.githubusercontent.com/seerr-team/seerr/{lines[0]}/"
        notice = args.license or args.api.parent / "LICENSES/MIT.txt"
        for remote, local in [("seerr-api.yml", args.api / "seerr-api.yml"), ("LICENSE", notice)]:
            with urllib.request.urlopen(base + remote, timeout=30) as response:
                if response.read() != local.read_bytes():
                    parser.exit(1, f"upstream: {local} differs from pinned upstream bytes\n")
    except (OSError, urllib.error.URLError) as error:
        parser.exit(1, f"upstream: {error}\n")
    print("upstream: specification and license are verbatim")


if __name__ == "__main__":
    main()
