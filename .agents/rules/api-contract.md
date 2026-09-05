---
type: "agent_requested"
description: "Gauja API contract rules: vendored Seerr OpenAPI spec, overlays, fixtures, generated clients, compat gating"
---

# API contract

Everything both apps know about Seerr's API comes from `api/`. This file says how that directory is maintained and how the apps consume it. It is normative and expands PRD §4 and §12.2. The Seerr source tree is inspiration only, never a dependency; never code against a remembered or guessed API.

## Contract maintenance

[api/README.md](../../api/README.md) owns the supported baseline, verbatim spec/pin pairing,
ordered evidence-backed overlays, complete operation coverage and recording conventions.
Follow those procedures; do not duplicate them in other documents. The pinned Seerr MIT notice
lives in `LICENSES/MIT.txt`; [third-party provenance](../../docs/THIRD_PARTY.md) owns attribution
and distribution responsibilities.

Record real server behavior with the first consuming flow, using plan §11.6's early tasks.
Synthetic serialization cases remain tooling tests, not recorded server evidence. Never commit
credentials or weaken a mapper to hide a fixture decoding failure.

## Generated clients

- Android: openapi-generator (`kotlin` generator, kotlinx-serialization, `jvm-retrofit2` library) into `apps/android/core/api/`. iOS: `swift-openapi-generator` into `apps/ios/Packages/SeerrAPI/Generated/`.
- [Codegen setup](../../tools/codegen/README.md) owns generator commands, pins, supported file structure and compile harnesses. Generation goes through that entry point; `--check` verifies all owned output byte-for-byte.
- Generated code is excluded from formatters, linters and the whitespace hooks (see the `GENERATED` comment in `prek.toml`), annotated in `REUSE.toml`, and never hand-edited. A hand edit is a CI failure by construction.
- [Modularity](modularity.md) owns API → Data → domain exposure. DTOs are accessible only to Data and test support; Data exposes domain types. The import guard is active; graph checks land with real modules.
- Wire decoding is defensive: unknown keys are ignored; optional fields retain defaults/nullability. A generation-only lowering represents upstream enums as primitive wire values; the effective contract retains its enum constraints. Phase 4 Data mappers turn unrecognized values into explicit domain `Unknown` cases. Do not remove required fields wholesale. Generated secret-bearing descriptions are redacted; transient auth DTO wire encoding is permitted, while persistence and diagnostics must use SecretStore/Keychain and redaction. A decode failure on a recorded fixture is an overlay candidate; a crash on a live server is a defect.

## Compatibility gating (`api/compat.json`)

- `compat.json` maps a feature key to `{ "min": "<semver>", "max": "<semver>|null", "endpoint": "<path>" , "note": "<why>" }`. Phase 4 bundles the JSON as a resource in each app and decodes it into `FeatureGate` metadata; current tooling validates it. No table generator is needed.
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
