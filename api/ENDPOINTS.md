<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Endpoint inventory

GENERATED — do not edit. Update `api/coverage.json`, then run
`tools/contract/python.sh tools/contract/endpoints.py`.

Pinned Seerr contract: **163 paths / 212 operations**, relative to `/api/v1`.

Counts use the first operation tag; paths shared between tags are counted in each group.
Excluded operations remain generated but are never invoked by Gauja.

| Tag | Paths | Operations |
|---|---:|---:|
| auth | 8 | 8 |
| blocklist | 5 | 10 |
| collection | 1 | 1 |
| issue | 6 | 10 |
| media | 5 | 5 |
| movies | 5 | 5 |
| other | 6 | 6 |
| overriderule | 2 | 4 |
| person | 2 | 2 |
| public | 2 | 2 |
| request | 5 | 8 |
| search | 18 | 18 |
| service | 5 | 5 |
| settings | 58 | 82 |
| tmdb | 7 | 7 |
| tv | 5 | 5 |
| users | 21 | 32 |
| watchlist | 2 | 2 |

## auth

- [ ] `POST /auth/jellyfin` — `postAuthJellyfin`; phase 5.2; planned. Sign in using a Jellyfin username and password.
- [ ] `POST /auth/jellyfin/quickconnect/authenticate` — `postAuthJellyfinQuickconnectAuthenticate`; phase 5.2; planned. Authenticate with Quick Connect.
- [ ] `GET /auth/jellyfin/quickconnect/check` — `getAuthJellyfinQuickconnectCheck`; phase 5.2; planned. Check Quick Connect authorization status.
- [ ] `POST /auth/jellyfin/quickconnect/initiate` — `postAuthJellyfinQuickconnectInitiate`; phase 5.2; planned. Initiate Jellyfin Quick Connect.
- [ ] `POST /auth/local` — `postAuthLocal`; phase 5.2; planned. Sign in using a local account.
- [ ] `POST /auth/logout` — `postAuthLogout`; phase 5.2; planned. Sign out and clear session cookie.
- [ ] `GET /auth/me` — `getAuthMe`; phase 5.2; planned. Get logged-in user.
- [ ] `POST /auth/plex` — `postAuthPlex`; phase 5.2; planned. Sign in using a Plex token.

## blocklist

- [ ] `GET /blacklist` — `getBlacklist`; phase —; excluded. Legacy alias; Sunset 2026-06-01. Use /blocklist.
- [ ] `POST /blacklist` — `postBlacklist`; phase —; excluded. Legacy alias; Sunset 2026-06-01. Use /blocklist.
- [ ] `DELETE /blacklist/{tmdbId}` — `deleteBlacklistByTmdbId`; phase —; excluded. Legacy alias; Sunset 2026-06-01. Use /blocklist.
- [ ] `GET /blacklist/{tmdbId}` — `getBlacklistByTmdbId`; phase —; excluded. Legacy alias; Sunset 2026-06-01. Use /blocklist.
- [ ] `GET /blocklist` — `getBlocklist`; phase 7.1; planned. Returns blocklisted items.
- [ ] `POST /blocklist` — `postBlocklist`; phase 7.1; planned. Add media to blocklist.
- [ ] `DELETE /blocklist/collection/{collectionId}` — `deleteBlocklistCollectionByCollectionId`; phase 7.1; planned. Remove collection from blocklist.
- [ ] `POST /blocklist/collection/{collectionId}` — `postBlocklistCollectionByCollectionId`; phase 7.1; planned. Add collection to blocklist.
- [ ] `DELETE /blocklist/{tmdbId}` — `deleteBlocklistByTmdbId`; phase 7.1; planned. Remove media from blocklist.
- [ ] `GET /blocklist/{tmdbId}` — `getBlocklistByTmdbId`; phase 7.1; planned. Get media from blocklist.

## collection

- [ ] `GET /collection/{collectionId}` — `getCollectionByCollectionId`; phase 7.1; planned. Get collection details.

## issue

