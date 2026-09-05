<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0001: Pure Kotlin and pure Swift in one repository; no Kotlin Multiplatform; contract-only sharing

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §11, §12.3, §13](../gauja-prd.md#11-tech-stack) |

## Context

Two native apps could share networking, models and business logic through Kotlin Multiplatform, or share nothing but an agreed contract. KMP buys shared code at the price of a second toolchain inside the iOS build, a Kotlin runtime in the iOS binary, non-idiomatic Swift APIs at the boundary, and a build that neither platform maintainer fully owns. The Seerr API surface is stable enough that a generated client per platform costs less than a shared one.

## Decision

Gauja is one Android app in pure Kotlin and one iOS app in pure Swift, developed together in a single repository. They share only a contract: the vendored OpenAPI specification with overlays, recorded fixtures and `compat.json` under `api/`, and design tokens plus screen specifications under `design/`. Generated artifacts flow from the contract into each app; nothing flows sideways. No shared runtime code exists, and no shared runtime code will be introduced.

## Consequences

- Each app builds, tests and lints alone; a contributor needs only one toolchain.
- Domain models, mappers and permission tables are written twice, from the same contract, and kept in parity by the contract tests and screen specs.
- Nothing under `apps/android/` references `apps/ios/` or vice versa; `pr-hygiene.yml` enforces it.
- A shared `common/` directory, a KMP module, or any cross-app import is a defect, not a style choice (see `.agents/rules/monorepo.md`).
