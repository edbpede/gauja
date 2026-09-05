#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Discover shell tests and Python suites centrally, including directories without __init__.py.
set -euo pipefail

usage() { echo "Usage: tools/tests/run.sh [--help]"; echo "Discovers shell tests and test_*.py suites; exits non-zero if any test fails."; }
[[ "${1:-}" == "--help" || "${1:-}" == "-h" ]] && { usage; exit 0; }

here="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
failed=0
while IFS= read -r test; do
  echo "== ${test#"$here"/}"
  if ! bash "$test"; then failed=$((failed + 1)); fi
done < <(find "$here" -mindepth 2 -type f -name '*.sh' | sort)

while IFS= read -r suite; do
  echo "== ${suite#"$here"/} (Python)"
  if ! "$here/../contract/python.sh" -m unittest discover -s "$suite" -p 'test_*.py' -v; then
    failed=$((failed + 1))
  fi
done < <(find "$here" -type f -name 'test_*.py' -exec dirname {} \; | sort -u)

if [[ $failed -ne 0 ]]; then
  echo "tools/tests: $failed test file(s) failed." >&2
  exit 1
fi
echo "tools/tests: all passed."
