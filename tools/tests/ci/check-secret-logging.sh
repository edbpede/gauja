#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/../assert.sh"
script="$REPO_ROOT/tools/ci/check-secret-logging.sh"

assert_exit 0 "no sources passes" "$script" "$TEST_TMP/apps"

src="$TEST_TMP/apps/android/core/network/src/main/kotlin"
mkdir -p "$src"
cat > "$src/AuthInterceptor.kt" <<'KT'
class AuthInterceptor(private val secretStore: SecretStore) {
    fun log() {
        Log.d(TAG, "profile switched: ${profile.id}")
        Timber.tag(TAG).i("request to %s", redact(url))
    }
}
KT
assert_exit 0 "log calls without secrets pass" "$script" "$TEST_TMP/apps"

cat > "$src/Leak.kt" <<'KT'
fun leak() { Log.d(TAG, "key=$apiKey") }
KT
assert_exit 1 "Kotlin Log.d formatting apiKey fails" "$script" "$TEST_TMP/apps"
rm "$src/Leak.kt"

ios="$TEST_TMP/apps/ios/Packages/Network/Sources/Network"
mkdir -p "$ios"
cat > "$ios/Session.swift" <<'SWIFT'
func leak() { print("cookie: \(sessionCookie)") }
SWIFT
assert_exit 1 "Swift print formatting sessionCookie fails" "$script" "$TEST_TMP/apps"

cat > "$ios/Session.swift" <<'SWIFT'
func fine() { os_log("token refreshed for %{public}@", profileID) }
SWIFT
assert_exit 0 "Swift os_log without secrets passes" "$script" "$TEST_TMP/apps"

# Generated code is skipped even when it contains such a line.
gen="$TEST_TMP/apps/ios/Packages/SeerrAPI/Generated"
mkdir -p "$gen"
echo 'print(apiKey)' > "$gen/Client.swift"
assert_exit 0 "generated directories are skipped" "$script" "$TEST_TMP/apps"

cat > "$ios/Custom.swift" <<'SWIFT'
func leak() { Logger().debug("plex \(plexAuth)") }
SWIFT
assert_exit 1 "custom symbol list is honoured" "$script" --symbols plexAuth "$TEST_TMP/apps"
rm "$ios/Custom.swift"

cat > "$ios/Parameterized.swift" <<'SWIFT'
func leak() { Logger(subsystem: "gauja", category: "net").debug("cookie \(sessionCookie)") }
SWIFT
assert_exit 1 "parameterized Logger initializer is scanned" "$script" "$TEST_TMP/apps"
rm "$ios/Parameterized.swift"

cat > "$src/Multiline.kt" <<'KT'
fun leak() {
    Log.d(
        TAG,
        "key=$apiKey",
    )
}
KT
assert_exit 1 "log call split across lines is scanned" "$script" "$TEST_TMP/apps"
rm "$src/Multiline.kt"

cat > "$src/Documented.kt" <<'KT'
/**
 * Never do `Log.d(TAG, apiKey)`; redact through core/common first.
 */
// Log.d(TAG, apiKey)
fun fine() { Log.d(TAG, redact(value)) }
KT
assert_exit 0 "full-line comments are not scanned" "$script" "$TEST_TMP/apps"
