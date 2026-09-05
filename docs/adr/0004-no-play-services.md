<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0004: Single Android build with no Google Play Services or Firebase; F-Droid eligible by construction

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §10, §11.2, §15.4](../gauja-prd.md#10-privacy-and-security) |

## Context

Play Services and Firebase bring push, crash reporting and analytics at the cost of proprietary dependencies, a Google account requirement on the device, telemetry the project cannot audit, and F-Droid ineligibility. Gauja's network peers are the user's Seerr server, plex.tv during sign-in, and the configured image host; nothing else is needed for the product.

## Decision

Gauja ships one Android build flavour with no Google Play Services, no Firebase and no proprietary SDKs. The same APK goes to Google Play, F-Droid and GitHub Releases. Crash reporting, analytics and advertising identifiers are absent by design; the egress allow-list and CI egress test reject any other host.

## Consequences

- F-Droid eligibility and reproducible builds are release requirements (PRD §17).
- Push notifications cannot use FCM; the Google-free path is UnifiedPush (ADR 0002).
- Any dependency with network behaviour of its own fails review; `tools/ci/egress-test.sh` fails CI on unexpected hosts.