- [ ] `GET /issue` — `getIssue`; phase 7.3; planned. Get all issues.
- [ ] `POST /issue` — `postIssue`; phase 7.3; planned. Create new issue.
- [ ] `GET /issue/count` — `getIssueCount`; phase 7.3; planned. Gets issue counts.
- [ ] `DELETE /issue/{issueId}` — `deleteIssueByIssueId`; phase 7.3; planned. Delete issue.
- [ ] `GET /issue/{issueId}` — `getIssueByIssueId`; phase 7.3; planned. Get issue.
- [ ] `POST /issue/{issueId}/comment` — `postIssueByIssueIdComment`; phase 7.3; planned. Create a comment.
- [ ] `POST /issue/{issueId}/{status}` — `postIssueByIssueIdByStatus`; phase 7.3; planned. Update an issue's status.
- [ ] `DELETE /issueComment/{commentId}` — `deleteIssueCommentByCommentId`; phase 7.3; planned. Delete issue comment.
- [ ] `GET /issueComment/{commentId}` — `getIssueCommentByCommentId`; phase 7.3; planned. Get issue comment.
- [ ] `PUT /issueComment/{commentId}` — `putIssueCommentByCommentId`; phase 7.3; planned. Update issue comment.

## media

- [ ] `GET /media` — `getMedia`; phase 7.1; planned. Get media.
- [ ] `DELETE /media/{mediaId}` — `deleteMediaByMediaId`; phase 7.1; planned. Delete media item.
- [ ] `DELETE /media/{mediaId}/file` — `deleteMediaByMediaIdFile`; phase 7.1; planned. Delete media file.
- [ ] `GET /media/{mediaId}/watch_data` — `getMediaByMediaIdWatchData`; phase 7.1; planned. Get watch data.
- [ ] `POST /media/{mediaId}/{status}` — `postMediaByMediaIdByStatus`; phase 7.1; planned. Update media status.

## movies

- [ ] `GET /movie/{movieId}` — `getMovieByMovieId`; phase 7.1; planned. Get movie details.
- [ ] `GET /movie/{movieId}/ratings` — `getMovieByMovieIdRatings`; phase 7.1; planned. Get movie ratings.
- [ ] `GET /movie/{movieId}/ratingscombined` — `getMovieByMovieIdRatingscombined`; phase 7.1; planned. Get RT and IMDB movie ratings combined.
- [ ] `GET /movie/{movieId}/recommendations` — `getMovieByMovieIdRecommendations`; phase 7.1; planned. Get recommended movies.
- [ ] `GET /movie/{movieId}/similar` — `getMovieByMovieIdSimilar`; phase 7.1; planned. Get similar movies.

## other

- [ ] `GET /certifications/movie` — `getCertificationsMovie`; phase 6.1; planned. Get movie certifications.
- [ ] `GET /certifications/tv` — `getCertificationsTv`; phase 6.1; planned. Get TV certifications.
- [ ] `GET /keyword/{keywordId}` — `getKeywordByKeywordId`; phase 6.1; planned. Get keyword.
- [ ] `GET /watchproviders/movies` — `getWatchprovidersMovies`; phase 6.1; planned. Get watch provider movies.
- [ ] `GET /watchproviders/regions` — `getWatchprovidersRegions`; phase 6.1; planned. Get watch provider regions.
- [ ] `GET /watchproviders/tv` — `getWatchprovidersTv`; phase 6.1; planned. Get watch provider series.

## overriderule

- [ ] `GET /overrideRule` — `getOverrideRule`; phase 7.2; planned. Get override rules.
- [ ] `POST /overrideRule` — `postOverrideRule`; phase 7.2; planned. Create override rule.
- [ ] `DELETE /overrideRule/{ruleId}` — `deleteOverrideRuleByRuleId`; phase 7.2; planned. Delete override rule by ID.
- [ ] `PUT /overrideRule/{ruleId}` — `putOverrideRuleByRuleId`; phase 7.2; planned. Update override rule.

## person

- [ ] `GET /person/{personId}` — `getPersonByPersonId`; phase 7.1; planned. Get person details.
- [ ] `GET /person/{personId}/combined_credits` — `getPersonByPersonIdCombinedCredits`; phase 7.1; planned. Get combined credits.

