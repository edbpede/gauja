<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Authentication and server test matrix

Every case is required on both native platforms when Phase 5 lands. These are acceptance requirements, not recorded fixtures or completed runtime tests. Use an initialized Phase 11 test container, LAN HTTPS, a Basic-auth reverse proxy and self-signed TLS variants. Inject clock/dispatchers and controllable responses for deterministic unit tests.

Quick Connect timing follows pinned `src/hooks/useQuickConnect.ts`: 2-second checks, expiry on 404, five consecutive failures before stopping. Native implementations also enforce foreground-only, single-flight checks and attempt cancellation.

| Case | Setup / trigger | Expected outcome |
|---|---|---|
| A01 | Initialized server with localLogin false | Local sign-in absent; no guessed fallback method. |
| A02 | Uninitialized server | Offer explicit browser setup; never call initialize. |
| A03 | Older/unknown server version or missing public flags | Show range explanation; unsupported methods remain gated. |
| A04 | Valid local credentials | One auth/local, then auth/me; activate only that profile. |
| A05 | Invalid password / 401 | Redacted inline error; no session activated. |
| A06 | Repeated submit / cancel / rotation | Single flight; non-secret draft survives; canceled response cannot sign in. |
| A07 | Valid Jellyfin and Emby credentials | Correct server type and shape sent to Seerr only. |
| A08 | Emby or disabled media login | No Quick Connect for Emby; disabled login explained. |
| A09 | Plex approved PIN | Token exchanged once with the intended Seerr server; no embedded web view. |
| A10 | Plex token or PIN expires before exchange | No session; offer a fresh attempt; discard expired token. |
| A11 | Plex system browser canceled / user denies access | Stop attempt; no stale success after cancellation. |
| A12 | Plex app backgrounds, returns, or switches profile | No app polling in background; resume only current nonexpired attempt. |
| A13 | Quick Connect waiting | Display code only; foreground checks every 2 s, no overlap. |
| A14 | Quick Connect authenticated | Stop polling; exactly one authenticate call, then auth/me. |
| A15 | Quick Connect check 404 | Expired message; restart uses a fresh code/secret. |
| A16 | Five consecutive transient check failures | Stop and offer retry; one successful check resets the counter. |
| A17 | Quick Connect dismissal/background/profile change | Cancel or pause; ignore late callbacks from old attempts. |
| A18 | Quick Connect unavailable/403 | Explain; ordinary Jellyfin login remains an alternative if enabled. |
| A19 | API key without warning acknowledgment | No profile saved; operator scope explained. |
| A20 | Valid/invalid API key | Use X-Api-Key alone, never X-API-User or another profile’s cookie; label Operator after validation. |
| A21 | Reset request for existing/nonexisting email | Neutral completion text does not disclose account existence. |
| A22 | Valid reset link and matching new passwords | Submit once; clear draft; return to sign-in. |
| A23 | Expired GUID / mismatched passwords / wrong profile host | Actionable error; no unintended server request and no GUID diagnostics. |
| A24 | Profile A receives 401 while profile B is valid | Invalidate A only; B’s cookies, cache and UI remain intact. |
| A25 | 403 on an authenticated action | Permission-denied state; no global logout or automatic mutation replay. |
| A26 | Logout online/offline | Clear local credentials for that profile; explain unconfirmed remote logout offline. |
| S01 | No profiles / named profiles reordered | Meaningful empty state or persisted order; stable profile IDs. |
| S02 | Switch between cookie and operator profiles | Separate transports, cookie jars, keys, permissions and image caches. |
| S03 | Delete active or final profile | Choose next remaining profile or show empty list; no remote account deletion. |
| S04 | HTTPS URL, explicit port, proxy path prefix | Normalize without dropping the proxy prefix; status/public use correct URL. |
| S05 | Reverse proxy with valid/invalid Basic auth | Basic header only to intended origin; distinguish proxy rejection from Seerr auth. |
| S06 | Self-signed TLS first connection | Show full SHA-256 fingerprint; send no credentials until explicit confirmation. |
| S07 | Pin mismatch / confirmation canceled | Fail closed; no silent pin replacement or trust-all retry. |
| S08 | Explicit plain HTTP URL | Allow with persistent transport warning; never silently downgrade HTTPS. |
| S09 | Malformed scheme / URL userinfo / query / fragment | Reject at boundary with field error; no network request. |
| S10 | Edit profile endpoint to different origin | Invalidate old session; validate and reauthenticate; no automatic credential forwarding. |
| S11 | Failed edit validation or cancel | Existing profile remains unchanged. |
| S12 | Change trust mode or certificate | Rebuild only that profile’s transport; new pin requires explicit confirmation. |
| S13 | Old request finishes after switching profiles | Cache/update stays associated with old profile; active UI never shows it. |
| S14 | Foreground with restartRequired / appdata warning | Refresh status/public settings and show the server warning without background polling. |
| S15 | Deletion wipe fails partway | Report failure; never claim all data removed until every owned store/cache is wiped. |

## Shared assertions

For every row: no credential/GUID in logs or diagnostics; no unauthorized peer; no cross-profile session mixing; loading/empty/error/offline/denied state exercised where applicable; largest text and screen-reader labels work at compact and expanded widths. Repeated taps and late responses never cause duplicate writes. Use synthetic test values outside api/fixtures until real container recording.

Password-reset OS deep-link registration and actual container fixture capture remain Phase 11. Tests must not install background polling workers.
