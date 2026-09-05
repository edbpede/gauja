<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Gauja — Product Requirements Document

| Field | Value |
|---|---|
| Status | Draft v0.1 |
| Date | 2026-09-05 |
| Product | Gauja — native Android and iOS companion apps for Seerr |
| License | AGPL-3.0-or-later, with the App Store Distribution Exception (Appendix A) |
| Companion documents | `docs/TECH_SPEC.md` (how), `docs/IMPLEMENTATION_PLAN.md` (order), `docs/adr/` (decisions), `docs/screens/` (screen inventory) |
| Normative coding guidelines | `.agents/rules/kotlin-2_4-android-app.md`, `.agents/rules/swift-6_3-ios-app.md` (see §12.1) |

This document states **what** Gauja is and **why** each decision was made. It deliberately carries some material that a conventional PRD would push elsewhere — the licensing exception, the quality-gate configuration, the codebase doctrine — because losing those details is a bigger risk to this project than an over-long PRD.

The name: Gauja was a VEF portable transistor radio built in Riga in the early 1960s, the predecessor of the Spidola. It is one word, pronounced *GOW-ya*.

---

## 1. Summary

Gauja is an unofficial, fully native companion to [Seerr](https://github.com/seerr-team/seerr) — one Android app written in Kotlin and one iOS app written in Swift, developed together in a single repository. It lets users browse, search, and request media, and lets administrators manage requests, users, and the complete server configuration, all through native UI that follows Seerr's information architecture and visual language.

Gauja depends on nothing but the user's own Seerr server. It ships no telemetry, no third-party SDKs, no hosted services, and no web views. It is not affiliated with the Seerr project and requires no changes upstream.

---

## 2. Goals and non-goals

### 2.1 Goals

1. **Full native coverage** of the Seerr API surface: user features, admin operations, and server configuration (§5).
2. **Two first-class native apps** that share an information architecture, design tokens, and content-component vocabulary, while each respects its platform's conventions (§8).
3. **Fast and light**: measurable performance budgets (§9), small binaries, offline-tolerant reads.
4. **Private by construction**: the only network peer is the user's Seerr server, plus the image source that server is configured to use (§10).
5. **A codebase that is a pleasure to contribute to**: single-purpose files and modules, deep folder hierarchies that mirror the domain, mechanically enforced boundaries (§12).
6. **Maintainable against a moving upstream**: a pinned, vendored API contract with automated drift detection and per-version feature gating (§4).

### 2.2 Non-goals

- **No Kotlin Multiplatform, no shared runtime code.** Android is pure Kotlin; iOS is pure Swift. Only the *contract* (API spec, design tokens, screen specs, fixtures) is shared.
- **No upstream dependency.** Gauja never requires a PR to Seerr, a Seerr plugin, or a Seerr feature flag.
- **No hosted infrastructure** operated by the project in v1. (Push notifications are deferred for exactly this reason — §7.)
- **No web views.** If a Seerr feature cannot be rendered natively, it is either built natively or explicitly listed as unsupported; it is never embedded.
- **No telemetry, analytics, or crash-reporting SDKs**, opt-in or otherwise.
- **No Google Play Services or Firebase dependencies.** One Android build, F-Droid eligible.
- **No media playback.** Gauja requests media; it does not play it.
- **Not a Seerr setup wizard.** `/settings/initialize` and first-run server setup remain the web UI's job; Gauja targets an already-initialized server.

---

## 3. Users

| Persona | Needs | Typical device use |
|---|---|---|
| **Requester** | Discover, search, view details, request movies and TV (per-season), track request status, manage a watchlist, report and follow issues, edit own profile and notification preferences. | Phone, short sessions, often away from home. |
| **Admin** | Everything above, plus: approve/decline/retry requests, manage users (permissions, quotas, linked accounts), triage issues, and change any server setting Seerr's web UI exposes. | Phone for triage; tablet or foldable for configuration. |
| **Instance operator** | Same as Admin, but also cares about: self-signed certificates, reverse proxies with basic auth, multiple servers (home and a friend's), API-key sign-in for automation accounts, and being told when the server version is outside the app's support window. | Any. |

All three personas are assumed to be capable adults running self-hosted software. The app explains technical states plainly rather than hiding them.

---

## 4. Compatibility policy

Seerr moves quickly and its API is not semantically versioned: the OpenAPI document declares `info.version: 1.0.0` while the application is at `0.1.0`, and the specification is hand-maintained and may drift from real server behaviour. Gauja's compatibility policy is designed around those facts.

### 4.1 The vendored contract

- `api/seerr-api.yml` is a verbatim copy of upstream's `seerr-api.yml` (OpenAPI 3.0.2, 167 paths at the time of writing).
- `api/UPSTREAM_COMMIT` records the exact upstream commit the copy was taken from. The two files change together or not at all; a pre-commit hook and a CI check enforce this (§14).
- `api/LICENSE.upstream` carries Seerr's MIT license text, which covers the vendored specification.
- `api/overlays/` holds OpenAPI overlay documents that correct upstream spec defects (missing `required`, wrong types) without editing the vendored file. Every overlay entry cites the upstream issue or the observed server behaviour that justifies it.

### 4.2 Version gating

- On connect and on every foreground, Gauja calls `/status` and records `version`, `commitTag`, `updateAvailable` and `restartRequired` for the active server profile.
- `api/compat.json` maps feature identifiers to minimum (and, where needed, maximum) server versions. Features outside the window are hidden or disabled with an inline explanation, never crashed.
- Each Gauja release states a **supported Seerr range** in its release notes. The floor is the version whose spec is vendored; the ceiling is "latest known at release" with a soft warning beyond it.

### 4.3 Deprecation signals

Seerr emits RFC 8594 headers (`Deprecation`, `Sunset`, `Link rel="successor-version"`) on deprecated routes. Gauja's network layer records these per endpoint and surfaces them in the About → Diagnostics screen and in debug logs. A CI job fails when a vendored endpoint Gauja calls is marked deprecated with a `Sunset` date inside the next 90 days.

### 4.4 Upstream drift detection

A scheduled workflow (`api-sync.yml`) fetches upstream `seerr-api.yml` from the `develop` branch, diffs it against the vendored copy, and opens a pull request with the diff, an updated `UPSTREAM_COMMIT`, and a regenerated client for both platforms. Humans review; nothing merges automatically.

### 4.5 Contract tests

CI runs a real Seerr container (upstream `Dockerfile`, SQLite) seeded with fixtures, and executes recorded request/response contract tests against it for every endpoint Gauja uses. Recorded fixtures live in `api/fixtures/<seerr-version>/` and are scanned for credentials on every commit (§14).

---

## 5. Feature inventory

Organised by API tag family. **Parity** is required unless a cell says otherwise; "platform-adapted" means the same information and actions with platform-native chrome. Every row here maps to one or more screen specs in `docs/screens/`.

### 5.1 Authentication and servers (`/auth`, `/status`)

| Feature | Endpoints | Notes |
|---|---|---|
| Add server by URL | `/status`, `/settings/public` | Validates reachability and version; reads public settings to learn which sign-in methods are enabled. |
| Plex sign-in | `/auth/plex` | PIN flow against plex.tv in a system browser tab; the token is exchanged with Seerr. |
| Jellyfin / Emby sign-in | `/auth/jellyfin` | Username + password. |
| Jellyfin Quick Connect | `/auth/jellyfin/quickconnect/*` | Code display, polling, authenticate. |
| Local sign-in | `/auth/local` | Email + password. |
| API-key sign-in | `X-Api-Key` header | For automation/service accounts; key stored in Keystore/Keychain. |
| Password reset | `/auth/reset-password`, `/auth/reset-password/{guid}` | Request + deep-link completion. |
| Session | `/auth/me`, `/auth/logout` | Per-server cookie jar (`connect.sid`). |
| Multiple servers | — | Named profiles, one active at a time, quick switcher. |
| TLS trust | — | Per-profile: system trust (default), or pinned self-signed certificate fingerprint accepted after explicit user confirmation. |
| Reverse-proxy basic auth | — | Optional per-profile credentials sent as `Authorization: Basic`. |

### 5.2 Discover (`/discover`, `/genres`, `/studio`, `/network`, `/keyword`, `/watchproviders`, `/regions`, `/languages`, `/certifications`, `/backdrops`)

| Feature | Notes |
|---|---|
| Home sliders | Server-defined slider set from `/settings/discover`; each slider maps to its `/discover/*` endpoint. Order and visibility follow the server. |
| Trending, popular movies/TV, upcoming movies/TV | Paginated grids with infinite scroll. |
| Genre, studio, network, keyword, language browse | `/discover/movies/genre/{id}` etc., with the genre slider endpoints for the entry cards. |
| Watch-provider filter | `/watchproviders/*` and `/regions`, persisted per profile. |
| Plex/Jellyfin watchlist slider | `/discover/watchlist` when the server exposes it. |
| Admin: slider management | Add, reorder, enable/disable, reset (`/settings/discover/*`). Platform-adapted (drag reorder). |

### 5.3 Search (`/search`, `/search/company`, `/search/keyword`)

Debounced multi-search across movies, TV and people, with company and keyword lookups used by the discover filters. Results page uses the same content cards as discover.

### 5.4 Media details (`/movie`, `/tv`, `/person`, `/collection`, `/media`)

| Feature | Notes |
|---|---|
| Movie, TV, season, person, collection pages | Full metadata, cast/crew, ratings (`/ratings`, `/ratingscombined`), recommendations, similar, external links, trailers (link-out). |
| Availability and request state | Derived from the embedded `mediaInfo`; season-level for TV. |
| Watch data | `/media/{id}/watch_data` (Tautulli) shown when the server has it. |
| Admin: media management | Mark available / partially available / unknown, delete media, view file paths (`/media/{id}/*`). |
| Blocklist / blacklist | Add/remove titles and collections (`/blocklist/*`, `/blacklist/*`); blocklisted-tag badges. |

### 5.5 Requests (`/request`, `/service`, `/overrideRule`)

| Feature | Notes |
|---|---|
| Create request | Movie; TV with season picker; 4K variant when enabled; advanced options (server, profile, root folder, tags, language profile) when the user has permission, populated from `/service/radarr|sonarr/*`. |
| My requests | List with filters (pending, approved, available, declined, failed) and sort. |
| Request detail | Status timeline, requester, modified-by, downloads. |
| Admin: manage requests | Approve, decline, retry, edit, delete; bulk actions from the list. |
| Admin: override rules | CRUD on `/overrideRule/*`. |
| Counts | `/request/count` for badges. |

### 5.6 Issues (`/issue`, `/issueComment`)

Create issue on media (type, season/episode, message); list and filter; detail with comment thread; comment, edit, delete own comment; admin: resolve, reopen, delete. `/issue/count` for badges.

### 5.7 Watchlist (`/watchlist`)

Add/remove, list, sync status with linked Plex/Jellyfin watchlist where the server supports it.

### 5.8 Profile and user settings (`/user/{id}/*`, `/auth/me`)

Own profile with request history and quotas; general settings (display name, locale, region, original language, discover region); password change; linked accounts (Plex, Jellyfin incl. Quick Connect); **notification preferences** (`/user/{id}/settings/notifications`) — types per enabled agent, Discord/Telegram/Pushover/Pushbullet identifiers. See §7 for what is *not* included.

### 5.9 Admin: users (`/user`, `/user/{id}/permissions`, `/user/{id}/quota`, `/user/import-from-*`)

User list with search and sort; create local user; edit any user's settings, permissions (full Seerr permission matrix), quotas; import users from Plex/Jellyfin; delete. Bulk permission edit.

### 5.10 Admin: server settings (`/settings/*`) — full native coverage

| Seerr section | Endpoints | Gauja screens |
|---|---|---|
| General | `/settings/main`, `/settings/main/regenerate`, `/settings/public` | Application title/URL, API key (view, regenerate), locale, region, language, hide-available, CSRF, cache images, trust proxy, etc. |
| Users | (see §5.9) plus default permissions, default quotas, local login, new Plex login toggles in `/settings/main` | |
| Plex | `/settings/plex`, `/settings/plex/devices/servers`, `/settings/plex/library*`, `/settings/plex/sync`, `/settings/plex/users` | Server picker from devices, manual host, libraries with sync, full/recent scan controls, Tautulli (`/settings/tautulli`). |
| Jellyfin / Emby | `/settings/jellyfin*` | Host, external URL, libraries, sync, users. |
| Services | `/settings/radarr*`, `/settings/sonarr*` | List, add/edit/delete, test connection, profiles/root folders/tags loaded after test. Default-server rules. |
| Metadata | `/settings/metadatas`, `/settings/metadatas/test` | Provider selection and test. |
| Network | `/settings/network` | Proxy configuration, DNS cache, IP forwarding. |
| Notifications | `/settings/notifications/*` (every agent, incl. `test`) | Native forms for email, Discord, Pushbullet, Pushover, Gotify, ntfy, Slack, Telegram, web push, webhook. Webhook JSON template gets a monospaced editor with template-variable insertion. |
| Discover | `/settings/discover*` | (see §5.2) |
| Jobs & Cache | `/settings/jobs*`, `/settings/cache*` | Run, cancel, reschedule (cron editor with presets); flush caches and DNS entries. |
| Logs | `/settings/logs` | Filterable, searchable, copyable log viewer. |
| About | `/settings/about`, `/status`, `/status/appdata` | Version, commit, update availability, restart-required banner, data directory, appdata warning. |

Settings screens are **adaptive**: a list/detail layout on tablets and foldables, a stacked navigation on phones.

### 5.11 Cross-cutting

- Permission-aware UI: every action is shown only when `/auth/me` permissions allow it, mirroring Seerr's `lib/permissions`.
- Deep links: `gauja://server/<profileId>/movie/<tmdbId>` etc., plus Seerr web URLs (`https://<host>/movie/<id>`) registered as app links / universal links.
- Offline: last successful discover, requests, watchlist and profile responses are cached locally and shown with a staleness indicator; all writes require connectivity and fail visibly.
- Accessibility: Dynamic Type / font scaling, TalkBack / VoiceOver labels on all content cards, reduced-motion respected.
- Localization: see §16.

---

## 6. Authentication and server model

- A **server profile** is: display name, base URL, TLS trust mode (system | pinned fingerprint), optional basic-auth credentials, auth method (cookie session | API key), and the cached `/status` and `/settings/public` snapshots.
- Secrets (session cookie, API key, basic-auth password, Plex token) live only in Android Keystore-backed encrypted storage and iOS Keychain. They never appear in logs, fixtures, crash output or exported diagnostics (§10, §14).
- Each profile owns an isolated cookie jar. Switching profiles never mixes sessions.
- Plex sign-in opens plex.tv in a system browser tab (Custom Tabs / `ASWebAuthenticationSession`), never an embedded web view.
- The app detects `/status.restartRequired` and `/status/appdata` and shows the same banners Seerr's web UI does.

---

## 7. Notifications — deferred

**Decision:** push notifications are out of scope for v1. The design that was investigated is recorded in `docs/adr/0002-notifications-deferred.md` so that the eventual implementation starts from a decided architecture.

What v1 ships:

- A greyed-out **Notifications** entry in Gauja's app settings labelled "Coming later", with a one-line explanation and a link to the ADR in the About screen.
- The per-user **notification preferences** screen (§5.8), because it is an ordinary Seerr settings endpoint and works today for every agent the admin has enabled.
- The admin **notification agent** configuration screens (§5.10), for the same reason.

What v1 deliberately does not ship: any background polling worker, any push subscription, any relay, any `push` module. The ADR captures why: Seerr's per-user, multi-device channel is web push; on Android the Google-free path is UnifiedPush; on iOS the only wake-up path is APNs, which requires project-operated infrastructure; end-to-end encryption via RFC 8291 holds on every tier. That is a v2 conversation.

---

## 8. UX principles

**Same information architecture, same tokens, same content components. Platform-native chrome.**

- **Information architecture** follows Seerr's web UI: Discover, Requests, Issues, Users (admin), Settings (admin), Profile, Search. Screen names, groupings and the order of settings sections match Seerr so that anyone who knows the web UI knows Gauja.
- **Design tokens** live in `design/tokens.json` — the single source of truth for colour, spacing, radii, elevation, typography scale and motion durations. Both platform themes are *generated* from it (`tools/tokens/`); hand-editing a generated theme is a CI failure. The initial token set is derived from Seerr's Tailwind configuration: gray-900/800/700 surfaces, indigo-600 accent, the indigo-400→purple-400 gradient for hero text, Seerr's badge colour semantics for request status.
- **Content components** are shared by name and behaviour, not by code: `TitleCard`, `MediaSlider`, `RequestCard`, `IssueBlock`, `StatusBadge`, `PersonCard`, `DownloadBlock`, `AirDateBadge`, and so on, mirroring Seerr's `src/components/` vocabulary. Each has a screen spec describing content, states and interactions; each platform implements it idiomatically.
- **Chrome is native.** Navigation bars, tabs, sheets, dialogs, pull-to-refresh, haptics, context menus, and system back behave as Material 3 (Adaptive) on Android and as iOS 18 SwiftUI on iOS. Gauja does not imitate one platform on the other.
- **Dark first.** Seerr is dark; Gauja's default theme is dark with a light theme generated from the same tokens. Follow-system is available.
- **Adaptive layouts** are a requirement, not a stretch goal: list/detail for settings and requests on tablets and foldables, multi-column discover grids, Material 3 Adaptive on Android and `NavigationSplitView` on iOS.

Screen specs (`docs/screens/<area>/<screen>.md`) are the contract between the two apps: content, states (loading, empty, error, offline, permission-denied), actions, and analytics-free acceptance criteria.

---

## 9. Performance and efficiency targets

Targets are measured on a mid-range reference device (Android: Pixel 8a or equivalent; iOS: iPhone 13) in release builds, with a warm server on a LAN.

| Metric | Target |
|---|---|
| Cold start to interactive Discover (cached) | ≤ 800 ms Android, ≤ 600 ms iOS |
| Cold start to interactive Discover (uncached, network) | ≤ 1.5 s |
| Scroll jank on Discover and Requests lists | < 1 % janky frames over a 30 s scroll |
| Image loading | Progressive; memory cache ≤ 64 MB, disk cache ≤ 256 MB, configurable; images requested through Seerr's `/imageproxy` when the server has image caching enabled, otherwise from the TMDB CDN at the size the layout needs |
| Install size | ≤ 15 MB Android (per-ABI split), ≤ 20 MB iOS (thinned) |
| Battery | No background work in v1 (§7); foreground network only |
| Memory | No retained-heap growth across 50 navigation cycles (leak check in CI) |
| Offline reads | Cached discover/requests/watchlist/profile render in ≤ 300 ms with no network |

Implementation guidance (paging, stable keys, image sizing, list prefetch) belongs to `TECH_SPEC.md`; the targets above are the product commitment.

---

## 10. Privacy and security

- **Network peers**: the active Seerr server, plex.tv during Plex sign-in only, and the image CDN the server is configured to use. Nothing else, ever. CI includes a network-egress test that fails on any other host.
- **No identifiers**: no advertising ID, no device fingerprinting, no analytics.
- **Secrets**: Android Keystore (encrypted DataStore) and iOS Keychain only. A secret-logging guard hook (§14) rejects code that formats a secret into a log call. Diagnostics export redacts hosts, cookies and keys.
- **TLS**: system trust by default; pinned self-signed fingerprints require explicit per-profile confirmation showing the fingerprint. Plain HTTP is allowed (LAN use) with a persistent warning.
- **Permissions**: Android — `INTERNET` only (plus `POST_NOTIFICATIONS` reserved for v2). iOS — none beyond network.
- **Local data**: server profiles and caches are removable per profile and in bulk; deleting a profile wipes its cookie jar, keys and cache.
- **Supply chain**: dependency versions are pinned; `gitleaks` and a license allow-list run in CI; the SBOM is published with each release.

---

## 11. Tech stack

### 11.1 Shared contract (`api/`, `design/`)

- OpenAPI 3.0.2 specification vendored from upstream (§4).
- `design/tokens.json` in the W3C Design Tokens format.
- Screen specifications in Markdown.
- Recorded response fixtures per Seerr version.

### 11.2 Android (`apps/android/`)

Governed by `.agents/rules/kotlin-2_4-android-app.md`.

| Concern | Choice |
|---|---|
| Language / build | Kotlin 2.4 (K2), AGP 9.x with built-in Kotlin, KSP2, Gradle version catalog |
| SDK | minSdk 30 (Android 11; fixed by the guideline — build behind availability checks, do not raise), targetSdk/compileSdk 37 (per the Kotlin rule file) |
| UI | Jetpack Compose (BOM-governed), Material 3, Material 3 Adaptive |
| Navigation | Navigation 3, single Activity, owned back stack |
| DI | Hilt via KSP |
| Networking | OkHttp 5 + Retrofit 3 with kotlinx-serialization; generated client from openapi-generator (`kotlin` generator, `jvm-okhttp4`/retrofit template) isolated in `core/api` |
| Persistence | Room (KSP) for caches; DataStore (Preferences + encrypted Proto) for settings and profiles |
| Images | Coil 3 with the OkHttp network fetcher |
| Concurrency | Coroutines + Flow, structured; no `GlobalScope` |
| Testing | JUnit, Turbine, Compose UI tests, Robolectric for units, an emulator smoke lane |
| Quality | ktfmt, detekt (with the Compose ruleset), Android Lint, baseline profiles |

One build flavour. No Google Play Services, no Firebase.

### 11.3 iOS (`apps/ios/`)

Governed by `.agents/rules/swift-6_3-ios-app.md`.

| Concern | Choice |
|---|---|
| Language / build | Swift 6.3, Swift 6 language mode, strict concurrency, `SWIFT_DEFAULT_ACTOR_ISOLATION = MainActor`; Xcode project generated by XcodeGen from `project.yml`, never committed |
| Target | iOS 18.0+ |
| UI | SwiftUI, `NavigationStack` / `NavigationSplitView` |
| State | Observation (`@Observable`), not `ObservableObject` |
| DI | `swift-dependencies` |
| Networking | `URLSession`; generated client from `swift-openapi-generator` (SPM plugin) isolated in the `SeerrAPI` package |
| Persistence | SwiftData for caches; Keychain for secrets; `UserDefaults` via a typed wrapper for non-secret settings |
| Images | A small `URLSession`-based pipeline with `NSCache` and on-disk cache (choice of a third-party loader such as Nuke is an open question — §18) |
| Testing | Swift Testing; XCUITest smoke lane on a simulator |
| Quality | swift-format (toolchain), SwiftLint strict |

### 11.4 Tooling (`tools/`, root)

prek (pre-commit), gitleaks, conventional commits, DCO, REUSE/SPDX, Crowdin, openapi-generator and swift-openapi-generator invoked via `tools/codegen/`, token generation via `tools/tokens/`, API drift check via `tools/api-drift/`.

---

## 12. Codebase doctrine

This section is normative. It reads as rules because it is enforced as rules: by lint configuration, by module dependency constraints, and by code review.

### 12.1 Rule files

Normative coding guidelines live in `.agents/rules/` and **must be followed** for their respective platforms. They are authoritative over this document on matters of language, framework and library usage; where they are silent, this section applies.

| File | Scope |
|---|---|
| `.agents/rules/kotlin-2_4-android-app.md` | All Kotlin under `apps/android/` |
| `.agents/rules/swift-6_3-ios-app.md` | All Swift under `apps/ios/` |
| `.agents/rules/modularity.md` | Module boundaries, dependency direction, file-organisation rules (this section, expanded) — both platforms |
| `.agents/rules/api-contract.md` | How generated clients are produced, isolated and wrapped; how overlays and fixtures are maintained |
| `.agents/rules/monorepo.md` | Directory ownership, CI lanes, what may and may not cross the `apps/` boundary |

The three project-level files are written during Phase 1 of the implementation plan (the plan's numbering; this document's earlier "Phase 0") and are treated as living documents; a change to a rule file is a PR with a rationale, reviewed like code.

### 12.2 Single purpose, everywhere

1. **One responsibility per file.** A file contains one type, one composable/view, one use case, one mapper, or one small family of tightly related extensions. If a file needs a section comment to navigate, it is two files.
2. **One concern per module/package.** A module has one reason to change. `core/network` changes when transport changes; `core/api` changes when the spec changes; `feature/requests` changes when the requests experience changes.
3. **No god-files, no god-objects.** No `Utils`, `Helpers`, `Extensions.kt`, `Constants`, `AppState`, or `Repository` that knows more than one aggregate. Grab-bags are rejected in review.
4. **Folders nest as deep as the domain does.** `feature/settings/services/radarr/edit/` is correct; `feature/settings/RadarrEditScreen.kt` next to forty siblings is not. Flat folders with more than roughly a dozen files are a smell.
5. **Advisory size limits.** Files over ~300 lines and functions over ~40 lines produce lint *warnings*, never errors. The doctrine is about purpose, not line counts; the warning exists to prompt the question.
6. **Generated code is isolated and never edited.** Generated clients, generated themes and generated resources live in leaf modules or clearly named `Generated/` directories, are excluded from formatters, and are verified byte-for-byte against the generator in CI. Hand-written domain models wrap generated DTOs so spec churn stops at the boundary.
7. **Names say what, folders say where.** A file's name never repeats its path (`feature/requests/list/RequestListScreen.kt`, not `feature/requests/list/FeatureRequestsListScreen.kt`).
8. **Tests mirror sources.** Every source folder has a sibling test folder with the same structure; test files are named for the unit they test.

### 12.3 Module boundaries and dependency direction

Both platforms follow the same layered shape. Arrows point in the only allowed direction.

```
app  ──►  feature/*  ──►  core/ui, core/designsystem, core/data, core/navigation, core/common
                            core/data  ──►  core/api (generated), core/database, core/datastore, core/network, core/model
                            core/network ──► core/common
                            core/api (generated) ──► nothing in-repo
```

- `feature/*` modules never depend on each other. Cross-feature navigation goes through `core/navigation` keys; cross-feature data goes through `core/data`.
- `core/model` is pure Kotlin / pure Swift with no platform imports.
- Nothing depends on `app`.
- Enforcement: Gradle `dependency-analysis` and a module-graph check on Android; SPM package boundaries on iOS (a package cannot import what it does not declare), plus a script that verifies `project.yml` and each `Package.swift` conform to the allowed graph.

### 12.4 Android module map (`apps/android/`)

```
app/                          single Activity, Nav3 wiring, Hilt root, deep-link entry
core/
  api/                        GENERATED openapi client (leaf; excluded from lint/format)
  common/                     Result types, dispatchers, clock, error taxonomy
  compat/                     server-version gating driven by api/compat.json
  data/                       repositories; generated-DTO → domain mappers (one mapper per aggregate)
    auth/  discover/  media/  requests/  issues/  watchlist/  users/  settings/
  database/                   Room database, DAOs, entities (one entity per file)
  datastore/                  server profiles, preferences, encrypted secrets
  designsystem/               GENERATED theme from design/tokens.json + primitive components
  model/                      domain models (pure Kotlin)
  navigation/                 Nav3 keys and result contracts
  network/                    OkHttp/Retrofit config, per-profile cookie jars, auth & basic-auth
                              interceptors, TLS trust manager, deprecation-header recorder
  testing/                    fakes, fixtures loaders, test rules
  ui/                         shared content components (TitleCard, MediaSlider, StatusBadge, …)
feature/
  auth/         servers/       discover/      search/
  media/movie/  media/tv/      media/person/  media/collection/
  requests/     issues/        watchlist/     profile/
  users/                                            (admin)
  settings/general/  settings/plex/  settings/jellyfin/  settings/services/radarr/
  settings/services/sonarr/  settings/metadata/  settings/network/
  settings/notifications/<agent>/  settings/discover/  settings/jobs/  settings/cache/
  settings/logs/  settings/about/
```

Inside a feature module: `ui/` (screens and state holders), `domain/` (use cases), `navigation/` (keys), and nested subfolders per screen (`list/`, `detail/`, `edit/`).

### 12.5 iOS package map (`apps/ios/`)

```
project.yml                   XcodeGen; the .xcodeproj is never committed
App/                          app target: entry, scene, root navigation, deep links
Packages/
  SeerrAPI/                   GENERATED swift-openapi client (leaf; Generated/ excluded from format/lint)
  Common/                     Result, errors, clock, dependency keys
  Compat/                     server-version gating
  Model/                      domain models (pure Swift)
  Network/                    URLSession config, per-profile cookie storage, auth, TLS delegate,
                              deprecation-header recorder
  Data/                       repositories and DTO → domain mappers, one target per aggregate
  Persistence/                SwiftData models and stores; Keychain; typed defaults
  DesignSystem/               GENERATED theme from design/tokens.json + primitive views
  UI/                         shared content components
  Navigation/                 routes and result types
  Testing/                    fakes, fixture loaders
  Features/
    Auth/  Servers/  Discover/  Search/  Media/  Requests/  Issues/  Watchlist/  Profile/  Users/
    Settings/                 one target per Seerr settings section, nested per screen
```

Each package declares only the dependencies the allowed graph permits. Feature packages expose a single public entry view and route type; everything else is `internal`.

---

## 13. Monorepo layout

```
gauja/
  .agents/rules/                normative guidelines (§12.1)
  .github/
    workflows/                  android.yml · ios.yml · contract.yml · api-sync.yml · tokens-check.yml · release.yml
    CODEOWNERS · PULL_REQUEST_TEMPLATE.md · ISSUE_TEMPLATE/
  api/                          seerr-api.yml · UPSTREAM_COMMIT · LICENSE.upstream · overlays/ · fixtures/<version>/ · compat.json
  apps/
    android/                    Gradle project (§12.4)
    ios/                        XcodeGen project + SPM packages (§12.5)
  design/                       tokens.json · screens/<area>/<screen>.md · assets/ (icons, SVG sources, store art)
  docs/                         gauja-prd.md · TECH_SPEC.md · gauja-implementation-plan.md · adr/ · THIRD_PARTY.md
  tools/
    codegen/                    generator configs and wrapper scripts (android, ios)
    tokens/                     tokens.json → Compose theme / SwiftUI theme
    api-drift/                  upstream diff + UPSTREAM_COMMIT consistency
    ci/                         helper scripts (secret-logging guard, fixture scan, egress test)
    community/                  translation validation
  LICENSE                       AGPL-3.0-or-later text + the note in §15.2
  APPSTORE_EXCEPTION.md         Appendix A
  REUSE.toml · prek.toml · crowdin.yml · deny.toml (license allow-list) · renovate.json · README.md · CONTRIBUTING.md · SECURITY.md
```

- **Ownership:** `CODEOWNERS` assigns `apps/android/` and `apps/ios/` to their platform maintainers, `api/` and `design/` to both, `docs/` and root config to the project leads. A change under `apps/android/` needs no iOS reviewer and vice versa.
- **Independence:** each app is a complete, standalone build. A contributor can clone, build and test one platform without the other's toolchain. Only `api/`, `design/` and `tools/` are cross-platform inputs.
- **Cross-boundary rule:** nothing under `apps/android/` references anything under `apps/ios/` or vice versa. Generated artifacts flow *from* `api/` and `design/` *into* the apps, never sideways.

---

## 14. Quality gates

Two layers: fast local hooks via prek, and authoritative CI. The local layer mirrors CI's format/lint failures so they surface before a push; heavier gates (full compilation, emulator/simulator smoke, REUSE lint, contract tests) run only in CI.

### 14.1 Local hooks (`prek.toml`, Appendix B)

Builtin: trailing whitespace, EOF fixer, LF line endings, merge-conflict and case-conflict checks, large-file guard (512 KB), private-key detection, JSON/TOML/YAML validity, no direct commits to `main`. All whitespace fixers **exclude generated directories** so generated output stays byte-identical to the generator.

Third-party: `conventional-pre-commit` (commit-msg), `gitleaks`.

Local, path-scoped:

| Hook | Scope | Purpose |
|---|---|---|
| `dco-signoff` | commit-msg | Requires a `Signed-off-by:` trailer; fast mirror of the GitHub DCO check (§15.3) |
| `ktfmt`, `detekt` | `apps/android/` Kotlin, excluding `core/api/` generated | Gradle-driven so detekt loads the Compose ruleset; ktfmt is the formatter named by the Kotlin rule file |
| `swift-format`, `swiftlint --strict` | `apps/ios/` Swift, excluding `Packages/SeerrAPI/Generated/` | Toolchain swift-format; SwiftLint strict |
| `api-drift` | `api/seerr-api.yml`, `api/UPSTREAM_COMMIT` | Fails if the spec changed without the commit file, or vice versa |
| `tokens-check` | `design/tokens.json`, generated themes | Regenerates themes and fails on diff |
| `fixture-secrets` | `api/fixtures/**` | Rejects `X-Api-Key`, `connect.sid`, Plex/Jellyfin token patterns, VAPID keys |
| `check-secret-logging` | Kotlin and Swift sources | No log call may format a value from the secrets layer |
| `translations` | `crowdin.yml`, `strings.xml`, `*.xcstrings` | Catalog validity and key parity |
| `license-check` | manifests / lockfiles | Dependency license allow-list (`deny.toml`) |

Complexity and length lints are **advisory** (warn) and are never wired as blocking hooks (§12.2 rule 5).

### 14.2 CI lanes

| Lane | Trigger | What it does |
|---|---|---|
| `android` | changes under `apps/android/`, `api/`, `design/` | Build, unit tests, lint, module-graph check, generated-code drift check, emulator smoke, baseline profile validation, egress test |
| `ios` | changes under `apps/ios/`, `api/`, `design/` | XcodeGen, build, Swift Testing, SwiftLint, package-graph check, generated-code drift check, simulator smoke, egress test |
| `contract` | changes under `api/`, `tools/codegen/`, or weekly | Boot Seerr container, seed fixtures, run recorded contract tests for both generated clients, verify no called endpoint has an imminent `Sunset` |
| `api-sync` | scheduled (weekly) | Diff upstream spec, open PR with regenerated clients |
| `tokens-check` | changes under `design/` | Regenerate both themes, fail on diff |
| `release` | tag | Reproducible builds, SBOM, F-Droid metadata, App Store / Play upload via fastlane |

The GitHub DCO check and the REUSE lint are required status checks on every PR. `main` is protected; squash-merge with the PR title as the conventional-commit subject.

---

## 15. Licensing and distribution

### 15.1 License

Gauja is licensed **AGPL-3.0-or-later**. Every source file carries `SPDX-FileCopyrightText` and `SPDX-License-Identifier` headers; the repository is REUSE-compliant. Vendored upstream material (`api/seerr-api.yml`) is MIT and retains its license text.

For the mobile apps the AGPL's network clause is effectively dormant; it is retained so that any future server-side component (the deferred notification relay, §7) is covered automatically.

### 15.2 App Store Distribution Exception

Apple's App Store terms (and, to a lesser but real degree, Google Play's distribution agreement) impose restrictions on recipients that the (A)GPL does not permit. The original author may distribute their own code there, but every third-party contributor's copyright also governs distribution once their contribution is merged. Gauja therefore grants an **additional permission under AGPL-3.0 §7** for distribution through Apple's and Google's app stores and comparable services. The full text is `APPSTORE_EXCEPTION.md`, reproduced in Appendix A.

To close the gap between the DCO's "license indicated in the file" and this exception, the root `LICENSE` file states:

> The license of this project is the GNU Affero General Public License, version 3 or (at your option) any later version, **together with** the additional permission set out in `APPSTORE_EXCEPTION.md`. Every reference to "the license" in this repository, including in SPDX headers and in the Developer Certificate of Origin, means the AGPL together with that additional permission.

`CONTRIBUTING.md` repeats that sentence in its DCO section.

### 15.3 Contributions

- Every commit is signed off (`git commit -s`) under the Developer Certificate of Origin 1.1; the GitHub DCO check is a required status.
- No CLA. Contributors retain copyright; the DCO plus §15.2 is the whole grant.
- Conventional Commits are required; the changelog is generated from them.

### 15.4 Distribution channels

| Channel | Notes |
|---|---|
| Google Play | Single build; no Play Services dependencies |
| F-Droid | Eligible by construction (no proprietary dependencies, reproducible build); metadata maintained in-repo |
| GitHub Releases | Signed APKs and the iOS source archive |
| Apple App Store | Requires the exception in §15.2; TestFlight for beta |

Gauja is unaffiliated with the Seerr project. Store listings say so, use the name "Seerr" only descriptively, and use none of Seerr's marks or artwork.

---

## 16. Localization

- Source language: English (US). Catalogs: `strings.xml` per locale on Android, `Localizable.xcstrings` on iOS.
- Crowdin hosts translation, configured by `crowdin.yml`; `tools/community/validate-translations.py` checks catalog validity and key parity between platforms.
- Seerr's own translation catalogs (`server/i18n/locale/`, MIT) may be used as a **seed** for UI strings that are identical in meaning (status names, permission labels, settings section titles), with attribution in `docs/THIRD_PARTY.md`. Seeding is a one-time import, not an ongoing dependency.
- Both apps honour the user's Seerr locale setting for server-provided content and the device locale for UI, matching Seerr's behaviour.

---

## 17. Release and versioning

- Each app is versioned independently with semantic versions (`android/v1.2.0`, `ios/v1.2.0`), because store review cadences differ. A shared `CHANGELOG.md` is generated from conventional commits, sectioned per app.
- `api/compat.json` and the supported Seerr range in release notes are updated on every release.
- Reproducible builds are a release requirement (F-Droid verification).
- Store-listing screenshots are generated from the simulator/emulator smoke lanes so they stay in sync with the UI.

---

## 18. Risks and open questions

| # | Risk / question | Mitigation or owner |
|---|---|---|
| 1 | **Settings surface size.** 62 `/settings` paths and every notification agent form is the majority of the screen count. | Screen inventory sized before Phase 2; settings sections implemented in Seerr's sidebar order so partial progress is coherent; each section is its own module and PR. |
| 2 | **Spec drift from real behaviour.** Upstream's hand-maintained spec may not match the server. | Overlays (§4.1) plus contract tests against a real container (§4.5). |
| 3 | **Maintainer bandwidth per platform.** Two native codebases. | CODEOWNERS; each app buildable alone; screen specs let one platform lead. |
| 4 | **Apple review of an AGPL app.** | Exception (§15.2); precedent exists for GPL-family apps with such exceptions. |
| 5 | **Auth edge cases** (proxies, self-signed TLS, Plex token expiry, Quick Connect timing). | Dedicated test matrix in `docs/screens/auth/`; per-profile trust modes (§6). |
| 6 | **Android minSdk.** A floor of 30 covers ~87% of active devices (April 2026), roughly parity with the iOS 18 floor; the remaining ~13% are Android 8–10 devices. | Decided: 30. The rule file is amended to match. Revisit only by amending the rule file first. |
| 7 | **iOS image pipeline.** Own `URLSession` loader vs. a third-party library. | Decide in TECH_SPEC after measuring against §9 targets. |
| 8 | **Seerr's Overseerr/Jellyseerr merge is ongoing** (`server/lib/overseerrMerge.ts`); endpoints may be renamed with `Deprecation` headers. | §4.3 and §4.4. |
| 9 | **Webhook JSON editor.** Native editing of Seerr's templated JSON is fiddly. | Monospaced editor with variable-insertion palette and server-side validation via `/settings/notifications/webhook/test`. |
| 10 | **Notifications (deferred).** Users will ask. | Greyed-out entry, ADR link, roadmap statement in README. |

---

## Appendix A — App Store Distribution Exception (`APPSTORE_EXCEPTION.md`)

*An additional permission under section 7 of the GNU Affero General Public License, version 3.*

### Why this exception exists

This project is licensed **AGPL-3.0-or-later**. Apple distributes App Store apps, and Google distributes Google Play apps, under their own standard terms — usage rules, device limits, and restrictions on redistribution — which have long been considered (by the Free Software Foundation, and in practice in the 2011 VLC removal from the App Store) to impose restrictions on users beyond what the (A)GPL permits. Distributing a copyleft-covered work through such a service therefore requires permission from **every** copyright holder of that work.

The original author can lawfully publish their own code there — one cannot infringe one's own copyright. But the moment a third-party contribution is merged, that contributor's copyright governs distribution too, and their AGPL grant alone arguably does not authorize the store's terms. This document is that missing permission, granted by every copyright holder of the project.

### The additional permission

As an additional permission under section 7 of the GNU Affero General Public License, version 3 (or any later version), each copyright holder of this work grants you permission to convey the work, and works based on it, in object-code form through Apple's App Store and associated Apple distribution services (including TestFlight), through Google Play and associated Google distribution services, and through other application distribution services that impose comparable terms on recipients, notwithstanding that those services' terms and conditions may impose restrictions on recipients that the License would otherwise prohibit — provided that the complete corresponding source code remains available to all recipients under the GNU Affero General Public License version 3 or any later version.

### What this does not change

- **The license stays AGPL-3.0-or-later for everyone, everywhere.** This exception grants an *additional* permission for specific distribution channels; it removes no right and adds no restriction for anyone.
- **Source availability is unconditional.** Anyone may build, modify, and redistribute the app from source under the AGPL, on any platform, without Apple or Google.
- **You may remove this permission.** Section 7 of the License expressly allows recipients to remove additional permissions from their copies.
- **Contributions carry it automatically.** By submitting a contribution with a Developer Certificate of Origin sign-off (see `CONTRIBUTING.md`), you license your contribution under AGPL-3.0-or-later **together with this additional permission**.

---

## Appendix B — `prek.toml`

Adapted from the Spidola configuration. Differences: no Rust lanes; generated-code exclusions point at the OpenAPI clients and generated themes; added `api-drift`, `tokens-check`, `fixture-secrets` and a two-language `check-secret-logging`.

```toml
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later

# prek.toml — pre-commit hooks configuration
# Docs: https://prek.j178.dev/configuration/
#
# Setup: `prek install` (installs both the pre-commit and commit-msg shims).
# Run everything: `prek run --all-files` (do this after changing this file).
# See docs/gauja-implementation-plan.md — Phase 1, and docs/gauja-prd.md §14.

default_install_hook_types = ["pre-commit", "commit-msg"]

# Generated artifacts must match their generators byte-for-byte (CI drift check),
# so whitespace/EOF fixers and formatters skip them.
# GENERATED = ^(apps/android/core/api/|apps/ios/Packages/SeerrAPI/Generated/|apps/android/core/designsystem/src/main/kotlin/.*/generated/|apps/ios/Packages/DesignSystem/Sources/DesignSystem/Generated/)

[[repos]]
repo = "builtin"
hooks = [
  { id = "trailing-whitespace", exclude = "^(apps/android/core/api/|apps/ios/Packages/SeerrAPI/Generated/|apps/android/core/designsystem/src/main/kotlin/.*/generated/|apps/ios/Packages/DesignSystem/Sources/DesignSystem/Generated/)" },
  { id = "end-of-file-fixer",   exclude = "^(apps/android/core/api/|apps/ios/Packages/SeerrAPI/Generated/|apps/android/core/designsystem/src/main/kotlin/.*/generated/|apps/ios/Packages/DesignSystem/Sources/DesignSystem/Generated/)" },
  { id = "mixed-line-ending", args = ["--fix=lf"], exclude = "^(apps/android/core/api/|apps/ios/Packages/SeerrAPI/Generated/|apps/android/core/designsystem/src/main/kotlin/.*/generated/|apps/ios/Packages/DesignSystem/Sources/DesignSystem/Generated/)" },
  { id = "check-merge-conflict" },
  { id = "check-case-conflict" },
  { id = "check-added-large-files", args = ["--maxkb=512"] },
  { id = "detect-private-key" },
  { id = "check-json" },
  { id = "check-toml" },
  { id = "check-yaml" },
  { id = "no-commit-to-branch", args = ["--branch", "main"] },
]

# Conventional Commits
[[repos]]
repo = "https://github.com/compilerla/conventional-pre-commit"
rev = "v4.4.0"
hooks = [
  { id = "conventional-pre-commit", stages = ["commit-msg"] },
]

# Secret / credential leak guard (PRD §10, §14)
[[repos]]
repo = "https://github.com/gitleaks/gitleaks"
rev = "v8.30.1"
hooks = [
  { id = "gitleaks" },
]

# Local hooks — path-scoped mirrors of the CI lanes (PRD §14). Heavier gates stay in CI.
# Complexity/length lints remain ADVISORY (warn) per the modularity doctrine (PRD §12.2).
[[repos]]
repo = "local"
hooks = [
  # DCO sign-off — every commit certifies the DCO and grants the App Store exception
  # (CONTRIBUTING.md + APPSTORE_EXCEPTION.md; PRD §15). Commit with `git commit -s`.
  { id = "dco-signoff", name = "DCO sign-off (Signed-off-by)", entry = "sh -c 'grep -qE \"^Signed-off-by: .+ <.+@.+>\" \"$1\" || { echo \"Commit message is missing a Signed-off-by line (DCO). Commit with: git commit -s\"; exit 1; }' --", language = "system", stages = ["commit-msg"] },

  # API contract — spec and pinned upstream commit change together (PRD §4.1).
  { id = "api-drift", name = "api contract consistency", entry = "tools/api-drift/check-local.sh", language = "system", files = "^api/(seerr-api\\.yml|UPSTREAM_COMMIT|overlays/)", pass_filenames = false },
  # Recorded fixtures must never carry credentials (PRD §10).
  { id = "fixture-secrets", name = "fixture credential scan", entry = "tools/ci/check-fixture-secrets.sh", language = "system", files = "^api/fixtures/", pass_filenames = false },
  # Design tokens — generated themes must match tokens.json (PRD §8).
  { id = "tokens-check", name = "design tokens → themes", entry = "tools/tokens/check.sh", language = "system", files = "^(design/tokens\\.json|apps/android/core/designsystem/.*/generated/|apps/ios/Packages/DesignSystem/Sources/DesignSystem/Generated/)", pass_filenames = false },
  # Secret-logging guard — no log call may format an exposed secret value (PRD §10).
  { id = "check-secret-logging", name = "secret-logging guard", entry = "tools/ci/check-secret-logging.sh", language = "system", types_or = ["kotlin", "swift"], files = "^apps/", pass_filenames = false },
  # Translation catalogs (PRD §16).
  { id = "translations", name = "translation catalogs", entry = "python3 tools/community/validate-translations.py", language = "system", files = "(^crowdin\\.yml$|^apps/(ios/.+\\.xcstrings|android/.+/res/(values|values-[^/]+)/strings\\.xml)$)", pass_filenames = false },
  # Dependency license allow-list (PRD §10).
  { id = "license-check", name = "dependency licenses", entry = "tools/ci/check-licenses.sh", language = "system", files = "(^|/)(gradle/libs\\.versions\\.toml|Package\\.resolved|deny\\.toml)$", pass_filenames = false },

  # iOS — apps/ios/ (toolchain swift-format + SwiftLint; PRD §14 ios lane). Generated client excluded.
  { id = "swift-format", name = "swift-format", entry = "swift format --in-place", language = "system", types = ["swift"], files = "^apps/ios/", exclude = "^apps/ios/Packages/(SeerrAPI/Generated|DesignSystem/Sources/DesignSystem/Generated)/" },
  { id = "swiftlint", name = "swiftlint", entry = "swiftlint lint --strict", language = "system", types = ["swift"], files = "^apps/ios/", exclude = "^apps/ios/Packages/(SeerrAPI/Generated|DesignSystem/Sources/DesignSystem/Generated)/" },

  # Android — apps/android/ (ktfmt + detekt with the Compose ruleset; PRD §14 android lane).
  # ktfmt, not ktlint: the Kotlin rule file names ktfmt as the formatter and is authoritative (PRD §12.1).
  # Gradle-driven so detekt loads the Compose ruleset; fires only when Kotlin under apps/android/ is staged.
  { id = "ktfmt", name = "ktfmt", entry = "apps/android/gradlew --project-dir apps/android ktfmtCheck", language = "system", types = ["kotlin"], files = "^apps/android/", exclude = "^apps/android/core/(api|designsystem/.*/generated)/", pass_filenames = false },
  { id = "detekt", name = "detekt", entry = "apps/android/gradlew --project-dir apps/android detekt", language = "system", types = ["kotlin"], files = "^apps/android/", exclude = "^apps/android/core/(api|designsystem/.*/generated)/", pass_filenames = false },
]
```

---

## Appendix C — Decision log (ADR index)

| ADR | Decision |
|---|---|
| 0001 | Pure Kotlin + pure Swift in one repository; no KMP; contract-only sharing |
| 0002 | Notifications deferred; investigated design recorded (web push channel, UnifiedPush on Android, APNs relay on iOS, RFC 8291 end-to-end encryption) |
| 0003 | AGPL-3.0-or-later with App Store Distribution Exception; DCO, no CLA |
| 0004 | Single Android build; no Google Play Services / Firebase; F-Droid eligible |
| 0005 | Vendored OpenAPI spec pinned by upstream commit; overlays; contract tests against a real container |
| 0006 | Same IA / tokens / content components; platform-native chrome |
| 0007 | Full native admin configuration in v1 |
| 0008 | Android minSdk 30 (Android 11): parity with the iOS 18 floor, modern insets APIs, system dark theme; rule file amended to match |
| 0009 | Name: Gauja |

---

## Appendix D — Glossary

| Term | Meaning |
|---|---|
| **Seerr** | The upstream media-request server (merger of Overseerr and Jellyseerr). |
| **Server profile** | Gauja's record of one Seerr server plus how to reach and authenticate to it (§6). |
| **Content component** | A named, spec'd UI element shared across platforms by behaviour, not code (§8). |
| **Contract** | The shared inputs to both apps: API spec, overlays, fixtures, compat manifest, tokens, screen specs. |
| **Generated / leaf module** | Code produced by a generator, isolated, never edited, verified in CI (§12.2 rule 6). |
| **Rule file** | A normative guideline in `.agents/rules/` (§12.1). |
| **DCO** | Developer Certificate of Origin, the sign-off-based contribution certification (§15.3). |
