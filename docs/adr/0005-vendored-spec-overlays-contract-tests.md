<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0005: Vendored OpenAPI spec pinned by upstream commit, corrected by overlays, verified by contract tests against a real container

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §4](../gauja-prd.md#4-compatibility-policy) |

## Context

Seerr publishes a hand-maintained OpenAPI 3.0.2 specification that sometimes lags the server. Generating clients from it gives type safety but inherits its errors; hand-writing clients avoids the errors but drifts silently. The project must track upstream without ever requiring upstream changes.

## Decision

`api/seerr-api.yml` is vendored verbatim and pinned by `api/UPSTREAM_COMMIT`; the two change together (hook-enforced). Discrepancies are corrected by overlays under `api/overlays/`, each citing an upstream issue or observed behaviour. Both generated clients are produced only by `tools/codegen/` and verified byte-for-byte in CI. Recorded fixtures per Seerr version and contract tests against a real Seerr container prove that the effective spec matches the server. `api/compat.json` gates features by server version, and deprecation headers are recorded and surfaced.

## Consequences

- Generated DTOs never leave `core/api` / `SeerrAPI`; hand-written domain models absorb spec churn.
- A weekly `api-sync` PR carries new specs and regenerated clients.
- Spec-versus-server mismatches are overlay candidates, never crashes and never edits to the vendored file (see `.agents/rules/api-contract.md`).
