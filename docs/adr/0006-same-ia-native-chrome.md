<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0006: Same information architecture, tokens and content components; platform-native chrome

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §8](../gauja-prd.md#8-ux-principles) |

## Context

Users who know Seerr's web UI should recognise Gauja immediately, yet an app that copies web chrome or one platform's idioms onto the other feels wrong and fights the system. The two apps must also stay in behavioural parity without sharing code (ADR 0001).

## Decision

Screen names, groupings and the order of settings sections follow Seerr's web UI. Colour, spacing, radii, typography and motion come from `design/tokens.json`, from which both platform themes are generated. Content components (`TitleCard`, `MediaSlider`, `RequestCard`, `StatusBadge`, …) are shared by name, behaviour and screen spec, and implemented idiomatically per platform. Chrome is native: Material 3 (Adaptive) on Android, iOS 18 SwiftUI on iOS. Dark is the default theme; light is generated from the same tokens.

## Consequences

- Screen specs in `design/screens/` are the contract between the apps and are written before UI code.
- Hand-editing a generated theme fails CI.
- Adaptive layouts and accessibility are requirements, checked per screen spec.
