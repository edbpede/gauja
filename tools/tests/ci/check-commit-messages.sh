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

git -C "$repo" commit -q --allow-empty -m "feat(auth): foreign sign-off" -m "Signed-off-by: Someone Else <else@example.com>"
assert_exit 1 "sign-off email matching neither author nor committer fails" bash -c "cd '$repo' && '$script' $base HEAD"
git -C "$repo" reset -q --hard HEAD~1

git -C "$repo" -c user.email=committer@example.com commit -q --allow-empty -m "feat(auth): committer sign-off" --author="Author <author@example.com>" -m "Signed-off-by: Committer <committer@example.com>"
assert_exit 0 "sign-off matching the committer passes" bash -c "cd '$repo' && '$script' $base HEAD"
git -C "$repo" reset -q --hard HEAD~1

# The range itself is checked, not only HEAD: a bad commit under a good one still fails.
git -C "$repo" commit -q --allow-empty -m "added stuff" -s
git -C "$repo" commit -q --allow-empty -m "fix(auth): good on top" -s
assert_exit 1 "non-conventional intermediate commit fails" bash -c "cd '$repo' && '$script' $base HEAD"
git -C "$repo" reset -q --hard HEAD~2

assert_exit 2 "unresolvable range ref fails" bash -c "cd '$repo' && '$script' no-such-ref HEAD"

assert_exit 0 "conventional PR title passes" "$script" --title "feat: phase 1 tooling"
assert_exit 1 "non-conventional PR title fails" "$script" --title "Phase 1 tooling"
assert_exit 0 "breaking-change marker is accepted" "$script" --title "refactor(core)!: drop legacy blacklist"
