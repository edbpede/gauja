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
tools/contract/python.sh tools/contract/endpoints.py
tools/tokens/generate.sh --check
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
tools/tokens/generate.sh --check
```

Omit `--platform` to generate both clients. `--check` never changes committed
output: missing, changed and extra files all fail. Normal generation replaces
only the owned generated output directories, including removing stale files.

Android generation requires JDK 17+. It downloads the pinned OpenAPI Generator
JAR into `.cache/` and verifies its SHA-256 before every execution. The real Android API module is built with its committed Gradle wrapper and JDK 17.

iOS generation requires Swift 6.3 (Xcode 26.6 in CI). Its tooling package and
transitive dependencies are pinned in `tools/codegen/ios/Package.swift` and
`tools/codegen/ios/Package.resolved`.
The app lane installs SwiftLint through Homebrew; the build plugin is pinned in the application manifest. The generator is invoked as an SPM executable with automatic resolution disabled;
Phase 3 integrates the committed output into the application package without adding build-time regeneration. Serialization and redaction tests live in the real API modules:

```bash
apps/android/gradlew --project-dir apps/android :core:api:test
swift test --package-path apps/ios/Packages/SeerrAPI
```

Android requires no Xcode; iOS requires no JVM or Android SDK. The application
lanes own regeneration, compilation and these tests. The duplicate smoke manifests,
locks and copied source trees were retired after equivalent suites passed.

## Contract changes

Update the verbatim spec and pin together. Add source-backed overlays for shape
corrections, update the editable `api/coverage.json` ledger for added/removed operations,
then regenerate both clients. The effective spec stays
temporary. Stable operation identifiers come from method and path when upstream
omits them. Generation lowers wire enums to primitives and unspecified numeric
formats to doubles; it does not alter the canonical contract. Domain unknown
cases and mapping are Phase 4 responsibilities.

The Android output intentionally omits the stock HTTP client, which installs
body logging. Phase 4 supplies profile-isolated transports. Generated credential
descriptions are redacted while transient auth payloads remain encodable.
The [modularity rules](../../.agents/rules/modularity.md) own the API → Data mapping boundary.
Kotlin emits Retrofit coroutine interfaces, kotlinx serialization and collection parameter formatting.
Phase 4 supplies profile-owned transports and `Json { ignoreUnknownKeys = true; explicitNulls = false }`.
Swift uses the locked URLSession transport. Kotlin DTO descriptions and Swift secret-bearing
descriptions/reflection are redacted; persistent credentials still belong in SecretStore/Keychain.
Outgoing domain enum choices must be validated against the effective contract.

When updating tool versions, refresh the Python hashes and generator SPM lock,
then resolve each affected app's manifests and review its dependency licenses.
Android locks are written with `exportResolvedDependencies --write-locks`; iOS
has a canonical application `Package.resolved` plus package test locks. Regenerate,
run the real API module suites, and run the affected app lane before committing.

## Generated file structure

Keep clients and themes committed, and navigate primarily through Data and domain types.
The pinned Swift generator already splits types by namespace; it has no per-operation or
maximum-lines setting. `Client.swift` and `Types+Operations.swift` remain large because the
complete API is generated. See [namespace splitting](https://github.com/apple/swift-openapi-generator/blob/1.13.1/Sources/swift-openapi-generator/Documentation.docc/Proposals/SOAR-0015.md).
Android already groups interfaces by tag and emits individual DTO files. Unused SDK documentation
and test scaffolding are not committed.

If app measurements establish a problem, trial supported operation/tag/path filters or a
separate types/client target using Swift `additionalImports` ([configuration](https://github.com/apple/swift-openapi-generator/blob/1.13.1/Sources/swift-openapi-generator/Documentation.docc/Articles/Configuring-the-generator.md)).
A shared types target alone does not shrink the operations file. Filtered targets must preserve
all contract operation IDs, including the currently generated exclusions, and resolve shared
schemas without conflicting types. Android generation-only tag grouping is another option,
but introduces a maintained interface mapping. Compare indexing, clean/incremental builds,
serialization/redaction tests and API coverage before adopting either. Never manually split
output, fork templates or replace a generator solely to satisfy a line-count preference.


### Phase 3 measurement baseline (2026-09-06)

Machine: Apple M5 (10 CPU cores), 24 GiB memory, macOS 26.6.2, arm64;
Homebrew JDK 17.0.20.1, Gradle 9.7.1, Kotlin compiler 2.4.10, Swift 6.3.3 /
Xcode 26.6. These are single local wall-clock samples with dependencies already
resolved and a warm Gradle daemon, not release performance budgets or statistical
comparisons. No generated structure was changed for these measurements.

| API build | Clean outputs | No-op | Additive schema change |
|---|---:|---:|---:|
| Android `:core:api:compileKotlin`, build cache disabled | 4.00 s | 0.60 s | 0.98 s |
| SwiftPM `SeerrAPI`, debug with index store enabled | 14.42 s | 0.38 s | 11.06 s |

Reproduce the build baseline with `:core:api:clean :core:api:compileKotlin
--no-build-cache` through the app wrapper, and `swift package --package-path
apps/ios/Packages/SeerrAPI clean` followed by `swift build --package-path
apps/ios/Packages/SeerrAPI --disable-automatic-resolution --target SeerrAPI
--enable-index-store`. Repeat each build unchanged for the no-op measurement.
For the representative edit, add one optional string property to the effective
`/status` 200 response in temporary generator input, generate normally, and apply
only the changed generated files. This changed `GetStatus200Response.kt` and
`Types+Operations.swift`. Restore the original generated output and run `--check`
after measuring; never edit the vendored specification or generated source by hand.

Android Studio Quail 4 (2026.1.4, build AI-261.26222.65.2614.16204760; bundled JBR
25.0.3, separate from the Gradle JDK) indexed an isolated API module using its real
source root and resolved runtime JARs. With fresh IDE index storage, its diagnostic
reported 2,377 indexed files, including all 208 generated Kotlin files, in 1.050 s
of index processing after a 0.567 s completed scan. The whole command-line
inspection took 15.42 s; reopening after the additive schema edit took 11.32 s,
including startup and inspections. This is an API source/library indexing sample,
not a full Android Gradle import or a measure of editor completion latency. The
IDE's inspection report exporter logged missing bundled inspection descriptions;
the indexing diagnostic itself completed without cancellation. A full-project
first-run import also failed in the IDE's welcome panel, so that attempt is not
reported as a successful indexing measurement.

The [documented inspection CLI](https://www.jetbrains.com/help/idea/command-line-code-inspector.html)
accepts a temporary `.ipr`/`.iml` project with the API source root and runtime JAR
class roots. Run `inspect.sh PROJECT PROFILE OUTPUT -v2` using separate
`STUDIO_PROPERTIES` config/system/log directories; read completed `Scanning` and
`DumbIndexing` entries in `logs/indexing-diagnostic`, not just process exit status.

Xcode's `SeerrAPI` scheme built a fresh simulator index store in 26.77 s and rebuilt
it after the same additive change in 22.93 s, including both simulator architectures
and normal build work. The cold run produced 1,131 index-store files. Reproduce with
`xcodebuild -project apps/ios/Gauja.xcodeproj -scheme SeerrAPI -destination
'generic/platform=iOS Simulator' -derivedDataPath TEMP -skipPackagePluginValidation
-onlyUsePackageVersionsFromResolvedFile build COMPILER_INDEX_STORE_ENABLE=YES
CODE_SIGNING_ALLOWED=NO`. This measures Xcode build/index production; it does not
claim a separate interactive Xcode latency measurement.

Retain the supported Kotlin and Swift namespace output. This baseline does not
justify maintaining a custom API split or generator fork. Repeat it when contract
growth produces a measurable build or navigation problem; preserve coverage and
shared-schema checks for any proposed trial.
