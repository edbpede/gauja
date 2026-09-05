#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Minimal assertions shared by tools/tests/**. Source this file; do not execute it.

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
TEST_TMP="$(mktemp -d)"
trap 'rm -rf "$TEST_TMP"' EXIT

# assert_exit EXPECTED DESCRIPTION CMD...
assert_exit() {
  local expected="$1" description="$2"; shift 2
  local actual=0
  "$@" >/dev/null 2>&1 || actual=$?
  if [[ "$actual" -ne "$expected" ]]; then
    echo "FAIL: $description (expected exit $expected, got $actual)" >&2
    return 1
  fi
  echo "ok: $description"
}
