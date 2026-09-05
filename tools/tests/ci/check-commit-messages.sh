#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/../assert.sh"
script="$REPO_ROOT/tools/ci/check-commit-messages.sh"

repo="$TEST_TMP/repo"
git init -q "$repo"
git -C "$repo" config user.name "Test"
git -C "$repo" config user.email "test@example.com"
git -C "$repo" commit -q --allow-empty -m "chore: root" -s
base="$(git -C "$repo" rev-parse HEAD)"

git -C "$repo" commit -q --allow-empty -m "feat(auth): add sign-in" -s
assert_exit 0 "conventional signed-off commit passes" bash -c "cd '$repo' && '$script' $base HEAD"

git -C "$repo" commit -q --allow-empty -m "feat(auth): missing sign-off"
assert_exit 1 "missing Signed-off-by fails" bash -c "cd '$repo' && '$script' $base HEAD"
git -C "$repo" reset -q --hard HEAD~1

git -C "$repo" commit -q --allow-empty -m "added stuff" -s
assert_exit 1 "non-conventional subject fails" bash -c "cd '$repo' && '$script' $base HEAD"
git -C "$repo" reset -q --hard HEAD~1

assert_exit 0 "conventional PR title passes" "$script" --title "feat: phase 1 tooling"
assert_exit 1 "non-conventional PR title fails" "$script" --title "Phase 1 tooling"
assert_exit 0 "breaking-change marker is accepted" "$script" --title "refactor(core)!: drop legacy blacklist"
