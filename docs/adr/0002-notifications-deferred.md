<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ADR 0002: Push notifications are deferred beyond v1; the investigated design is recorded here

| Field | Value |
|---|---|
| Status | Accepted |
| Date | 2026-09-05 |
| Governing PRD section | [`docs/gauja-prd.md` §7](../gauja-prd.md#7-notifications--deferred) |

## Context

Users of a request app expect to hear when a request is approved or available. Seerr delivers per-user notifications through configurable agents; the one that reaches individual devices without third-party accounts is **web push** (`/user/{id}/settings/notifications` with `webpush` and `/user/{id}/pushSubscription`), which uses VAPID-signed HTTP requests to a push service endpoint and RFC 8291 end-to-end encryption of the payload.

The investigated design, per platform:

- **Channel.** Seerr's web push agent. Gauja registers a push subscription per profile exactly as the web UI does, so the server needs no change (a hard requirement of this project).
- **Android without Google.** [UnifiedPush](https://unifiedpush.org): the app registers with a user-chosen distributor (ntfy, NextPush, …) and receives an endpoint URL; that URL is what Gauja hands to Seerr as the subscription endpoint. Seerr's web push agent posts RFC 8030 messages to it; UnifiedPush distributors relay opaque bytes; the app decrypts with the RFC 8291 keys it generated. No Play Services, no Firebase, no project-run server. Requires an ECDH P-256 keypair and auth secret per subscription, stored in `SecretStore`, and a foreground-free decrypt path.
- **iOS.** The only wake-up path is APNs, which Seerr cannot address directly and which needs a project-operated **relay**: an HTTPS endpoint that accepts web push (RFC 8030 + RFC 8291 ciphertext) and forwards it to APNs as a Notification Service Extension payload; the extension decrypts on device with the same RFC 8291 keys. The relay never sees plaintext but is project-hosted infrastructure with availability, abuse and privacy obligations, and it is the one component the AGPL network clause would govern.
- **Encryption.** RFC 8291 (aes128gcm content encoding, RFC 8188) holds end-to-end on every tier: server encrypts to the device's public key; distributors and the relay forward ciphertext only.
- **Background execution.** Android: UnifiedPush delivers via a broadcast; decryption and the notification post run in a short worker. iOS: the service extension runs for the notification only; no background polling anywhere.

What blocks v1: the iOS path requires project-hosted infrastructure (PRD §2.2 non-goal for v1), UnifiedPush adoption needs a distributor chooser UX and fallback story, and both need a privacy statement update. None of it changes Seerr.

## Decision

Push notifications are out of scope for v1. v1 ships a greyed-out **Notifications** entry in app settings labelled "Coming later" with a one-line explanation and a link to this ADR from About; the per-user notification preferences screen and the admin notification-agent screens ship because they are ordinary Seerr settings. v1 ships no background polling worker, no push subscription, no relay and no `push` module. The design above is the starting point for v2, not an open question.

## Consequences

- No background work in v1 (PRD §9): battery and privacy claims stay simple.
- `POST_NOTIFICATIONS` is declared but unused on Android, reserved for v2.
- A v2 implementation starts from: web push channel, UnifiedPush on Android, APNs relay on iOS, RFC 8291 end-to-end; the relay is the only new infrastructure and is AGPL-covered by construction.
- Any PR adding polling, a push module or a relay before this ADR is superseded is rejected (plan Appendix B).