## public

- [ ] `GET /status` — `getStatus`; phase 5.1; planned. Get Seerr status.
- [ ] `GET /status/appdata` — `getStatusAppdata`; phase 5.1; planned. Get application data volume status.

## request

- [ ] `GET /request` — `getRequest`; phase 7.2; planned. Get all requests.
- [ ] `POST /request` — `postRequest`; phase 7.2; planned. Create new request.
- [ ] `GET /request/count` — `getRequestCount`; phase 7.2; planned. Gets request counts.
- [ ] `DELETE /request/{requestId}` — `deleteRequestByRequestId`; phase 7.2; planned. Delete request.
- [ ] `GET /request/{requestId}` — `getRequestByRequestId`; phase 7.2; planned. Get MediaRequest.
- [ ] `PUT /request/{requestId}` — `putRequestByRequestId`; phase 7.2; planned. Update MediaRequest.
- [ ] `POST /request/{requestId}/retry` — `postRequestByRequestIdRetry`; phase 7.2; planned. Retry failed request.
- [ ] `POST /request/{requestId}/{status}` — `postRequestByRequestIdByStatus`; phase 7.2; planned. Update a request's status.

## search

- [ ] `GET /discover/genreslider/movie` — `getDiscoverGenresliderMovie`; phase 6.1 / 6.2; planned. Get genre slider data for movies.
- [ ] `GET /discover/genreslider/tv` — `getDiscoverGenresliderTv`; phase 6.1 / 6.2; planned. Get genre slider data for TV series.
- [ ] `GET /discover/keyword/{keywordId}/movies` — `getDiscoverKeywordByKeywordIdMovies`; phase 6.1 / 6.2; planned. Get movies from keyword.
- [ ] `GET /discover/movies` — `getDiscoverMovies`; phase 6.1 / 6.2; planned. Discover movies.
- [ ] `GET /discover/movies/genre/{genreId}` — `getDiscoverMoviesGenreByGenreId`; phase 6.1 / 6.2; planned. Discover movies by genre.
- [ ] `GET /discover/movies/language/{language}` — `getDiscoverMoviesLanguageByLanguage`; phase 6.1 / 6.2; planned. Discover movies by original language.
- [ ] `GET /discover/movies/studio/{studioId}` — `getDiscoverMoviesStudioByStudioId`; phase 6.1 / 6.2; planned. Discover movies by studio.
- [ ] `GET /discover/movies/upcoming` — `getDiscoverMoviesUpcoming`; phase 6.1 / 6.2; planned. Upcoming movies.
- [ ] `GET /discover/trending` — `getDiscoverTrending`; phase 6.1 / 6.2; planned. Trending movies and TV.
- [ ] `GET /discover/tv` — `getDiscoverTv`; phase 6.1 / 6.2; planned. Discover TV shows.
- [ ] `GET /discover/tv/genre/{genreId}` — `getDiscoverTvGenreByGenreId`; phase 6.1 / 6.2; planned. Discover TV shows by genre.
- [ ] `GET /discover/tv/language/{language}` — `getDiscoverTvLanguageByLanguage`; phase 6.1 / 6.2; planned. Discover TV shows by original language.
- [ ] `GET /discover/tv/network/{networkId}` — `getDiscoverTvNetworkByNetworkId`; phase 6.1 / 6.2; planned. Discover TV shows by network.
- [ ] `GET /discover/tv/upcoming` — `getDiscoverTvUpcoming`; phase 6.1 / 6.2; planned. Discover Upcoming TV shows.
- [ ] `GET /discover/watchlist` — `getDiscoverWatchlist`; phase 6.1 / 6.2; planned. Get the Plex watchlist..
- [ ] `GET /search` — `getSearch`; phase 6.1 / 6.2; planned. Search for movies, TV shows, or people.
- [ ] `GET /search/company` — `getSearchCompany`; phase 6.1 / 6.2; planned. Search for companies.
- [ ] `GET /search/keyword` — `getSearchKeyword`; phase 6.1 / 6.2; planned. Search for keywords.

## service

