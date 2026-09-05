<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0008: Android minSdk 30

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §11.2 and §18 risk 6](../gauja-prd.md#18-risks-and-open-questions) |

## Context

A floor of API 30 (Android 11) covered roughly 87 % of active devices in April 2026, about parity with the iOS 18 floor. It brings the modern insets and edge-to-edge APIs, system dark theme, scoped storage and per-profile network security config without compatibility shims; the excluded ~13 % are Android 8 to 10 devices.

## Decision

minSdk is 30 and is fixed. compileSdk and targetSdk track the current SDK (37 today) per the Kotlin rule file. Any API above 30 goes behind an availability check; the floor is never raised by a feature PR. Revisiting the floor means amending the rule file first, then this ADR.

## Consequences

- No `sw600dp`-style shims or pre-30 inset workarounds in the codebase.
- New platform APIs are used behind `Build.VERSION.SDK_INT` checks with a 30 path.
- Device coverage is re-measured at each release; the floor changes only through a superseding ADR.
