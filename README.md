<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Gauja

Gauja is an unofficial, fully native companion app for [Seerr](https://github.com/seerr-team/seerr): one Android app in pure Kotlin and one iOS app in pure Swift, built together in this repository. Browse, search and request media; administrators manage requests, users and the complete server configuration through native UI that follows Seerr's information architecture. Gauja talks only to your own Seerr server: no telemetry, no third-party services, no Google Play Services, no web views, no media playback.

**Gauja is not affiliated with, endorsed by, or maintained by the Seerr project.** "Seerr" is used descriptively to name the server the app connects to. Server bugs belong to [Seerr's issue tracker](https://github.com/seerr-team/seerr/issues).

## Status

Pre-release. The repository is in Phase 1 of `docs/gauja-implementation-plan.md` (tooling and repository hygiene); no app code exists yet.

| | |
|---|---|
| Supported Seerr versions | _to be determined per release; recorded in `api/compat.json`_ |
| Android | minSdk 30 (Android 11), no Play Services, F-Droid eligible |
| iOS | iOS 18.0 and later |
| License | AGPL-3.0-or-later with the [App Store Distribution Exception](APPSTORE_EXCEPTION.md) |

## Roadmap note on notifications

Push notifications are deferred beyond v1. The investigated design (web push, UnifiedPush on Android, an APNs relay on iOS, RFC 8291 end-to-end encryption) is recorded in [`docs/adr/0002-notifications-deferred.md`](docs/adr/0002-notifications-deferred.md). v1 ships a greyed-out "Coming later" entry, the per-user notification preferences screen and the admin notification-agent settings, all of which work against Seerr today.

## Building

Each app is a standalone build; you never need both toolchains.

- **Android** (`apps/android/`): JDK 17, Android SDK 37. `./gradlew assembleDebug` from `apps/android/`.
- **iOS** (`apps/ios/`): Xcode 26, XcodeGen. `xcodegen generate` from `apps/ios/`, then build in Xcode.

Both trees land in Phase 3 of the plan. See [`CONTRIBUTING.md`](CONTRIBUTING.md) for hooks, sign-off and PR conventions.

## Documents

- [`docs/gauja-prd.md`](docs/gauja-prd.md): product requirements
- [`docs/gauja-implementation-plan.md`](docs/gauja-implementation-plan.md): phased plan with checkboxes
- [`docs/adr/`](docs/adr/): architecture decision records
- [`.agents/rules/`](.agents/rules/): normative coding guidelines
- [`SECURITY.md`](SECURITY.md): reporting vulnerabilities
