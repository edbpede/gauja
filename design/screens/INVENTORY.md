<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Screen inventory

This is the complete v1 sizing inventory. S = one bounded view/action; M = a list or form with several states; L = multiple coordinated operations, rich media, auth or complex validation. Estimates describe implementation complexity per platform, not elapsed days. Sheets and substantive editors count as screens; confirmation dialogs count when they carry meaningful safety state.

Settings order is General → Users → Plex or Jellyfin/Emby → Services → Network → Metadata Providers → Notifications → Logs → Jobs/Cache → About. Discover management is a separate destination. Profile forms are reused for target-user administration without feature-to-feature dependencies.

Only auth/server detail specs are authored in Phase 2. Other paths below are reserved and must receive TEMPLATE-based specs before their feature is implemented. All 21 content-component specs are complete. Password-reset registration remains Phase 11; app push, playback, web views and server initialization are excluded.

| Area | S | M | L | Total |
|---|---:|---:|---:|---:|
| auth | 3 | 4 | 2 | 9 |
| servers | 2 | 3 | 1 | 6 |
| discover | 0 | 2 | 1 | 3 |
| search | 2 | 1 | 0 | 3 |
| media | 1 | 5 | 2 | 8 |
| requests | 0 | 5 | 4 | 9 |
| issues | 1 | 2 | 1 | 4 |
| watchlist | 0 | 1 | 0 | 1 |
| profile | 1 | 2 | 3 | 6 |
| users | 1 | 5 | 1 | 7 |
| settings/general | 0 | 1 | 1 | 2 |
| settings/users | 0 | 0 | 1 | 1 |
| settings/plex | 0 | 2 | 2 | 4 |
| settings/jellyfin | 0 | 1 | 2 | 3 |
| settings/services/radarr | 0 | 1 | 1 | 2 |
| settings/services/sonarr | 0 | 1 | 1 | 2 |
| settings/network | 0 | 0 | 1 | 1 |
| settings/metadata | 0 | 0 | 1 | 1 |
| settings/notifications | 0 | 8 | 2 | 10 |
| settings/logs | 0 | 1 | 0 | 1 |
| settings/jobs | 0 | 1 | 1 | 2 |
| settings/cache | 1 | 1 | 0 | 2 |
| settings/about | 0 | 1 | 1 | 2 |
| settings/discover | 0 | 0 | 2 | 2 |
| settings/app | 1 | 2 | 0 | 3 |
| about | 1 | 0 | 0 | 1 |
| **Total** | **14** | **50** | **31** | **95** |

Contract references may point to a Markdown file or a `file.md#section` for small behavior. Keep identities unique and detailed acceptance criteria with the consuming feature.

## auth

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `auth/methods.md` | Sign-in methods | S | 5.2 | Auth/server contract |
| `auth/local.md` | Local sign-in | S | 5.2 | Auth/server contract |
| `auth/jellyfin.md` | Jellyfin / Emby sign-in | M | 5.2 | Auth/server contract |
| `auth/plex.md` | Plex sign-in | L | 5.2 | Auth/server contract |
| `auth/quick-connect.md` | Jellyfin Quick Connect | L | 5.2 | Auth/server contract |
| `auth/api-key.md` | Operator API-key sign-in | M | 5.2 | Auth/server contract |
| `auth/reset-request.md` | Request password reset | S | 5.2 | Auth/server contract |
| `auth/reset-complete.md` | Complete password reset | M | 5.2 | Auth/server contract |
| `auth/reauthenticate.md` | Re-authenticate profile | M | 5.2 | Auth/server contract |

## servers

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `servers/list.md` | Server profiles | M | 5.1 | Auth/server contract |
| `servers/add.md` | Add server | L | 5.1 | Auth/server contract |
| `servers/edit.md` | Edit server | M | 5.1 | Auth/server contract |
| `servers/trust.md` | Confirm certificate fingerprint | M | 5.1 | Auth/server contract |
| `servers/switcher.md` | Switch server | S | 5.1 | Auth/server contract |
| `servers/delete.md` | Delete server profile | S | 5.1 | Auth/server contract |

