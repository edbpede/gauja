#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/../assert.sh"
script="$REPO_ROOT/tools/tokens/check.sh"

assert_exit 0 "missing tokens.json passes" "$script" "$TEST_TMP/tokens.json"
echo '{}' > "$TEST_TMP/tokens.json"
assert_exit 0 "tokens.json without a generator passes (Phase 2 adds it)" "$script" "$TEST_TMP/tokens.json"
