#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Read-only online verification of the vendored specification and MIT license."""
import argparse
from pathlib import Path
import re
import urllib.request


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--api", type=Path, default=Path(__file__).resolve().parents[2] / "api")
    args = parser.parse_args()
    sha = (args.api / "UPSTREAM_COMMIT").read_text().splitlines()[0]
    if not re.fullmatch(r"[0-9a-f]{40}", sha):
        parser.exit(1, "upstream: invalid SHA\n")
    base = f"https://raw.githubusercontent.com/seerr-team/seerr/{sha}/"
    for remote, local in [("seerr-api.yml", "seerr-api.yml"), ("LICENSE", "LICENSE.upstream")]:
        with urllib.request.urlopen(base + remote, timeout=30) as response:
            if response.read() != (args.api / local).read_bytes():
                parser.exit(1, f"upstream: {local} differs from pinned upstream bytes\n")
    print("upstream: specification and license are verbatim")


if __name__ == "__main__":
    main()
