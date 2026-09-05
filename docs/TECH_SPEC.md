<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Shared contract implementation decisions

## Phase 2 baseline and boundaries

Maintainer choices made during Phase 2 planning:

- Vendor stable Seerr v3.4.1 at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c`, rather than develop. Counts are 163 paths / 212 operations. Four newer Plex/Jellyfin library paths require a future sync; the v1 inventory retains full library management using stable operations.
- Generated DTOs are public for the API → Data mapping boundary only, plus test support. Data exposes domain values outward. This resolves the previous impossible combination of module-internal DTOs and mappers in a separate module. The import guard lands now; full graph enforcement lands with Phase 3.
- Before container recording, precise pinned upstream source/spec citations may justify overlays. No invented endpoints or speculative nullable/required changes. Phase 11 records and verifies these cases.
- Hash-pinned YAML, JSON Schema and JSONPath dependencies are allowed for shared contract tooling. They are build-time only and do not require either app toolchain. Token generators remain Python stdlib-only.
- Swift client output uses `Packages/SeerrAPI/Generated/`, matching normative rules and existing hook/REUSE exclusions. Phase 3 points the SPM target at that directory. This corrects Phase 2's conflicting `Sources/SeerrAPI/Generated/` path.
- Phase 2 contains the complete sized screen inventory, detailed auth/server specs and all 21 component specs. Other detailed screen specs land with their features.

## Generation and transport

One effective OpenAPI document applies ordered overlays and stable operation IDs. A temporary generator view lowers wire enums to primitive values and unspecified numeric formats to Double; the effective contract retains enum constraints. Data must map unknown values to explicit domain Unknown variants and validate outgoing enum choices. Required fields remain required. The generator view is not a new API contract.

Kotlin emits Retrofit coroutine interfaces and serializable DTOs, plus collection parameter formatting. The stock ApiClient factory and BODY logger are deliberately not generated. Phase 4 supplies profile-owned Retrofit/OkHttp and `Json { ignoreUnknownKeys = true; explicitNulls = false }`. Swift generates types/client and uses the URLSession transport selected in the isolated compile check; Phase 4 supplies the profile-owned session.

DTOs necessarily encode transient auth payloads. They are not secret persistence types. Kotlin DTO descriptions and Swift secret-bearing descriptions/reflection are generated redacted. SecretStore/Keychain remain the only persistent credential stores. No generated logger or default global authenticated transport ships.

Both clients and themes are committed in Phase 2 for real byte-for-byte checks. Later Phase 4 checklist entries refer to integrating and verifying these outputs, not manual regeneration edits. Generator-supported namespace files are treated as generated artifacts, exempt from handwritten one-type-per-file guidance. The two oversized Swift namespace/client files receive a narrow 512 KB hook exception with generated drift checks; unrelated files retain the limit.

## Verification boundaries

Phase 2 checks compile clients and replay synthetic serialization cases in isolated JVM/SPM harnesses; these are not real-server contract tests or full app builds. Android/iOS application, module graph, navigation, performance, security storage and transport integration checks remain Phase 3/4/11 work. No runtime feature-gate implementation, container recording, scheduled sync PRs, deep-link registration or image-loader choice is brought forward.

The mandated JUnit 4 harness uses EPL-1.0. `deny.toml` permits this only for the test runner; it is never an application runtime dependency. Tooling dependency licenses and palette attribution are recorded in `docs/THIRD_PARTY.md`.
