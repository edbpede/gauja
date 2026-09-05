#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Check spec/pin pairing in the index or each CI commit, plus contract validity."""
import argparse
import json
from pathlib import Path
import subprocess
import sys
import tempfile
sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "contract"))
from endpoints import render
from validate import load_contract


def git(*args):
    return subprocess.check_output(["git", *args])


def paired(names):
    paths = set(names.decode().split("\0"))
    if ("api/seerr-api.yml" in paths) != ("api/UPSTREAM_COMMIT" in paths):
        raise ValueError("api/seerr-api.yml and api/UPSTREAM_COMMIT must change together")


def check(api):
    spec = load_contract(api)
    expected = render(spec, json.loads((api / "coverage.json").read_text()))
    if (api / "ENDPOINTS.md").read_text() != expected:
        raise ValueError("ENDPOINTS.md differs; regenerate the endpoint index")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--range", nargs=2, metavar=("BASE", "HEAD"))
    parser.add_argument("--working-tree", action="store_true", help="Validate unstaged inputs instead of the index")
    args = parser.parse_args()
    try:
        root = Path(git("rev-parse", "--show-toplevel").decode().strip())
        if args.range:
            base, head = args.range
            for revision in (base, head):
                git("rev-parse", "--verify", revision + "^{commit}")
            for commit in git("rev-list", "--reverse", f"{base}..{head}").decode().splitlines():
                paired(git("diff-tree", "--root", "--no-commit-id", "--name-only", "-r", "-z", commit, "--", "api/"))
            check(root / "api")
        elif args.working_tree:
            check(root / "api")
        else:
            paired(git("diff", "--cached", "--name-only", "-z", "--", "api/"))
            with tempfile.TemporaryDirectory(prefix="gauja-index-") as directory:
                snapshot = Path(directory)
                for name in filter(None, git("ls-files", "-z", "--", "api/").decode().split("\0")):
                    target = snapshot / name
                    target.parent.mkdir(parents=True, exist_ok=True)
                    target.write_bytes(git("show", ":" + name))
                check(snapshot / "api")
        print("api-drift: pairing, provenance, effective contract and index valid")
    except (ValueError, OSError, KeyError, subprocess.CalledProcessError) as error:
        parser.exit(1, f"api-drift: {error}\n")


if __name__ == "__main__":
    main()
