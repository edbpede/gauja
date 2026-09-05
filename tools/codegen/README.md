<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Shared contract tooling

Run from the repository root. Python 3.12+ and git are required. Install the
hash-pinned build dependencies once with `tools/contract/python.sh --install`.
Nothing in this environment ships in either app. Tokens use Python's standard
library and need no installation.

```bash
tools/api-drift/check-local.sh --working-tree
tools/contract/python.sh tools/contract/endpoints.py --check
tools/tokens/check.sh
tools/tests/run.sh
```

The default API drift check validates the git index, including spec/pin pairing.
CI uses `--range BASE HEAD` to check pairing in every commit and then validates
the working tree. `python3 tools/api-drift/check-upstream.py` additionally checks
the spec and MIT notice against the pinned upstream bytes over HTTPS.

## Generate and verify

```bash
tools/codegen/generate.sh --platform android
tools/codegen/generate.sh --check --platform android
tools/codegen/generate.sh --platform ios
tools/codegen/generate.sh --check --platform ios
tools/tokens/generate.sh
tools/tokens/check.sh
```

Omit `--platform` to generate both clients. `--check` never changes committed
output: missing, changed and extra files all fail. Normal generation replaces
only the owned generated output directories, including removing stale files.

Android generation requires JDK 17+. It downloads the pinned OpenAPI Generator
JAR into `.cache/` and verifies its SHA-256 before every execution. The isolated
JVM smoke build requires Gradle 9.7.1 and JDK 17+; CI uses JDK 17.

iOS generation requires Swift 6.3 (Xcode 26.6 in CI). Its tooling package and
transitive dependencies are pinned in `tools/codegen/ios/Package.swift` and
`tools/codegen/ios/Package.resolved`.
CI installs the checksum-pinned SwiftLint 0.65.1 archive rather than relying on
the runner PATH. The generator is invoked as an SPM executable with automatic resolution disabled;
the application package/plugin integration belongs to Phase 3. Smoke compilation
uses OpenAPIURLSession and the locked runtime dependencies.

```bash
python3 tools/codegen/smoke.py --platform android
python3 tools/codegen/smoke.py --platform ios
```

These compile committed clients and run synthetic optional-field, unknown-enum
and secret-description tests. They are independent: Android requires no Xcode,
and iOS requires no JVM or Android SDK. App builds and graph checks begin in
Phase 3; server recordings and container contract tests begin in Phase 11.

## Contract changes

Update the verbatim spec and pin together. Add source-backed overlays for shape
corrections, update `api/coverage.json` for added/removed operations, regenerate
`api/coverage.json`, then regenerate both clients. The effective spec stays
temporary. Stable operation identifiers come from method and path when upstream
omits them. Generation lowers wire enums to primitives and unspecified numeric
formats to doubles; it does not alter the canonical contract. Domain unknown
cases and mapping are Phase 4 responsibilities.

The Android output intentionally omits the stock HTTP client, which installs
body logging. Phase 4 supplies profile-isolated transports. Generated credential
descriptions are redacted while transient auth payloads remain encodable.
See `docs/TECH_SPEC.md` and `.agents/rules/api-contract.md` for the public DTO
mapping boundary and rationale.

When updating tool versions, refresh the Python hashes, SPM locks and Gradle
smoke lock deliberately. For Gradle, run the rendered smoke project with
`--write-locks` and copy its `gradle.lockfile` back to `tools/codegen/android/smoke/`.
For SPM, resolve the relevant package with the new exact versions and copy the
smoke project's `Package.resolved` back to `tools/codegen/ios/smoke/`. Review transitive license
changes, regenerate, and run both smoke builds and `prek run --all-files`.

Keep the isolated smoke manifests and locks until the real API modules compile independently and run equivalent serialization/redaction tests. At that point remove the duplicate harness inputs and use each platform’s build configuration. Measure clean/incremental builds and indexing before changing generator structure.
