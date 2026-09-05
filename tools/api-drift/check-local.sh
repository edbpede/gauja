#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
for arg in "$@"; do
  if [[ "$arg" == --help || "$arg" == -h ]]; then
    echo 'Usage: tools/api-drift/check-local.sh [--help] [API_DIR] [--range BASE HEAD | --working-tree]'
    echo 'Validate the staged contract by default (API_DIR defaults to api).'
    exit 0
  fi
done
exec "$root/tools/contract/python.sh" "$root/tools/api-drift/check.py" "$@"
