<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Add server

## Contract

Screen ID `servers/add`, phase 5.1. Entry and exit remain scoped to the selected profile. Auth surfaces are native forms/sheets except the explicitly external Plex system browser. Reference contract: Seerr v3.4.1 at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c`.

## Content

Display name, base URL, optional reverse-proxy Basic username/password, trust mode and returned server version/public settings. Secure fields never become ordinary preferences.

## States

- **Loading:** disable duplicate submit; preserve the profile label and permit cancellation. Refresh retains valid content.
- **Empty:** first-use fields or no profiles show the next action; an empty configuration does not enable an unsupported login method.
- **Error:** show an actionable, redacted error and retry/alternate method. Preserve non-secret draft values; never interpolate raw HTTP bodies, credentials or GUIDs.
- **Offline:** explain that sign-in, validation and server writes need connectivity. Existing per-profile cached reads may remain available with staleness; no queued mutation is silently replayed.
- **Permission-denied:** explain server rejection/config restrictions and offer the permitted alternate method or profile list. A 403 does not erase other profiles or their sessions.

## Actions

Trim input; default an omitted scheme to HTTPS; preserve explicit port and reverse-proxy path prefix. Reject userinfo, query/fragment, unsupported schemes and malformed hosts. Probe status/public settings through the intended profile transport. On TLS failure offer the fingerprint flow without sending credentials to an untrusted peer. Save only after server validation, then choose sign-in.

All network requests carry an immutable attempt/profile identity; cancel and ignore results when that identity changes. Secrets are transient secure input and then SecretStore/Keychain only, never saved-state payloads, logs, fixtures or diagnostics. Error handling never broadens TLS trust or follows a credential-bearing redirect to another origin.

## Adaptive behavior

Compact: native form/sheet and stacked back navigation. Medium: bounded readable form width using size classes. Expanded: profile list/detail uses native adaptive scaffolds; auth forms stay associated with the selected profile. Resizing or folding preserves non-secret draft/selection, never starts a second auth attempt. The system back gesture cancels the current attempt.

## Accessibility

Secure input labels identify the credential and server. Announce validation/pending/expiry without reading secret values. Support keyboard submit/focus order, native 48 dp/44 pt targets, largest Dynamic Type/font scaling and reduced motion. Fingerprint/code can wrap and remains selectable only where explicitly safe; never copy the Quick Connect secret.

## Endpoints

GET /status; GET /settings/public; GET /status/appdata for the supported server warning. Paths are relative to `/api/v1`. Request/response shapes come from the effective spec. Authenticated requests go to the profile’s Seerr origin only; cookies never cross profiles.

## Permissions

No Seerr session required. HTTP is allowed only as an explicit URL choice with a persistent warning; system trust is the default. Unsupported server versions receive an explanation and gated features.

## Content components

Native form, secure field, progress indicator, alert and action button primitives from DesignSystem. Profile/status warnings may use StatusBadge semantics. No content-media card is required for sign-in. Component behavior follows `../components/INVENTORY.md`.

## Acceptance criteria

The initial check and the later profile setup have separate acceptance scopes.

### Initial server-check slice (Phase 3)

The initial app opens this form with a server address and a **Check server** action. It
checks `GET /status?checkUpdateAvailable=false`, then `GET /settings/public`, and displays
the application title, server version, initialization state, restart warning and reported
sign-in capabilities. These capabilities are informational; profile saving and sign-in
are not part of this slice. A successful probe is never described as authentication.

- Empty/invalid addresses never start requests. Preserve ports and proxy prefixes; reject
  userinfo, queries, fragments and malformed hosts. Omitted schemes default to HTTPS.
- Checking disables duplicate submission and permits cancellation. Changing the address,
  leaving the screen or starting another attempt cancels old work; late results are ignored.
- Offline, denied (401/403), TLS and malformed-response failures show distinct, actionable
  messages without raw response bodies. Preserve the address for retry.
- Use system trust. Explain that Basic-auth credentials and fingerprint approval arrive
  with profile setup; never silently bypass either. Explicit HTTP shows a persistent warning.
- Results remain associated with their address, including during compact/regular resizing.
  Refresh on foreground only after an explicit successful check; no background polling.
- The app performs no persistence and sends no credentials or cookies. Each attempt owns
  an ephemeral transport. Cross-origin redirects and HTTPS downgrades fail closed.
- Verify unknown wire values, unsupported/untested versions, cancellation, both themes,
  largest text, native touch targets and screen-reader order on both platforms.

The broader profile/auth requirements below remain Phase 5 work.

Pass auth matrix rows **S04, S05, S06, S07, S08, S09** on Android and iOS. Exercise all five states, cancellation/late responses, a second profile with different credentials, compact/expanded resizing, largest text and screen-reader traversal. A successful auth result is not activated until auth/me succeeds. Existing cached reads meet the ≤300 ms offline target; no background work is introduced. Implementation tests belong to phase 5.1; this specification does not claim those runtime tests have passed.