- [ ] `GET /service/radarr` — `getServiceRadarr`; phase 7.2; planned. Get non-sensitive Radarr server list.
- [ ] `GET /service/radarr/{radarrId}` — `getServiceRadarrByRadarrId`; phase 7.2; planned. Get Radarr server quality profiles and root folders.
- [ ] `GET /service/sonarr` — `getServiceSonarr`; phase 7.2; planned. Get non-sensitive Sonarr server list.
- [ ] `GET /service/sonarr/lookup/{tmdbId}` — `getServiceSonarrLookupByTmdbId`; phase 7.2; planned. Get series from Sonarr.
- [ ] `GET /service/sonarr/{sonarrId}` — `getServiceSonarrBySonarrId`; phase 7.2; planned. Get Sonarr server quality profiles and root folders.

## settings

- [ ] `GET /settings/about` — `getSettingsAbout`; phase 10.10; planned. Get server stats.
- [ ] `GET /settings/cache` — `getSettingsCache`; phase 10.9; planned. Get a list of active caches.
- [ ] `POST /settings/cache/dns/{dnsEntry}/flush` — `postSettingsCacheDnsByDnsEntryFlush`; phase 10.9; planned. Flush a specific DNS cache entry.
- [ ] `POST /settings/cache/{cacheId}/flush` — `postSettingsCacheByCacheIdFlush`; phase 10.9; planned. Flush a specific cache.
- [ ] `GET /settings/discover` — `getSettingsDiscover`; phase 6.1 / 10.11; planned. Get all discover sliders.
- [ ] `POST /settings/discover` — `postSettingsDiscover`; phase 6.1 / 10.11; planned. Batch update all sliders..
- [ ] `POST /settings/discover/add` — `postSettingsDiscoverAdd`; phase 6.1 / 10.11; planned. Add a new slider.
- [ ] `GET /settings/discover/reset` — `getSettingsDiscoverReset`; phase 6.1 / 10.11; planned. Reset all discover sliders.
- [ ] `DELETE /settings/discover/{sliderId}` — `deleteSettingsDiscoverBySliderId`; phase 6.1 / 10.11; planned. Delete slider by ID.
- [ ] `PUT /settings/discover/{sliderId}` — `putSettingsDiscoverBySliderId`; phase 6.1 / 10.11; planned. Update a single slider.
- [ ] `POST /settings/initialize` — `postSettingsInitialize`; phase —; excluded. Server initialization remains the web UI’s job; Phase 11 container seeding only.
- [ ] `GET /settings/jellyfin` — `getSettingsJellyfin`; phase 10.3; planned. Get Jellyfin settings.
- [ ] `POST /settings/jellyfin` — `postSettingsJellyfin`; phase 10.3; planned. Update Jellyfin settings.
- [ ] `GET /settings/jellyfin/library` — `getSettingsJellyfinLibrary`; phase 10.3; planned. Get Jellyfin libraries.
- [ ] `GET /settings/jellyfin/sync` — `getSettingsJellyfinSync`; phase 10.3; planned. Get status of full Jellyfin library sync.
- [ ] `POST /settings/jellyfin/sync` — `postSettingsJellyfinSync`; phase 10.3; planned. Start full Jellyfin library sync.
- [ ] `GET /settings/jellyfin/users` — `getSettingsJellyfinUsers`; phase 10.3; planned. Get Jellyfin Users.
- [ ] `GET /settings/jobs` — `getSettingsJobs`; phase 10.9; planned. Get scheduled jobs.
- [ ] `POST /settings/jobs/{jobId}/cancel` — `postSettingsJobsByJobIdCancel`; phase 10.9; planned. Cancel a specific job.
- [ ] `POST /settings/jobs/{jobId}/run` — `postSettingsJobsByJobIdRun`; phase 10.9; planned. Invoke a specific job.
- [ ] `POST /settings/jobs/{jobId}/schedule` — `postSettingsJobsByJobIdSchedule`; phase 10.9; planned. Modify job schedule.
- [ ] `GET /settings/logs` — `getSettingsLogs`; phase 10.8; planned. Returns logs.
- [ ] `GET /settings/main` — `getSettingsMain`; phase 10.1 / 10.2; planned. Get main settings.
- [ ] `POST /settings/main` — `postSettingsMain`; phase 10.1 / 10.2; planned. Update main settings.
- [ ] `POST /settings/main/regenerate` — `postSettingsMainRegenerate`; phase 10.1 / 10.2; planned. Get main settings with newly-generated API key.
- [ ] `GET /settings/metadatas` — `getSettingsMetadatas`; phase 10.6; planned. Get Metadata settings.
- [ ] `PUT /settings/metadatas` — `putSettingsMetadatas`; phase 10.6; planned. Update Metadata settings.
- [ ] `POST /settings/metadatas/test` — `postSettingsMetadatasTest`; phase 10.6; planned. Test Provider configuration.
- [ ] `GET /settings/network` — `getSettingsNetwork`; phase 10.5; planned. Get network settings.
- [ ] `POST /settings/network` — `postSettingsNetwork`; phase 10.5; planned. Update network settings.
- [ ] `GET /settings/notifications/discord` — `getSettingsNotificationsDiscord`; phase 10.7; planned. Get Discord notification settings.
- [ ] `POST /settings/notifications/discord` — `postSettingsNotificationsDiscord`; phase 10.7; planned. Update Discord notification settings.
- [ ] `POST /settings/notifications/discord/test` — `postSettingsNotificationsDiscordTest`; phase 10.7; planned. Test Discord settings.
- [ ] `GET /settings/notifications/email` — `getSettingsNotificationsEmail`; phase 10.7; planned. Get email notification settings.
- [ ] `POST /settings/notifications/email` — `postSettingsNotificationsEmail`; phase 10.7; planned. Update email notification settings.
- [ ] `POST /settings/notifications/email/test` — `postSettingsNotificationsEmailTest`; phase 10.7; planned. Test email settings.
- [ ] `GET /settings/notifications/gotify` — `getSettingsNotificationsGotify`; phase 10.7; planned. Get Gotify notification settings.
- [ ] `POST /settings/notifications/gotify` — `postSettingsNotificationsGotify`; phase 10.7; planned. Update Gotify notification settings.
- [ ] `POST /settings/notifications/gotify/test` — `postSettingsNotificationsGotifyTest`; phase 10.7; planned. Test Gotify settings.
- [ ] `GET /settings/notifications/ntfy` — `getSettingsNotificationsNtfy`; phase 10.7; planned. Get ntfy.sh notification settings.
- [ ] `POST /settings/notifications/ntfy` — `postSettingsNotificationsNtfy`; phase 10.7; planned. Update ntfy.sh notification settings.
- [ ] `POST /settings/notifications/ntfy/test` — `postSettingsNotificationsNtfyTest`; phase 10.7; planned. Test ntfy.sh settings.
- [ ] `GET /settings/notifications/pushbullet` — `getSettingsNotificationsPushbullet`; phase 10.7; planned. Get Pushbullet notification settings.
- [ ] `POST /settings/notifications/pushbullet` — `postSettingsNotificationsPushbullet`; phase 10.7; planned. Update Pushbullet notification settings.
- [ ] `POST /settings/notifications/pushbullet/test` — `postSettingsNotificationsPushbulletTest`; phase 10.7; planned. Test Pushbullet settings.
- [ ] `GET /settings/notifications/pushover` — `getSettingsNotificationsPushover`; phase 10.7; planned. Get Pushover notification settings.
- [ ] `POST /settings/notifications/pushover` — `postSettingsNotificationsPushover`; phase 10.7; planned. Update Pushover notification settings.
- [ ] `GET /settings/notifications/pushover/sounds` — `getSettingsNotificationsPushoverSounds`; phase 10.7; planned. Get Pushover sounds.
- [ ] `POST /settings/notifications/pushover/test` — `postSettingsNotificationsPushoverTest`; phase 10.7; planned. Test Pushover settings.
- [ ] `GET /settings/notifications/slack` — `getSettingsNotificationsSlack`; phase 10.7; planned. Get Slack notification settings.
- [ ] `POST /settings/notifications/slack` — `postSettingsNotificationsSlack`; phase 10.7; planned. Update Slack notification settings.
- [ ] `POST /settings/notifications/slack/test` — `postSettingsNotificationsSlackTest`; phase 10.7; planned. Test Slack settings.
- [ ] `GET /settings/notifications/telegram` — `getSettingsNotificationsTelegram`; phase 10.7; planned. Get Telegram notification settings.
- [ ] `POST /settings/notifications/telegram` — `postSettingsNotificationsTelegram`; phase 10.7; planned. Update Telegram notification settings.
- [ ] `POST /settings/notifications/telegram/test` — `postSettingsNotificationsTelegramTest`; phase 10.7; planned. Test Telegram settings.
- [ ] `GET /settings/notifications/webhook` — `getSettingsNotificationsWebhook`; phase 10.7; planned. Get webhook notification settings.
- [ ] `POST /settings/notifications/webhook` — `postSettingsNotificationsWebhook`; phase 10.7; planned. Update webhook notification settings.
- [ ] `POST /settings/notifications/webhook/test` — `postSettingsNotificationsWebhookTest`; phase 10.7; planned. Test webhook settings.
- [ ] `GET /settings/notifications/webpush` — `getSettingsNotificationsWebpush`; phase 10.7; planned. Get Web Push notification settings.
- [ ] `POST /settings/notifications/webpush` — `postSettingsNotificationsWebpush`; phase 10.7; planned. Update Web Push notification settings.
- [ ] `POST /settings/notifications/webpush/test` — `postSettingsNotificationsWebpushTest`; phase 10.7; planned. Test Web Push settings.
- [ ] `GET /settings/plex` — `getSettingsPlex`; phase 10.3; planned. Get Plex settings.
- [ ] `POST /settings/plex` — `postSettingsPlex`; phase 10.3; planned. Update Plex settings.
- [ ] `GET /settings/plex/devices/servers` — `getSettingsPlexDevicesServers`; phase 10.3; planned. Gets the user's available Plex servers.
- [ ] `GET /settings/plex/library` — `getSettingsPlexLibrary`; phase 10.3; planned. Get Plex libraries.
- [ ] `GET /settings/plex/sync` — `getSettingsPlexSync`; phase 10.3; planned. Get status of full Plex library scan.
- [ ] `POST /settings/plex/sync` — `postSettingsPlexSync`; phase 10.3; planned. Start full Plex library scan.
- [ ] `GET /settings/plex/users` — `getSettingsPlexUsers`; phase 10.3; planned. Get Plex users.
- [ ] `GET /settings/public` — `getSettingsPublic`; phase 5.1; planned. Get public settings.
- [ ] `GET /settings/radarr` — `getSettingsRadarr`; phase 10.4; planned. Get Radarr settings.
- [ ] `POST /settings/radarr` — `postSettingsRadarr`; phase 10.4; planned. Create Radarr instance.
- [ ] `POST /settings/radarr/test` — `postSettingsRadarrTest`; phase 10.4; planned. Test Radarr configuration.
- [ ] `DELETE /settings/radarr/{radarrId}` — `deleteSettingsRadarrByRadarrId`; phase 10.4; planned. Delete Radarr instance.
- [ ] `PUT /settings/radarr/{radarrId}` — `putSettingsRadarrByRadarrId`; phase 10.4; planned. Update Radarr instance.
- [ ] `GET /settings/radarr/{radarrId}/profiles` — `getSettingsRadarrByRadarrIdProfiles`; phase 10.4; planned. Get available Radarr profiles.
- [ ] `GET /settings/sonarr` — `getSettingsSonarr`; phase 10.4; planned. Get Sonarr settings.
- [ ] `POST /settings/sonarr` — `postSettingsSonarr`; phase 10.4; planned. Create Sonarr instance.
- [ ] `POST /settings/sonarr/test` — `postSettingsSonarrTest`; phase 10.4; planned. Test Sonarr configuration.
- [ ] `DELETE /settings/sonarr/{sonarrId}` — `deleteSettingsSonarrBySonarrId`; phase 10.4; planned. Delete Sonarr instance.
- [ ] `PUT /settings/sonarr/{sonarrId}` — `putSettingsSonarrBySonarrId`; phase 10.4; planned. Update Sonarr instance.
- [ ] `GET /settings/tautulli` — `getSettingsTautulli`; phase 10.3; planned. Get Tautulli settings.
- [ ] `POST /settings/tautulli` — `postSettingsTautulli`; phase 10.3; planned. Update Tautulli settings.