## discover

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `discover/home.md` | Discover | L | 6.1 | Required with feature |
| `discover/grid.md` | Media results / see all | M | 6.1 | Required with feature |
| `discover/filters.md` | Browse and watch-provider filters | M | 6.1 | Required with feature |

## search

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `search/results.md` | Search and recent searches | M | 6.2 | Required with feature |
| `search/company.md` | Company picker | S | 6.2 | Required with feature |
| `search/keyword.md` | Keyword picker | S | 6.2 | Required with feature |

## media

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `media/movie.md` | Movie details | L | 7.1 | Required with feature |
| `media/tv.md` | TV details | L | 7.1 | Required with feature |
| `media/season.md` | Season and episode details | M | 7.1 | Required with feature |
| `media/person.md` | Person details | M | 7.1 | Required with feature |
| `media/collection.md` | Collection details | M | 7.1 | Required with feature |
| `media/manage.md` | Manage media / files | M | 7.1 | Required with feature |
| `media/blocklist.md` | Blocklist title or collection | M | 7.1 | Required with feature |
| `media/downloads.md` | Download progress details | S | 7.1 | Required with feature |

## requests

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `requests/list.md` | Requests | L | 7.2 | Required with feature |
| `requests/detail.md` | Request details | M | 7.2 | Required with feature |
| `requests/create-movie.md` | Request movie | M | 7.2 | Required with feature |
| `requests/create-tv.md` | Request TV seasons | L | 7.2 | Required with feature |
| `requests/advanced.md` | Advanced request options | L | 7.2 | Required with feature |
| `requests/edit.md` | Edit request | M | 7.2 | Required with feature |
| `requests/bulk.md` | Bulk request actions | M | 7.2 | Required with feature |
| `requests/override-list.md` | Override rules | M | 7.2 | Required with feature |
| `requests/override-edit.md` | Add / edit override rule | L | 7.2 | Required with feature |

## issues

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `issues/list.md` | Issues | M | 7.3 | Required with feature |
| `issues/detail.md` | Issue and comments | L | 7.3 | Required with feature |
| `issues/create.md` | Report issue | M | 7.3 | Required with feature |
| `issues/edit-comment.md` | Edit comment | S | 7.3 | Required with feature |

## watchlist

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `watchlist/list.md` | Watchlist | M | 7.4 | Required with feature |

## profile

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `profile/overview.md` | Profile and quotas | M | 8.1 | Required with feature |
| `profile/general.md` | General user settings | M | 8.1 | Required with feature |
| `profile/password.md` | Change password | S | 8.1 | Required with feature |
| `profile/linked-accounts.md` | Linked accounts | L | 8.1 | Required with feature |
| `profile/notifications.md` | Notification preferences | L | 8.1 | Required with feature |
| `profile/permissions.md` | Permissions view / editor | L | 8.1 | Required with feature |

## users

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `users/list.md` | Users | M | 9.1 | Required with feature |
| `users/create.md` | Create local user | M | 9.1 | Required with feature |
| `users/import.md` | Import Plex / Jellyfin users | L | 9.1 | Required with feature |
| `users/detail.md` | User details and settings entry | M | 9.1 | Required with feature |
| `users/quotas.md` | Edit quotas | M | 9.1 | Required with feature |
| `users/bulk-permissions.md` | Bulk permission edit | M | 9.1 | Required with feature |
| `users/delete.md` | Delete user | S | 9.1 | Required with feature |

## settings/general

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/general/main.md` | General | L | 10.1 | Required with feature |
| `settings/general/api-key.md` | View / regenerate API key | M | 10.1 | Required with feature |

## settings/users

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/users/defaults.md` | Users defaults | L | 10.2 | Required with feature |

