#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/../assert.sh"
script="$REPO_ROOT/tools/community/validate-translations.py"

assert_exit 0 "no catalogs passes" python3 "$script" --root "$TEST_TMP"

android="$TEST_TMP/apps/android/app/src/main/res"
ios="$TEST_TMP/apps/ios/App/Resources"
mkdir -p "$android/values" "$android/values-da" "$ios"
cat > "$android/values/strings.xml" <<'XML'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="discover_title">Discover</string>
    <string name="requests_title">Requests</string>
</resources>
XML
cat > "$android/values-da/strings.xml" <<'XML'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="discover_title">Opdag</string>
</resources>
XML
cat > "$ios/Localizable.xcstrings" <<'JSON'
{"sourceLanguage":"en","version":"1.0","strings":{"discover_title":{},"requests_title":{}}}
JSON
assert_exit 0 "valid catalogs with parity pass" python3 "$script" --root "$TEST_TMP"

cat > "$android/values-da/strings.xml" <<'XML'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="discover_title">Opdag</string>
    <string name="orphan">Nope</string>
</resources>
XML
assert_exit 1 "locale key missing from default catalog fails" python3 "$script" --root "$TEST_TMP"
printf '<resources><string name="discover_title">Opdag</string></resources>\n' > "$android/values-da/strings.xml"

# Only the duplicate is wrong here: values-da still references discover_title, which stays defined.
printf '<resources><string name="discover_title">1</string><string name="discover_title">2</string></resources>\n' > "$android/values/strings.xml"
assert_exit 1 "duplicate key fails" python3 "$script" --root "$TEST_TMP" --no-parity
printf '<resources><string name="discover_title">Discover</string><string name="requests_title">Requests</string></resources>\n' > "$android/values/strings.xml"

printf '<resources><string name="x">' > "$android/values-da/strings.xml"
assert_exit 1 "malformed XML fails" python3 "$script" --root "$TEST_TMP"
printf '<resources><string name="discover_title">Opdag</string></resources>\n' > "$android/values-da/strings.xml"

printf '{"strings":{"discover_title":{}}}\n' > "$ios/Localizable.xcstrings"
assert_exit 1 "key parity mismatch fails" python3 "$script" --root "$TEST_TMP"
assert_exit 0 "--no-parity skips the parity check" python3 "$script" --root "$TEST_TMP" --no-parity

printf '{not json' > "$ios/Localizable.xcstrings"
assert_exit 1 "invalid xcstrings JSON fails" python3 "$script" --root "$TEST_TMP"

printf '[]\n' > "$ios/Localizable.xcstrings"
assert_exit 1 "xcstrings whose top level is not an object fails" python3 "$script" --root "$TEST_TMP"