## tmdb

- [ ] `GET /backdrops` — `getBackdrops`; phase 6.1; planned. Get backdrops of trending items.
- [ ] `GET /genres/movie` — `getGenresMovie`; phase 6.1; planned. Get list of official TMDB movie genres.
- [ ] `GET /genres/tv` — `getGenresTv`; phase 6.1; planned. Get list of official TMDB TV genres.
- [ ] `GET /languages` — `getLanguages`; phase 6.1; planned. Languages supported by TMDB.
- [ ] `GET /network/{networkId}` — `getNetworkByNetworkId`; phase 6.1; planned. Get TV network details.
- [ ] `GET /regions` — `getRegions`; phase 6.1; planned. Regions supported by TMDB.
- [ ] `GET /studio/{studioId}` — `getStudioByStudioId`; phase 6.1; planned. Get movie studio details.

## tv

- [ ] `GET /tv/{tvId}` — `getTvByTvId`; phase 7.1; planned. Get TV details.
- [ ] `GET /tv/{tvId}/ratings` — `getTvByTvIdRatings`; phase 7.1; planned. Get TV ratings.
- [ ] `GET /tv/{tvId}/recommendations` — `getTvByTvIdRecommendations`; phase 7.1; planned. Get recommended TV series.
- [ ] `GET /tv/{tvId}/season/{seasonNumber}` — `getTvByTvIdSeasonBySeasonNumber`; phase 7.1; planned. Get season details and episode list.
- [ ] `GET /tv/{tvId}/similar` — `getTvByTvIdSimilar`; phase 7.1; planned. Get similar TV series.

