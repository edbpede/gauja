#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Secret-logging guard (PRD §10, §14.1 `check-secret-logging`): no log call may format a
# value from the secrets layer. The symbol list below is confirmed in Phase 4 when
# core/datastore / Persistence name the real types; the scan itself is complete.
#
# A log call is scanned from the line it starts on until its parentheses balance, following
# chained `.method(` continuations, so a call split across lines cannot hide a secret on a later
# line. Parentheses inside string literals and comments are not counted. Comments outside a call
# (`//`, `/* ... */`) are skipped so documentation may show what not to do.
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
# `[Ll]ogger(\.[a-z]+)?\(` starts at a `logger.debug(` call or at a `Logger(...)` initializer whose
# chained `.debug(` is followed by the scanner, whatever the initializer's arguments look like.
log_call='(Log\.[a-z]+\(|Timber(\.tag\([^)]*\))?\.[a-z]+\(|[Ll]ogger(\.[a-z]+)?\(|print\(|println\(|debugPrint\(|NSLog\(|os_log\()'
symbol_alt="$(printf '%s' "$symbols" | tr ',' '|')"
symbol_regex="(^|[^A-Za-z0-9_])(${symbol_alt})([^A-Za-z0-9_]|$)"

status=0
# Regexes reach awk through the environment: `-v` would reinterpret their backslashes.
scan_file() {
  LOG_CALL="$log_call" SYMBOL_REGEX="$symbol_regex" awk '
    BEGIN { log_call = ENVIRON["LOG_CALL"]; symbol_regex = ENVIRON["SYMBOL_REGEX"] }
    # Index of the parenthesis that closes the call opened in text, following chained
    # `.method(` continuations; 0 while the call is still open or may still be chained on the
    # next line. Strings ("...", """...""") and comments are skipped so their parentheses do
    # not count.
    function call_end(text,   i, n, c, depth, str, raw, rest) {
      n = length(text)
      for (i = 1; i <= n; i++) {
        c = substr(text, i, 1)
        if (raw) { if (substr(text, i, 3) == "\"\"\"") { raw = 0; i += 2 }; continue }
        if (str) { if (c == "\\") i++; else if (c == "\"") str = 0; continue }
        if (substr(text, i, 3) == "\"\"\"") { raw = 1; i += 2; continue }
        if (c == "\"") { str = 1; continue }
        if (substr(text, i, 2) == "//") { rest = index(substr(text, i), "\n"); if (rest == 0) return 0; i += rest - 1; continue }
        if (substr(text, i, 2) == "/*") { rest = index(substr(text, i + 2), "*/"); if (rest == 0) return 0; i += rest + 2; continue }
        if (c == "(") depth++
        else if (c == ")") {
          depth--
          if (depth <= 0) {
            rest = substr(text, i + 1)
            if (rest ~ /^[[:space:]]*\.[A-Za-z_][A-Za-z0-9_]*[[:space:]]*\(/) continue
            if (rest ~ /^[[:space:]]*$/) return 0   # a chained `.method(` may follow on the next line
            return i
          }
        }
      }
      return 0
    }
    # text begins at a log-call match. Reports the line once, keeps the call open across lines,
    # and scans any further call on the same line.
    function scan(text,   e) {
      while (text != "") {
        e = call_end(text)
        if (e == 0) { open = text; return }
        if (substr(text, 1, e) ~ symbol_regex) { print start; break }
        text = substr(text, e + 1)
        if (match(text, log_call)) text = substr(text, RSTART); else break
      }
      open = ""
    }
    open != "" { open = open "\n" $0; scan(open); next }
    in_comment { if (index($0, "*/")) in_comment = 0; next }
    /^[[:space:]]*(\/\/|\*)/ { next }
    /^[[:space:]]*\/\*/ { if (!index($0, "*/")) in_comment = 1; next }
    match($0, log_call) { start = NR; scan(substr($0, RSTART)) }
    END { if (open != "" && open ~ symbol_regex) print start }
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
