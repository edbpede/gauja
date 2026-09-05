#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# tokens-check (PRD §8, §14.1): both platform themes are generated from design/tokens.json;
# a hand-edited theme fails. Phase 1 scope: exits 0 until Phase 2 adds the generators.
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: tools/tokens/check.sh [--help] [TOKENS_FILE]

Regenerates the Compose and SwiftUI themes from TOKENS_FILE (default: design/tokens.json)
and fails on any diff. Exits 0 while the tokens file or the generators do not exist.
USAGE
}

tokens="design/tokens.json"
for arg in "$@"; do
  case "$arg" in
    --help|-h) usage; exit 0 ;;
    *) tokens="$arg" ;;
  esac
done

if [[ ! -f "$tokens" ]]; then
  echo "tokens-check: $tokens does not exist yet; nothing to regenerate."
  exit 0
fi
generator="$(dirname "${BASH_SOURCE[0]}")/generate.sh"
if [[ ! -x "$generator" ]]; then
  echo "tokens-check: $tokens present but no generator yet (Phase 2); skipping."
  exit 0
fi
exec "$generator" --check "$tokens"
