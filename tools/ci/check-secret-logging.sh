#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Secret-logging guard (PRD §10, §14.1 `check-secret-logging`): no log call may format a
# value from the secrets layer. The symbol list below is confirmed in Phase 4 when
# core/datastore / Persistence name the real types; the scan itself is complete.
#
# A log call is scanned from the line it starts on until its parentheses balance (capped at
# MAX_CALL_LINES), so a call split across lines cannot hide a secret on a continuation line.
# Full-line comments (`//`, `/*`, `*`) outside a call are skipped so documentation may show
# what not to do; a trailing comment on a code line is still scanned.
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: tools/ci/check-secret-logging.sh [--help] [--symbols A,B,C] [PATH...]

Scans Kotlin and Swift sources under PATH... (default: apps) for log calls
(Log.*, Timber.*, Logger.*, logger.*, print(, println(, debugPrint(, NSLog(, os_log()
that mention a secret-layer symbol, including calls split across lines.
Generated directories are skipped.
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
# `[Ll]ogger(...)` covers both `Logger()` and `Logger(subsystem:, category:)` initializers.
log_call='(Log\.[a-z]+\(|Timber(\.tag\([^)]*\))?\.[a-z]+\(|[Ll]ogger(\([^)]*\))?\.[a-z]+\(|print\(|println\(|debugPrint\(|NSLog\(|os_log\()'
symbol_alt="$(printf '%s' "$symbols" | tr ',' '|')"
symbol_regex="(^|[^A-Za-z0-9_])(${symbol_alt})([^A-Za-z0-9_]|$)"

status=0
# Regexes reach awk through the environment: `-v` would reinterpret their backslashes.
scan_file() {
  LOG_CALL="$log_call" SYMBOL_REGEX="$symbol_regex" MAX_CALL_LINES=10 awk '
    BEGIN { log_call = ENVIRON["LOG_CALL"]; symbol_regex = ENVIRON["SYMBOL_REGEX"]; max_lines = ENVIRON["MAX_CALL_LINES"] + 0 }
    function parens(s,  opened, closed) {
      opened = gsub(/\(/, "(", s); closed = gsub(/\)/, ")", s)
      return opened - closed
    }
    function finish() {
      if (buffer ~ symbol_regex) print start
      depth = 0; buffer = ""; lines = 0
    }
    depth > 0 {
      buffer = buffer "\n" $0; lines++
      depth += parens($0)
      if (depth <= 0 || lines >= max_lines) finish()
      next
    }
    $0 ~ /^[[:space:]]*(\/\/|\/\*|\*)/ { next }
    match($0, log_call) {
      start = NR; buffer = substr($0, RSTART); lines = 1
      depth = parens(buffer)
      if (depth <= 0) finish()
    }
    END { if (depth > 0) finish() }
  ' "$1"
}

while IFS= read -r file; do
  hits="$(scan_file "$file")"
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
