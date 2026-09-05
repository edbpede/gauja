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
from jsonschema.exceptions import SchemaError, ValidationError
from validate import load_contract, validate_coverage


def git(*args, cwd=None):
    return subprocess.check_output(["git", *args], cwd=cwd)


def paired(names, api):
    paths = set(names.decode().split("\0"))
    if ((Path(api) / "seerr-api.yml").as_posix() in paths) != ((Path(api) / "UPSTREAM_COMMIT").as_posix() in paths):
        raise ValueError(f"{api}/seerr-api.yml and {api}/UPSTREAM_COMMIT must change together")


def check(api):
    spec = load_contract(api)
    validate_coverage(spec, json.loads((api / "coverage.json").read_text()))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("api", nargs="?", type=Path, metavar="API_DIR")
    parser.add_argument("--range", nargs=2, metavar=("BASE", "HEAD"))
    parser.add_argument("--working-tree", action="store_true", help="Validate unstaged inputs instead of the index")
    args = parser.parse_args()
    try:
        root = Path(git("rev-parse", "--show-toplevel").decode().strip())
        api = args.api.resolve() if args.api else root / "api"
        if args.range:
            api = api.relative_to(root).as_posix()
            base, head = args.range
            for revision in (base, head):
                git("rev-parse", "--verify", revision + "^{commit}", cwd=root)
            for commit in git("rev-list", "--reverse", f"{base}..{head}", cwd=root).decode().splitlines():
                paired(git("diff-tree", "--root", "--no-commit-id", "--name-only", "-r", "-z", commit, "--", api + "/", cwd=root), api)
            check(root / api)
        elif args.working_tree:
            check(api)
        else:
            api = api.relative_to(root).as_posix()
            paired(git("diff", "--cached", "--name-only", "-z", "--", api + "/", cwd=root), api)
            with tempfile.TemporaryDirectory(prefix="gauja-index-") as directory:
                snapshot = Path(directory)
                for name in filter(None, git("ls-files", "-z", "--", api + "/", cwd=root).decode().split("\0")):
                    target = snapshot / name
                    target.parent.mkdir(parents=True, exist_ok=True)
                    target.write_bytes(git("show", ":" + name, cwd=root))
                check(snapshot / api)
        print("api-drift: pairing, provenance, effective contract and coverage valid")
    except (SchemaError, ValidationError, ValueError, OSError, KeyError, TypeError, subprocess.CalledProcessError) as error:
        parser.exit(1, f"api-drift: {error}\n")


if __name__ == "__main__":
    main()
