---
type: "agent_requested"
description: "Gauja API contract rules: vendored Seerr OpenAPI spec, overlays, fixtures, generated clients, compat gating"
---

# API contract

Everything both apps know about Seerr's API comes from `api/`. This file says how that directory is maintained and how the apps consume it. It is normative and expands PRD §4 and §12.2 rule 6. The Seerr source tree is inspiration only, never a dependency; never code against a remembered or guessed API.

## Vendoring

- `api/seerr-api.yml` is upstream's `seerr-api.yml` **verbatim** (OpenAPI 3.0.2, MIT, license text in `api/LICENSE.upstream`). It is never edited by hand.
- `api/UPSTREAM_COMMIT` identifies the upstream commit the spec was copied from: the full 40-character SHA on its first line, followed by `# Fetched: YYYY-MM-DD`. **The two files change together or not at all**; the `api-drift` hook and CI reject a commit that touches one without the other.
- [API usage](../../api/README.md) owns the supported stable baseline. `api/coverage.json` owns operation implementation status; counts and the optional endpoint report are derived by `tools/contract/endpoints.py`. The report is not committed and coverage validation remains mandatory.
- Planned in Phase 11: weekly upstream discovery identifies changes against the pin. Baseline upgrades use a deliberately selected stable release, with the new spec, pin and regenerated clients reviewed together; discovery of develop changes does not upgrade support. Review that PR for renamed or removed operations, new enum values and new `Deprecation` markers before merging.

## Overlays (`api/overlays/`)

Upstream's spec is hand-maintained and sometimes disagrees with the server. Corrections are **overlays**, never edits to the vendored file.

- One overlay file per corrected operation or schema, named `<area>-<operation-or-schema>.yml`, in the OpenAPI Overlay 1.0 format (`overlay: 1.0.0`, `actions: [{ target: <JSONPath>, update|remove }]`).
- Every overlay starts with a comment block citing **either** an upstream issue or PR URL **or** the observed behaviour with the Seerr version, endpoint and a pointer to the recorded fixture that proves it. Before Phase 11 recordings exist, a precise pinned upstream source/spec link (`blob/<40-character SHA>/...`) plus version, operation/schema and rationale also qualifies. Source evidence must be checked against real container fixtures in Phase 11. An overlay without a citation is rejected in review.
- Overlays are applied by `tools/codegen/` before generation, in filename order; the effective spec is never committed.
- When upstream fixes the discrepancy, the overlay is deleted in the same PR that bumps `UPSTREAM_COMMIT`.
- Overlays fix shapes (nullable fields, missing properties, wrong types, missing enum values). They never invent endpoints.

## Fixtures (`api/fixtures/<seerr-version>/`)

- Recorded responses from a real Seerr container, one directory per server version, one file per operation and scenario (`request-list-page1.json`, `status-restart-required.json`).
- Recording happens through `tools/codegen/record-fixtures.sh` against the contract-test container (Phase 11); fixtures are never typed by hand.
- **Fixtures never carry credentials.** `tools/ci/check-fixture-secrets.sh` rejects API keys, `connect.sid` cookies, Plex and Jellyfin tokens, VAPID keys, PEM keys and Basic authorization values. Scrub to `REDACTED` before committing. `*.local.*` variants are git-ignored for private servers.
- Both apps' contract tests replay fixtures through the generated clients and the mappers; a fixture that fails to decode is an overlay candidate, not a reason to loosen the mapper.

## Generated clients

- Android: openapi-generator (`kotlin` generator, kotlinx-serialization, Retrofit/OkHttp template) into `apps/android/core/api/`. iOS: `swift-openapi-generator` into `apps/ios/Packages/SeerrAPI/Generated/`.
- Generation is only ever `tools/codegen/generate.sh` (both platforms by default; `--platform android|ios` for independent lanes) with the pinned generator versions in `tools/codegen/versions.env`. `tools/codegen/generate.sh --check` regenerates into a temporary directory and fails on any byte difference; CI runs it on every change under `api/`, `tools/codegen/` or the generated paths.
- Generated code is excluded from formatters, linters and the whitespace hooks (see the `GENERATED` comment in `prek.toml`), annotated in `REUSE.toml`, and never hand-edited. A hand edit is a CI failure by construction.
- Generated DTOs are public only to permit the **API → Data mapping boundary**: `core/data` / `Data` may import them, and test support may exercise them. No other production module may import them. Data’s public APIs expose domain types, never generated DTOs. `tools/ci/check-api-boundary.py` guards imports now; Phase 3 adds dependency graph enforcement. `core/data` / `Data` owns aggregate-focused mappers (`RequestMapper.kt`, `RequestMapper.swift`) that produce the hand-written domain models in `core/model` / `Model`.
- Wire decoding is defensive: unknown keys are ignored; optional fields retain defaults/nullability. A generation-only lowering represents upstream enums as primitive wire values; the effective contract retains its enum constraints. Phase 4 Data mappers turn unrecognized values into explicit domain `Unknown` cases. Do not remove required fields wholesale. Generated secret-bearing descriptions are redacted; transient auth DTO wire encoding is permitted, while persistence and diagnostics must use SecretStore/Keychain and redaction. A decode failure on a recorded fixture is an overlay candidate; a crash on a live server is a defect.

## Compatibility gating (`api/compat.json`)

- `compat.json` maps a feature key to `{ "min": "<semver>", "max": "<semver>|null", "endpoint": "<path>" , "note": "<why>" }`. Phase 4 will generate each app’s `FeatureGate` table from it; current tooling validates the metadata.
- The active server's `/status` is refreshed on connect and on every foreground. A feature outside its range is hidden or disabled with an inline explanation, never invoked and never allowed to crash.
- `Deprecation`, `Sunset` and `Link rel="successor-version"` response headers are recorded per endpoint by `core/network` / `Network` and surfaced under About → Diagnostics. An endpoint with a `Sunset` inside the next 90 days fails the contract lane so a successor is wired before it disappears. The v3.4.1 supported floor uses `/blocklist`; `/blacklist` is excluded from callable coverage (`Sunset: 2026-06-01`). Supporting an older legacy range requires an explicit future gate.
- Enum ordinals, permission bits and status values come from Seerr's `server/constants/` and `server/lib/permissions.ts` and must match bit-for-bit; the tables in `core/model` / `Model` cite the upstream file and commit they were transcribed from.

## Anti-patterns

| Wrong | Why | Right |
|---|---|---|
| Editing `api/seerr-api.yml` to fix a field | Diverges from upstream; lost on next sync | An overlay with a citation |
| Bumping `UPSTREAM_COMMIT` without the spec (or vice versa) | Pin lies | Change both in one commit |
| A generated DTO in `feature/*` | Spec churn reaches UI | Domain model via a mapper in `core/data` / `Data` |
| Hand-typing a fixture | Tests prove nothing | Record it from a container and scrub it |
| Calling a gated feature outside its supported range | Crashes older servers | Gate it and explain inline |
| Copying a permission bit from memory | Off-by-one bugs in admin UI | Transcribe from `server/lib/permissions.ts` with the commit cited |