## users

- [ ] `POST /auth/reset-password` — `postAuthResetPassword`; phase 5.2 / 11.1; planned. Send a reset password email.
- [ ] `POST /auth/reset-password/{guid}` — `postAuthResetPasswordByGuid`; phase 5.2 / 11.1; planned. Reset the password for a user.
- [ ] `GET /user` — `getUser`; phase 8.1 / 9.1; planned. Get all users.
- [ ] `POST /user` — `postUser`; phase 8.1 / 9.1; planned. Create new user.
- [ ] `PUT /user` — `putUser`; phase 8.1 / 9.1; planned. Update batch of users.
- [ ] `POST /user/import-from-jellyfin` — `postUserImportFromJellyfin`; phase 8.1 / 9.1; planned. Import all users from Jellyfin.
- [ ] `POST /user/import-from-plex` — `postUserImportFromPlex`; phase 8.1 / 9.1; planned. Import all users from Plex.
- [ ] `GET /user/jellyfin/{jellyfinUserId}` — `getUserJellyfinByJellyfinUserId`; phase 8.1 / 9.1; planned. Get user by Jellyfin user ID.
- [ ] `POST /user/registerPushSubscription` — `postUserRegisterPushSubscription`; phase 8.1 / 9.1; planned. Register a web push /user/registerPushSubscription.
- [ ] `DELETE /user/{userId}` — `deleteUserByUserId`; phase 8.1 / 9.1; planned. Delete user by ID.
- [ ] `GET /user/{userId}` — `getUserByUserId`; phase 8.1 / 9.1; planned. Get user by ID.
- [ ] `PUT /user/{userId}` — `putUserByUserId`; phase 8.1 / 9.1; planned. Update a user by user ID.
- [ ] `DELETE /user/{userId}/pushSubscription/{endpoint}` — `deleteUserByUserIdPushSubscriptionByEndpoint`; phase —; excluded. Deferred push subscription (v2).
- [ ] `GET /user/{userId}/pushSubscription/{endpoint}` — `getUserByUserIdPushSubscriptionByEndpoint`; phase —; excluded. Deferred push subscription (v2).
- [ ] `GET /user/{userId}/pushSubscriptions` — `getUserByUserIdPushSubscriptions`; phase —; excluded. Deferred push subscription (v2).
- [ ] `GET /user/{userId}/quota` — `getUserByUserIdQuota`; phase 8.1 / 9.1; planned. Get quotas for a specific user.
- [ ] `GET /user/{userId}/requests` — `getUserByUserIdRequests`; phase 8.1 / 9.1; planned. Get requests for a specific user.
- [ ] `DELETE /user/{userId}/settings/linked-accounts/jellyfin` — `deleteUserByUserIdSettingsLinkedAccountsJellyfin`; phase 8.1 / 9.1; planned. Remove the linked Jellyfin account for a user.
- [ ] `POST /user/{userId}/settings/linked-accounts/jellyfin` — `postUserByUserIdSettingsLinkedAccountsJellyfin`; phase 8.1 / 9.1; planned. Link the provided Jellyfin account to the current user.
- [ ] `POST /user/{userId}/settings/linked-accounts/jellyfin/quickconnect` — `postUserByUserIdSettingsLinkedAccountsJellyfinQuickconnect`; phase 8.1 / 9.1; planned. Link Jellyfin/Emby account with Quick Connect.
- [ ] `DELETE /user/{userId}/settings/linked-accounts/plex` — `deleteUserByUserIdSettingsLinkedAccountsPlex`; phase 8.1 / 9.1; planned. Remove the linked Plex account for a user.
- [ ] `POST /user/{userId}/settings/linked-accounts/plex` — `postUserByUserIdSettingsLinkedAccountsPlex`; phase 8.1 / 9.1; planned. Link the provided Plex account to the current user.
- [ ] `GET /user/{userId}/settings/main` — `getUserByUserIdSettingsMain`; phase 8.1 / 9.1; planned. Get general settings for a user.
- [ ] `POST /user/{userId}/settings/main` — `postUserByUserIdSettingsMain`; phase 8.1 / 9.1; planned. Update general settings for a user.
- [ ] `GET /user/{userId}/settings/notifications` — `getUserByUserIdSettingsNotifications`; phase 8.1 / 9.1; planned. Get notification settings for a user.
- [ ] `POST /user/{userId}/settings/notifications` — `postUserByUserIdSettingsNotifications`; phase 8.1 / 9.1; planned. Update notification settings for a user.
- [ ] `GET /user/{userId}/settings/password` — `getUserByUserIdSettingsPassword`; phase 8.1 / 9.1; planned. Get password page informatiom.
- [ ] `POST /user/{userId}/settings/password` — `postUserByUserIdSettingsPassword`; phase 8.1 / 9.1; planned. Update password for a user.
- [ ] `GET /user/{userId}/settings/permissions` — `getUserByUserIdSettingsPermissions`; phase 8.1 / 9.1; planned. Get permission settings for a user.
- [ ] `POST /user/{userId}/settings/permissions` — `postUserByUserIdSettingsPermissions`; phase 8.1 / 9.1; planned. Update permission settings for a user.
- [ ] `GET /user/{userId}/watch_data` — `getUserByUserIdWatchData`; phase 8.1 / 9.1; planned. Get watch data.
- [ ] `GET /user/{userId}/watchlist` — `getUserByUserIdWatchlist`; phase 8.1 / 9.1; planned. Get the Plex watchlist for a specific user.

## watchlist

- [ ] `POST /watchlist` — `postWatchlist`; phase 7.4; planned. Add media to watchlist.
- [ ] `DELETE /watchlist/{tmdbId}` — `deleteWatchlistByTmdbId`; phase 7.4; planned. Delete watchlist item.

## Outside this pin

Develop adds `/settings/{plex|jellyfin}/library/{libraryId}` and
`/settings/{plex|jellyfin}/library/sync` (four paths). These need a later contract sync.
The full v1 inventory retains library management using this release's operations.

Plex PIN endpoints and the configured image proxy/CDN are outside this OpenAPI document.
Their purposes and allowed peers are specified in auth and component specs.
