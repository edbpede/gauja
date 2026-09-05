#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/../assert.sh"
script="$REPO_ROOT/tools/ci/check-licenses.sh"

assert_exit 1 "missing deny.toml fails" "$script" --deny "$TEST_TMP/missing.toml"

cat > "$TEST_TMP/empty.toml" <<'TOML'
[licenses]
allow = []
TOML
assert_exit 1 "empty allow list fails" "$script" --deny "$TEST_TMP/empty.toml"

cat > "$TEST_TMP/deny.toml" <<'TOML'
[licenses]
allow = [
  "Apache-2.0",
  "MIT",
]
TOML
assert_exit 0 "valid allow list with no manifests passes" "$script" --deny "$TEST_TMP/deny.toml"
assert_exit 0 "repository deny.toml is valid" "$script" --deny "$REPO_ROOT/deny.toml"
