#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/../assert.sh"
script="$REPO_ROOT/tools/api-drift/check-local.sh"

assert_exit 0 "empty api/ passes" "$script" "$TEST_TMP/api"

mkdir -p "$TEST_TMP/api"
echo "openapi: 3.0.2" > "$TEST_TMP/api/seerr-api.yml"
assert_exit 1 "spec without UPSTREAM_COMMIT fails" "$script" "$TEST_TMP/api"

# Staged-together rule inside a scratch repository.
repo="$TEST_TMP/repo"
git init -q "$repo"
mkdir -p "$repo/api"
echo "openapi: 3.0.2" > "$repo/api/seerr-api.yml"
echo "0123456789abcdef0123456789abcdef01234567" > "$repo/api/UPSTREAM_COMMIT"
git -C "$repo" add api/seerr-api.yml
assert_exit 1 "spec staged alone fails" bash -c "cd '$repo' && '$script' api"
git -C "$repo" add api/UPSTREAM_COMMIT
assert_exit 0 "spec and pin staged together pass" bash -c "cd '$repo' && '$script' api"
