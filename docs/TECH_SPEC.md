<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Shared contract implementation decisions

## Phase 2 baseline and boundaries

Current decisions:

- Use the supported stable [contract baseline](../api/README.md); discovery of newer upstream operations does not upgrade it automatically.
- Generated DTOs are public for the API → Data mapping boundary and tests only. Data exposes domain values outward. The import guard is active; graph enforcement lands with working Phase 3 modules.
- Before container recording, precise pinned upstream source/spec citations may justify overlays. No invented endpoints or speculative nullable/required changes. Phase 11 records and verifies these cases.
- Hash-pinned YAML, JSON Schema and JSONPath dependencies are allowed for shared contract tooling. They are build-time only and do not require either app toolchain. Token generators remain Python stdlib-only.
- Swift client output uses `Packages/SeerrAPI/Generated/`, matching normative rules and existing hook/REUSE exclusions. Phase 3 points the SPM target at that directory.
- Keep the sized screen inventory, auth/server specs and shared component baseline. Detailed specs land or are refined with their features, documenting applicable behavior only.

## Generation and transport

One effective OpenAPI document applies ordered overlays and stable operation IDs. A temporary generator view lowers wire enums to primitive values and unspecified numeric formats to Double; the effective contract retains enum constraints. Data must map unknown values to explicit domain Unknown variants and validate outgoing enum choices. Required fields remain required. The generator view is not a new API contract.

Kotlin emits Retrofit coroutine interfaces and serializable DTOs, plus collection parameter formatting. The stock ApiClient factory and BODY logger are deliberately not generated. Phase 4 supplies profile-owned Retrofit/OkHttp and `Json { ignoreUnknownKeys = true; explicitNulls = false }`. Swift generates types/client and uses the URLSession transport selected in the isolated compile check; Phase 4 supplies the profile-owned session.

DTOs necessarily encode transient auth payloads. They are not secret persistence types. Kotlin DTO descriptions and Swift secret-bearing descriptions/reflection are generated redacted. SecretStore/Keychain remain the only persistent credential stores. No generated logger or default global authenticated transport ships.

Clients and themes remain committed and follow supported generator structure. Navigate primarily through Data repositories/mappers and domain types. Measure clean/incremental builds and indexing before considering filtered modules or generator restructuring; no build-performance defect has been established. Hook exclusions are owned by `prek.toml`, generation commands by [codegen setup](../tools/codegen/README.md).

## Verification boundaries

Create modules for enforced boundaries or demonstrated build/ownership benefits. Use folders within Data and Settings initially. Retain the isolated smoke manifests/locks until real API modules independently compile and run equivalent serialization/redaction tests, then retire duplicate build inputs.

Phase 2 checks compile clients and replay synthetic serialization cases in isolated JVM/SPM harnesses; these are not real-server contract tests or full app builds. Android/iOS application, module graph, navigation, performance, security storage and transport integration checks remain Phase 3/4/11 work. No runtime feature-gate implementation, container recording, scheduled sync PRs, deep-link registration or image-loader choice is brought forward.

The mandated JUnit 4 harness uses EPL-1.0. `deny.toml` permits this only for the test runner; it is never an application runtime dependency. Tooling dependency licenses and palette attribution are recorded in `docs/THIRD_PARTY.md`.
