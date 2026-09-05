#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Validate shared API inputs; optionally emit an uncommitted effective spec."""
import argparse
import json
from pathlib import Path

from validate import load_contract


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--api", type=Path, default=Path(__file__).resolve().parents[2] / "api")
    parser.add_argument("--output", type=Path)
    args = parser.parse_args()
    try:
        spec = load_contract(args.api)
        if args.output:
            args.output.write_text(json.dumps(spec, indent=2, ensure_ascii=False) + "\n")
        print("contract: references, overlays, operation IDs and compatibility valid")
    except (ValueError, KeyError, TypeError, OSError) as error:
        parser.exit(1, f"contract: {error}\n")


if __name__ == "__main__":
    main()
