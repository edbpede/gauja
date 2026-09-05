#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Runs every hook-backed script test under tools/tests/ (tests mirror tools/ folder-for-folder).
set -euo pipefail

usage() { echo "Usage: tools/tests/run.sh [--help]"; echo "Runs tools/tests/**/*.sh; exits non-zero if any test fails."; }
[[ "${1:-}" == "--help" || "${1:-}" == "-h" ]] && { usage; exit 0; }

here="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
failed=0
while IFS= read -r test; do
  echo "== ${test#"$here"/}"
  if ! bash "$test"; then failed=$((failed + 1)); fi
done < <(find "$here" -mindepth 2 -type f -name '*.sh' | sort)

if [[ $failed -ne 0 ]]; then
  echo "tools/tests: $failed test file(s) failed." >&2
  exit 1
fi
echo "tools/tests: all passed."
