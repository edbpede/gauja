#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Secret-logging guard (PRD §10, §14.1 `check-secret-logging`): no log call may format a
# value from the secrets layer. The symbol list below is confirmed in Phase 4 when
# core/datastore / Persistence name the real types; the scan itself is complete.
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: tools/ci/check-secret-logging.sh [--help] [--symbols A,B,C] [PATH...]

Scans Kotlin and Swift sources under PATH... (default: apps) for log calls
(Log.*, Timber.*, Logger.*, logger.*, print(, println(, debugPrint(, NSLog(, os_log()
whose line mentions a secret-layer symbol. Generated directories are skipped.
  --symbols  comma-separated identifiers
             (default: SecretStore,apiKey,sessionCookie,plexToken,basicAuthPassword)
Exits 0 when there are no sources yet; 1 on any offending line.
USAGE
}

symbols="SecretStore,apiKey,sessionCookie,plexToken,basicAuthPassword"
paths=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    --help|-h) usage; exit 0 ;;
    --symbols) symbols="$2"; shift 2 ;;
    --symbols=*) symbols="${1#--symbols=}"; shift ;;
    *) paths+=("$1"); shift ;;
  esac
done
[[ ${#paths[@]} -eq 0 ]] && paths=("apps")

existing=()
for p in "${paths[@]}"; do [[ -e "$p" ]] && existing+=("$p"); done
if [[ ${#existing[@]} -eq 0 ]]; then
  echo "check-secret-logging: no sources under ${paths[*]} yet; nothing to scan."
  exit 0
fi

# GENERATED paths from prek.toml; generated code never logs and is never hand-edited.
generated_regex='(apps/android/core/api/|apps/ios/Packages/SeerrAPI/Generated/|apps/android/core/designsystem/src/main/kotlin/.*/generated/|apps/ios/Packages/DesignSystem/Sources/DesignSystem/Generated/)'
log_call='(Log\.[a-z]+\(|Timber(\.tag\([^)]*\))?\.[a-z]+\(|[Ll]ogger(\(\))?\.[a-z]+\(|print\(|println\(|debugPrint\(|NSLog\(|os_log\()'
symbol_alt="$(printf '%s' "$symbols" | tr ',' '|')"
symbol_regex="(^|[^A-Za-z0-9_])(${symbol_alt})([^A-Za-z0-9_]|$)"

status=0
while IFS= read -r file; do
  hits="$(grep -En -e "$log_call" "$file" | grep -E -e "$symbol_regex" | cut -d: -f1 || true)"
  if [[ -n "$hits" ]]; then
    status=1
    while IFS= read -r line; do
      echo "check-secret-logging: $file:$line: log call formats a secret-layer symbol" >&2
    done <<<"$hits"
  fi
done < <(find "${existing[@]}" -type f \( -name '*.kt' -o -name '*.swift' \) | grep -Ev "$generated_regex" || true)

if [[ $status -ne 0 ]]; then
  echo "check-secret-logging: secrets never touch logs; redact through core/common / Common (PRD §10)." >&2
  exit 1
fi
echo "check-secret-logging: no secret-layer symbols inside log calls."
