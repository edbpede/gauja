#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
if [[ "${1:-}" == --help ]]; then
  echo 'Usage: tools/contract/python.sh [--install | PYTHON_ARGUMENTS...]'
  echo 'Install hash-pinned contract dependencies, or run the isolated interpreter.'
  exit 0
fi
venv="$root/.cache/contract"
if [[ "${1:-}" == --install ]]; then
  python3 -m venv "$venv"
  "$venv/bin/python" -m pip install --disable-pip-version-check --require-hashes -r "$root/tools/contract/requirements.txt"
  exit 0
fi
if [[ ! -x "$venv/bin/python" ]]; then
  echo 'Install contract tooling first: tools/contract/python.sh --install' >&2
  exit 1
fi
export PYTHONDONTWRITEBYTECODE=1
exec "$venv/bin/python" "$@"