## settings/plex

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/plex/server.md` | Plex server | L | 10.3 | Required with feature |
| `settings/plex/libraries.md` | Plex libraries and scans | L | 10.3 | Required with feature |
| `settings/plex/users.md` | Plex users | M | 10.3 | Required with feature |
| `settings/plex/tautulli.md` | Tautulli | M | 10.3 | Required with feature |

## settings/jellyfin

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/jellyfin/server.md` | Jellyfin / Emby server | L | 10.3 | Required with feature |
| `settings/jellyfin/libraries.md` | Libraries and scans | L | 10.3 | Required with feature |
| `settings/jellyfin/users.md` | Jellyfin users | M | 10.3 | Required with feature |

## settings/services/radarr

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/services/radarr/list.md` | Radarr servers | M | 10.4 | Required with feature |
| `settings/services/radarr/edit.md` | Add / edit Radarr | L | 10.4 | Required with feature |

## settings/services/sonarr

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/services/sonarr/list.md` | Sonarr servers | M | 10.4 | Required with feature |
| `settings/services/sonarr/edit.md` | Add / edit Sonarr | L | 10.4 | Required with feature |

## settings/network

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/network/main.md` | Network | L | 10.5 | Required with feature |

## settings/metadata

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/metadata/providers.md` | Metadata Providers | L | 10.6 | Required with feature |

## settings/notifications

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/notifications/email.md` | Email agent | L | 10.7 | Required with feature |
| `settings/notifications/discord.md` | Discord agent | M | 10.7 | Required with feature |
| `settings/notifications/gotify.md` | Gotify agent | M | 10.7 | Required with feature |
| `settings/notifications/ntfy.md` | Ntfy agent | M | 10.7 | Required with feature |
| `settings/notifications/pushbullet.md` | Pushbullet agent | M | 10.7 | Required with feature |
| `settings/notifications/pushover.md` | Pushover agent | M | 10.7 | Required with feature |
| `settings/notifications/slack.md` | Slack agent | M | 10.7 | Required with feature |
| `settings/notifications/telegram.md` | Telegram agent | M | 10.7 | Required with feature |
| `settings/notifications/webpush.md` | Webpush agent | M | 10.7 | Required with feature |
| `settings/notifications/webhook.md` | Webhook agent | L | 10.7 | Required with feature |

## settings/logs

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/logs/list.md` | Logs | M | 10.8 | Required with feature |

## settings/jobs

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/jobs/list.md` | Jobs | M | 10.9 | Required with feature |
| `settings/jobs/schedule.md` | Job schedule / cron editor | L | 10.9 | Required with feature |

## settings/cache

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/cache/list.md` | Cache and DNS entries | M | 10.9 | Required with feature |
| `settings/cache/flush.md` | Flush cache | S | 10.9 | Required with feature |

## settings/about

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/about/server.md` | About server | M | 10.10 | Required with feature |
| `settings/about/diagnostics.md` | Diagnostics and redacted export | L | 10.10 | Required with feature |

## settings/discover

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/discover/sliders.md` | Discover slider management | L | 10.11 | Required with feature |
| `settings/discover/edit.md` | Add / edit custom slider | L | 10.11 | Required with feature |

## settings/app

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `settings/app/preferences.md` | App settings / theme | M | 8.2 | Required with feature |
| `settings/app/cache.md` | Per-profile image cache controls | M | 8.2 | Required with feature |
| `settings/app/notifications.md` | Notifications — Coming later | S | 8.2 | Required with feature |

## about

| ID / spec path | Screen | Size | Phase | Detail spec |
|---|---|---|---|---|
| `about/app.md` | About Gauja / licenses / source | S | 8.2 / 12 | Required with feature |

## Cross-cutting acceptance

Every row inherits TEMPLATE.md’s five states, permission and compatibility gates, adaptive layouts, accessible targets/text, reduced motion and offline write restrictions. Discover, requests, watchlist and profile reads carry per-profile staleness timestamps. Performance and memory budgets remain PRD §9 release requirements. Tests mirror the owning source screen, on both platforms.

The auth matrix is in [auth/MATRIX.md](auth/MATRIX.md). Stable endpoint coverage is in [api/coverage.json](../../api/coverage.json). Any later server setting outside the pinned API requires a contract sync before implementation, never a guessed call.
