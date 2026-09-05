#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/../assert.sh"
script="$REPO_ROOT/tools/ci/check-fixture-secrets.sh"

assert_exit 0 "missing fixture dir passes" "$script" "$TEST_TMP/none"

good="$TEST_TMP/good"; mkdir -p "$good/2.7.0"
cat > "$good/2.7.0/status.json" <<'JSON'
{"version":"2.7.0","apiKey":"REDACTED","headers":{"X-Api-Key":"<redacted>","Cookie":"connect.sid=***"}}
JSON
assert_exit 0 "scrubbed fixture passes" "$script" "$good"

# Samples are assembled at runtime so that no literal credential lives in the repository
# (gitleaks and detect-private-key would otherwise reject this test file itself).
hex32="$(printf '0123456789abcdef%.0s' 1 2)"
b64="$(printf 'MTc1NzA5MjM1M%.0s' 1 2 3)"
pem="-----BEGIN EC PRIVATE"
pem+=" KEY-----"
bad_samples=(
  "X-Api-Key: $b64"
  "\"apiKey\": \"$b64\""
  "Cookie: connect.sid=s%3AabcDEF123456789012345.signatureSignature"
  "X-Plex-Token: aBcDeFgHiJkLmNoPqRsT"
  "\"accessToken\": \"$hex32\""
  "Authorization: MediaBrowser Client=\"Gauja\", Token=\"$hex32\""
  "\"vapidPrivateKey\": \"$(printf 'BNcRdreALRFX%.0s' 1 2 3 4)\""
  "$pem"
  "Authorization: Basic dXNlcjpwYXNzd29yZA=="
)
for sample in "${bad_samples[@]}"; do
  bad="$TEST_TMP/bad"; rm -rf "$bad"; mkdir -p "$bad/2.7.0"
  printf '%s\n' "$sample" > "$bad/2.7.0/recorded.http"
  assert_exit 1 "rejects '${sample%%[:=]*}'" "$script" "$bad"
done

# The offending value itself must never be printed.
bad="$TEST_TMP/bad"; rm -rf "$bad"; mkdir -p "$bad/2.7.0"
printf 'X-Plex-Token: aBcDeFgHiJkLmNoPqRsT\n' > "$bad/2.7.0/recorded.http"
if "$script" "$bad" 2>&1 | grep -q 'aBcDeFgHiJkLmNoPqRsT'; then
  echo "FAIL: secret value was echoed" >&2; exit 1
fi
echo "ok: matched value is not echoed"
