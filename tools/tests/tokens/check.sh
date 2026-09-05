#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../.." && pwd)"
if [[ "${1:-}" == --help ]]; then echo "Run tokens fixture tests"; exit 0; fi
exec "$root/tools/contract/python.sh" -m unittest discover -s "$root/tools/tests/tokens" -p "test_*.py" -v
