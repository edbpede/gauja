#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# api-drift (local): the vendored spec and its pinned upstream commit change together.
# Phase 1 scope: exits 0 while api/ is empty. When both files exist, fails if one is
# staged without the other. The upstream diff itself lands in Phase 2 (tools/api-drift/).
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: tools/api-drift/check-local.sh [--help] [API_DIR]

Fails when api/seerr-api.yml is staged without api/UPSTREAM_COMMIT or vice versa.
Exits 0 when the contract has not been vendored yet (PRD §4.1).
  API_DIR   directory holding seerr-api.yml and UPSTREAM_COMMIT (default: api)
USAGE
}

api_dir="api"
for arg in "$@"; do
  case "$arg" in
    --help|-h) usage; exit 0 ;;
    *) api_dir="$arg" ;;
  esac
done

spec="$api_dir/seerr-api.yml"
pin="$api_dir/UPSTREAM_COMMIT"

if [[ ! -f "$spec" && ! -f "$pin" ]]; then
  echo "api-drift: no vendored contract under $api_dir/ yet; nothing to check."
  exit 0
fi
if [[ -f "$spec" && ! -f "$pin" ]] || [[ ! -f "$spec" && -f "$pin" ]]; then
  echo "api-drift: $spec and $pin must exist together (PRD §4.1)." >&2
  exit 1
fi

# Staged-together rule. Outside a git repo (or with nothing staged) there is nothing to compare.
if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  staged="$(git diff --cached --name-only -- "$spec" "$pin" || true)"
  spec_staged=0; pin_staged=0
  grep -qx "$spec" <<<"$staged" && spec_staged=1
  grep -qx "$pin" <<<"$staged" && pin_staged=1
  if [[ $spec_staged -ne $pin_staged ]]; then
    echo "api-drift: $spec and $pin change together; stage both (PRD §4.1)." >&2
    exit 1
  fi
fi

echo "api-drift: contract consistent."
