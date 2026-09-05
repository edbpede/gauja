<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0007: Full native administration of the Seerr server in v1

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §5.10](../gauja-prd.md#510-admin-server-settings-settings--full-native-coverage) |

## Context

Seerr's settings surface is large (62 `/settings` paths, ten notification agents). Shipping a requester-only app first would be faster, but administrators are the users most likely to run their own server and least served by mobile today, and an embedded web view is a non-goal.

## Decision

v1 covers the complete server configuration natively, section by section in Seerr's `SettingsLayout.tsx` order, one module and one PR per section, with the `/settings/initialize` wizard as the only exclusion. Partial progress stays coherent because each section is independent.

## Consequences

- Phase 10 of the plan is the largest phase; the settings-parity issue template tracks gaps.
- The webhook JSON editor and other fiddly forms get native editors with server-side validation rather than a web view.
- Version gating hides sections the connected server does not support.
