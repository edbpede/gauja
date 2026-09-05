<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Jellyfin Quick Connect

## Contract

Screen ID `auth/quick-connect`, phase 5.2. Entry and exit remain scoped to the selected profile. Auth surfaces are native forms/sheets except the explicitly external Plex system browser. Reference contract: Seerr v3.4.1 at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c`.

## Content

One-time authorization code, Jellyfin instructions, loading/waiting/authorized/expired states, restart and cancel controls. The associated secret is never rendered or copied.

## States

- **Loading:** disable duplicate submit; preserve the profile label and permit cancellation. Refresh retains valid content.
- **Empty:** first-use fields or no profiles show the next action; an empty configuration does not enable an unsupported login method.
- **Error:** show an actionable, redacted error and retry/alternate method. Preserve non-secret draft values; never interpolate raw HTTP bodies, credentials or GUIDs.
- **Offline:** explain that sign-in, validation and server writes need connectivity. Existing per-profile cached reads may remain available with staleness; no queued mutation is silently replayed.
- **Permission-denied:** explain server rejection/config restrictions and offer the permitted alternate method or profile list. A 403 does not erase other profiles or their sessions.

## Actions

POST initiate, display code, then GET check with the secret every two seconds while foregrounded, with at most one request in flight. Stop on authenticated=true and POST authenticate exactly once. A 404 means expired; restart creates a new secret. Stop after five consecutive transient check failures, reset that counter after a successful check, and offer retry. Cancel on dismissal/profile change; backgrounding pauses polling. Late results from prior attempts are ignored.

All network requests carry an immutable attempt/profile identity; cancel and ignore results when that identity changes. Secrets are transient secure input and then SecretStore/Keychain only, never saved-state payloads, logs, fixtures or diagnostics. Error handling never broadens TLS trust or follows a credential-bearing redirect to another origin.

## Adaptive behavior

Compact: native form/sheet and stacked back navigation. Medium: bounded readable form width using size classes. Expanded: profile list/detail uses native adaptive scaffolds; auth forms stay associated with the selected profile. Resizing or folding preserves non-secret draft/selection, never starts a second auth attempt. The system back gesture cancels the current attempt.

## Accessibility

Secure input labels identify the credential and server. Announce validation/pending/expiry without reading secret values. Support keyboard submit/focus order, native 48 dp/44 pt targets, largest Dynamic Type/font scaling and reduced motion. Fingerprint/code can wrap and remains selectable only where explicitly safe; never copy the Quick Connect secret.

## Endpoints

POST /auth/jellyfin/quickconnect/initiate; GET /auth/jellyfin/quickconnect/check; POST /auth/jellyfin/quickconnect/authenticate; GET /auth/me. Paths are relative to `/api/v1`. Request/response shapes come from the effective spec. Authenticated requests go to the profile’s Seerr origin only; cookies never cross profiles.

## Permissions

compat quickconnect and JELLYFIN only. The backend may reject Quick Connect when disabled; explain that response and offer ordinary Jellyfin sign-in.

## Content components

Native form, secure field, progress indicator, alert and action button primitives from DesignSystem. Profile/status warnings may use StatusBadge semantics. No content-media card is required for sign-in. Component behavior follows `../components/INVENTORY.md`.

## Acceptance criteria

Pass auth matrix rows **A13, A14, A15, A16, A17, A18** on Android and iOS. Exercise all five states, cancellation/late responses, a second profile with different credentials, compact/expanded resizing, largest text and screen-reader traversal. A successful auth result is not activated until auth/me succeeds. Existing cached reads meet the ≤300 ms offline target; no background work is introduced. Implementation tests belong to phase 5.2; this specification does not claim those runtime tests have passed.
