// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
// GENERATED — do not edit. Run tools/codegen/generate.sh.
package app.gauja.core.api.apis

import app.gauja.core.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import app.gauja.core.api.models.DiscordSettings
import app.gauja.core.api.models.DiscoverSlider
import app.gauja.core.api.models.GetSettingsAbout200Response
import app.gauja.core.api.models.GetSettingsCache200Response
import app.gauja.core.api.models.GetSettingsJellyfinSync200Response
import app.gauja.core.api.models.GetSettingsJellyfinUsers200ResponseInner
import app.gauja.core.api.models.GetSettingsLogs200ResponseInner
import app.gauja.core.api.models.GetSettingsNotificationsPushoverSounds200ResponseInner
import app.gauja.core.api.models.GetSettingsPlexSync200Response
import app.gauja.core.api.models.GetSettingsPlexUsers200ResponseInner
import app.gauja.core.api.models.GotifySettings
import app.gauja.core.api.models.JellyfinLibrary
import app.gauja.core.api.models.JellyfinSettings
import app.gauja.core.api.models.Job
import app.gauja.core.api.models.MainSettings
import app.gauja.core.api.models.MetadataSettings
import app.gauja.core.api.models.NetworkSettings
import app.gauja.core.api.models.NotificationEmailSettings
import app.gauja.core.api.models.NtfySettings
import app.gauja.core.api.models.PlexDevice
import app.gauja.core.api.models.PlexLibrary
import app.gauja.core.api.models.PlexSettings
import app.gauja.core.api.models.PostSettingsDiscoverAddRequest
import app.gauja.core.api.models.PostSettingsJellyfinSyncRequest
import app.gauja.core.api.models.PostSettingsJobsByJobIdScheduleRequest
import app.gauja.core.api.models.PostSettingsMetadatasTest200Response
import app.gauja.core.api.models.PostSettingsMetadatasTestRequest
import app.gauja.core.api.models.PostSettingsRadarrTest200Response
import app.gauja.core.api.models.PostSettingsRadarrTestRequest
import app.gauja.core.api.models.PostSettingsSonarrTestRequest
import app.gauja.core.api.models.PublicSettings
import app.gauja.core.api.models.PushbulletSettings
import app.gauja.core.api.models.PushoverSettings
import app.gauja.core.api.models.PutSettingsDiscoverBySliderIdRequest
import app.gauja.core.api.models.RadarrSettings
import app.gauja.core.api.models.ServiceProfile
import app.gauja.core.api.models.SlackSettings
import app.gauja.core.api.models.SonarrSettings
import app.gauja.core.api.models.TautulliSettings
import app.gauja.core.api.models.TelegramSettings
import app.gauja.core.api.models.WebPushSettings
import app.gauja.core.api.models.WebhookSettings

interface SettingsApi {
    /**
     * DELETE settings/discover/{sliderId}
     * Delete slider by ID
     * Deletes the slider with the provided sliderId. Requires the &#x60;ADMIN&#x60; permission.
     * Responses:
     *  - 200: Slider successfully deleted
     *
     * @param sliderId 
     * @return [DiscoverSlider]
     */
    @DELETE("settings/discover/{sliderId}")
    suspend fun deleteSettingsDiscoverBySliderId(@Path("sliderId") sliderId: kotlin.Double): Response<DiscoverSlider>

    /**
     * DELETE settings/radarr/{radarrId}
     * Delete Radarr instance
     * Deletes an existing Radarr instance based on the radarrId parameter.
     * Responses:
     *  - 200: Radarr instance updated
     *
     * @param radarrId Radarr instance ID
     * @return [RadarrSettings]
     */
    @DELETE("settings/radarr/{radarrId}")
    suspend fun deleteSettingsRadarrByRadarrId(@Path("radarrId") radarrId: kotlin.Int): Response<RadarrSettings>

    /**
     * DELETE settings/sonarr/{sonarrId}
     * Delete Sonarr instance
     * Deletes an existing Sonarr instance based on the sonarrId parameter.
     * Responses:
     *  - 200: Sonarr instance updated
     *
     * @param sonarrId Sonarr instance ID
     * @return [SonarrSettings]
     */
    @DELETE("settings/sonarr/{sonarrId}")
    suspend fun deleteSettingsSonarrBySonarrId(@Path("sonarrId") sonarrId: kotlin.Int): Response<SonarrSettings>

    /**
     * GET settings/about
     * Get server stats
     * Returns current server stats in a JSON object.
     * Responses:
     *  - 200: Returned about settings
     *
     * @return [GetSettingsAbout200Response]
     */
    @GET("settings/about")
    suspend fun getSettingsAbout(): Response<GetSettingsAbout200Response>

    /**
     * GET settings/cache
     * Get a list of active caches
     * Retrieves a list of all active caches and their current stats.
     * Responses:
     *  - 200: Caches returned
     *
     * @return [GetSettingsCache200Response]
     */
    @GET("settings/cache")
    suspend fun getSettingsCache(): Response<GetSettingsCache200Response>

    /**
     * GET settings/discover
     * Get all discover sliders
     * Returns all discovery sliders. Built-in and custom made.
     * Responses:
     *  - 200: Returned all discovery sliders
     *
     * @return [kotlin.collections.List<DiscoverSlider>]
     */
    @GET("settings/discover")
    suspend fun getSettingsDiscover(): Response<kotlin.collections.List<DiscoverSlider>>

    /**
     * GET settings/discover/reset
     * Reset all discover sliders
     * Resets all discovery sliders to the default values. Requires the &#x60;ADMIN&#x60; permission.
     * Responses:
     *  - 204: All sliders reset to defaults
     *
     * @return [Unit]
     */
    @GET("settings/discover/reset")
    suspend fun getSettingsDiscoverReset(): Response<Unit>

    /**
     * GET settings/jellyfin
     * Get Jellyfin settings
     * Retrieves current Jellyfin settings.
     * Responses:
     *  - 200: OK
     *
     * @return [JellyfinSettings]
     */
    @GET("settings/jellyfin")
    suspend fun getSettingsJellyfin(): Response<JellyfinSettings>

    /**
     * GET settings/jellyfin/library
     * Get Jellyfin libraries
     * Returns a list of Jellyfin libraries in a JSON array.
     * Responses:
     *  - 200: Jellyfin libraries returned
     *
     * @param sync Syncs the current libraries with the current Jellyfin server (optional)
     * @param enable Comma separated list of libraries to enable. Any libraries not passed will be disabled! (optional)
     * @return [kotlin.collections.List<JellyfinLibrary>]
     */
    @GET("settings/jellyfin/library")
    suspend fun getSettingsJellyfinLibrary(@Query("sync") sync: kotlin.String? = null, @Query("enable") enable: kotlin.String? = null): Response<kotlin.collections.List<JellyfinLibrary>>

    /**
     * GET settings/jellyfin/sync
     * Get status of full Jellyfin library sync
     * Returns sync progress in a JSON array.
     * Responses:
     *  - 200: Status of Jellyfin sync
     *
     * @return [GetSettingsJellyfinSync200Response]
     */
    @GET("settings/jellyfin/sync")
    suspend fun getSettingsJellyfinSync(): Response<GetSettingsJellyfinSync200Response>

    /**
     * GET settings/jellyfin/users
     * Get Jellyfin Users
     * Returns a list of Jellyfin Users in a JSON array.
     * Responses:
     *  - 200: Jellyfin users returned
     *
     * @return [kotlin.collections.List<GetSettingsJellyfinUsers200ResponseInner>]
     */
    @GET("settings/jellyfin/users")
    suspend fun getSettingsJellyfinUsers(): Response<kotlin.collections.List<GetSettingsJellyfinUsers200ResponseInner>>

    /**
     * GET settings/jobs
     * Get scheduled jobs
     * Returns list of all scheduled jobs and details about their next execution time in a JSON array.
     * Responses:
     *  - 200: Scheduled jobs returned
     *
     * @return [kotlin.collections.List<Job>]
     */
    @GET("settings/jobs")
    suspend fun getSettingsJobs(): Response<kotlin.collections.List<Job>>

    /**
     * GET settings/logs
     * Returns logs
     * Returns list of all log items and details
     * Responses:
     *  - 200: Server log returned
     *
     * @param take  (optional)
     * @param skip  (optional)
     * @param filter  (optional, default to "debug")
     * @param search  (optional)
     * @return [kotlin.collections.List<GetSettingsLogs200ResponseInner>]
     */
    @GET("settings/logs")
    suspend fun getSettingsLogs(@Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null, @Query("filter") filter: kotlin.String? = "debug", @Query("search") search: kotlin.String? = null): Response<kotlin.collections.List<GetSettingsLogs200ResponseInner>>

    /**
     * GET settings/main
     * Get main settings
     * Retrieves all main settings in a JSON object.
     * Responses:
     *  - 200: OK
     *
     * @return [MainSettings]
     */
    @GET("settings/main")
    suspend fun getSettingsMain(): Response<MainSettings>

    /**
     * GET settings/metadatas
     * Get Metadata settings
     * Retrieves current Metadata settings.
     * Responses:
     *  - 200: OK
     *
     * @return [MetadataSettings]
     */
    @GET("settings/metadatas")
    suspend fun getSettingsMetadatas(): Response<MetadataSettings>

    /**
     * GET settings/network
     * Get network settings
     * Retrieves all network settings in a JSON object.
     * Responses:
     *  - 200: OK
     *
     * @return [MainSettings]
     */
    @GET("settings/network")
    suspend fun getSettingsNetwork(): Response<MainSettings>

    /**
     * GET settings/notifications/discord
     * Get Discord notification settings
     * Returns current Discord notification settings in a JSON object.
     * Responses:
     *  - 200: Returned Discord settings
     *
     * @return [DiscordSettings]
     */
    @GET("settings/notifications/discord")
    suspend fun getSettingsNotificationsDiscord(): Response<DiscordSettings>

    /**
     * GET settings/notifications/email
     * Get email notification settings
     * Returns current email notification settings in a JSON object.
     * Responses:
     *  - 200: Returned email settings
     *
     * @return [NotificationEmailSettings]
     */
    @GET("settings/notifications/email")
    suspend fun getSettingsNotificationsEmail(): Response<NotificationEmailSettings>

    /**
     * GET settings/notifications/gotify
     * Get Gotify notification settings
     * Returns current Gotify notification settings in a JSON object.
     * Responses:
     *  - 200: Returned Gotify settings
     *
     * @return [GotifySettings]
     */
    @GET("settings/notifications/gotify")
    suspend fun getSettingsNotificationsGotify(): Response<GotifySettings>

    /**
     * GET settings/notifications/ntfy
     * Get ntfy.sh notification settings
     * Returns current ntfy.sh notification settings in a JSON object.
     * Responses:
     *  - 200: Returned ntfy.sh settings
     *
     * @return [NtfySettings]
     */
    @GET("settings/notifications/ntfy")
    suspend fun getSettingsNotificationsNtfy(): Response<NtfySettings>

    /**
     * GET settings/notifications/pushbullet
     * Get Pushbullet notification settings
     * Returns current Pushbullet notification settings in a JSON object.
     * Responses:
     *  - 200: Returned Pushbullet settings
     *
     * @return [PushbulletSettings]
     */
    @GET("settings/notifications/pushbullet")
    suspend fun getSettingsNotificationsPushbullet(): Response<PushbulletSettings>

    /**
     * GET settings/notifications/pushover
     * Get Pushover notification settings
     * Returns current Pushover notification settings in a JSON object.
     * Responses:
     *  - 200: Returned Pushover settings
     *
     * @return [PushoverSettings]
     */
    @GET("settings/notifications/pushover")
    suspend fun getSettingsNotificationsPushover(): Response<PushoverSettings>

    /**
     * GET settings/notifications/pushover/sounds
     * Get Pushover sounds
     * Returns valid Pushover sound options in a JSON array.
     * Responses:
     *  - 200: Returned Pushover settings
     *
     * @param token 
     * @return [kotlin.collections.List<GetSettingsNotificationsPushoverSounds200ResponseInner>]
     */
    @GET("settings/notifications/pushover/sounds")
    suspend fun getSettingsNotificationsPushoverSounds(@Query("token") token: kotlin.String): Response<kotlin.collections.List<GetSettingsNotificationsPushoverSounds200ResponseInner>>

    /**
     * GET settings/notifications/slack
     * Get Slack notification settings
     * Returns current Slack notification settings in a JSON object.
     * Responses:
     *  - 200: Returned slack settings
     *
     * @return [SlackSettings]
     */
    @GET("settings/notifications/slack")
    suspend fun getSettingsNotificationsSlack(): Response<SlackSettings>

    /**
     * GET settings/notifications/telegram
     * Get Telegram notification settings
     * Returns current Telegram notification settings in a JSON object.
     * Responses:
     *  - 200: Returned Telegram settings
     *
     * @return [TelegramSettings]
     */
    @GET("settings/notifications/telegram")
    suspend fun getSettingsNotificationsTelegram(): Response<TelegramSettings>

    /**
     * GET settings/notifications/webhook
     * Get webhook notification settings
     * Returns current webhook notification settings in a JSON object.
     * Responses:
     *  - 200: Returned webhook settings
     *
     * @return [WebhookSettings]
     */
    @GET("settings/notifications/webhook")
    suspend fun getSettingsNotificationsWebhook(): Response<WebhookSettings>

    /**
     * GET settings/notifications/webpush
     * Get Web Push notification settings
     * Returns current Web Push notification settings in a JSON object.
     * Responses:
     *  - 200: Returned web push settings
     *
     * @return [WebPushSettings]
     */
    @GET("settings/notifications/webpush")
    suspend fun getSettingsNotificationsWebpush(): Response<WebPushSettings>

    /**
     * GET settings/plex
     * Get Plex settings
     * Retrieves current Plex settings.
     * Responses:
     *  - 200: OK
     *
     * @return [PlexSettings]
     */
    @GET("settings/plex")
    suspend fun getSettingsPlex(): Response<PlexSettings>

    /**
     * GET settings/plex/devices/servers
     * Gets the user&#39;s available Plex servers
     * Returns a list of available Plex servers and their connectivity state
     * Responses:
     *  - 200: OK
     *
     * @return [kotlin.collections.List<PlexDevice>]
     */
    @GET("settings/plex/devices/servers")
    suspend fun getSettingsPlexDevicesServers(): Response<kotlin.collections.List<PlexDevice>>

    /**
     * GET settings/plex/library
     * Get Plex libraries
     * Returns a list of Plex libraries in a JSON array.
     * Responses:
     *  - 200: Plex libraries returned
     *
     * @param sync Syncs the current libraries with the current Plex server (optional)
     * @param enable Comma separated list of libraries to enable. Any libraries not passed will be disabled! (optional)
     * @return [kotlin.collections.List<PlexLibrary>]
     */
    @GET("settings/plex/library")
    suspend fun getSettingsPlexLibrary(@Query("sync") sync: kotlin.String? = null, @Query("enable") enable: kotlin.String? = null): Response<kotlin.collections.List<PlexLibrary>>

    /**
     * GET settings/plex/sync
     * Get status of full Plex library scan
     * Returns scan progress in a JSON array.
     * Responses:
     *  - 200: Status of Plex scan
     *
     * @return [GetSettingsPlexSync200Response]
     */
    @GET("settings/plex/sync")
    suspend fun getSettingsPlexSync(): Response<GetSettingsPlexSync200Response>

    /**
     * GET settings/plex/users
     * Get Plex users
     * Returns a list of Plex users in a JSON array.  Requires the &#x60;MANAGE_USERS&#x60; permission. 
     * Responses:
     *  - 200: Plex users
     *
     * @return [kotlin.collections.List<GetSettingsPlexUsers200ResponseInner>]
     */
    @GET("settings/plex/users")
    suspend fun getSettingsPlexUsers(): Response<kotlin.collections.List<GetSettingsPlexUsers200ResponseInner>>

    /**
     * GET settings/public
     * Get public settings
     * Returns settings that are not protected or sensitive. Mainly used to determine if the application has been configured for the first time.
     * Responses:
     *  - 200: Public settings returned
     *
     * @return [PublicSettings]
     */
    @GET("settings/public")
    suspend fun getSettingsPublic(): Response<PublicSettings>

    /**
     * GET settings/radarr
     * Get Radarr settings
     * Returns all Radarr settings in a JSON array.
     * Responses:
     *  - 200: Values were returned
     *
     * @return [kotlin.collections.List<RadarrSettings>]
     */
    @GET("settings/radarr")
    suspend fun getSettingsRadarr(): Response<kotlin.collections.List<RadarrSettings>>

    /**
     * GET settings/radarr/{radarrId}/profiles
     * Get available Radarr profiles
     * Returns a list of profiles available on the Radarr server instance in a JSON array.
     * Responses:
     *  - 200: Returned list of profiles
     *
     * @param radarrId Radarr instance ID
     * @return [kotlin.collections.List<ServiceProfile>]
     */
    @GET("settings/radarr/{radarrId}/profiles")
    suspend fun getSettingsRadarrByRadarrIdProfiles(@Path("radarrId") radarrId: kotlin.Int): Response<kotlin.collections.List<ServiceProfile>>

    /**
     * GET settings/sonarr
     * Get Sonarr settings
     * Returns all Sonarr settings in a JSON array.
     * Responses:
     *  - 200: Values were returned
     *
     * @return [kotlin.collections.List<SonarrSettings>]
     */
    @GET("settings/sonarr")
    suspend fun getSettingsSonarr(): Response<kotlin.collections.List<SonarrSettings>>

    /**
     * GET settings/tautulli
     * Get Tautulli settings
     * Retrieves current Tautulli settings.
     * Responses:
     *  - 200: OK
     *
     * @return [TautulliSettings]
     */
    @GET("settings/tautulli")
    suspend fun getSettingsTautulli(): Response<TautulliSettings>

    /**
     * POST settings/cache/{cacheId}/flush
     * Flush a specific cache
     * Flushes all data from the cache ID provided
     * Responses:
     *  - 204: Flushed cache
     *
     * @param cacheId 
     * @return [Unit]
     */
    @POST("settings/cache/{cacheId}/flush")
    suspend fun postSettingsCacheByCacheIdFlush(@Path("cacheId") cacheId: kotlin.String): Response<Unit>

    /**
     * POST settings/cache/dns/{dnsEntry}/flush
     * Flush a specific DNS cache entry
     * Flushes a specific DNS cache entry
     * Responses:
     *  - 204: Flushed dns cache
     *
     * @param dnsEntry 
     * @return [Unit]
     */
    @POST("settings/cache/dns/{dnsEntry}/flush")
    suspend fun postSettingsCacheDnsByDnsEntryFlush(@Path("dnsEntry") dnsEntry: kotlin.String): Response<Unit>

    /**
     * POST settings/discover
     * Batch update all sliders.
     * Batch update all sliders at once. Should also be used for creation. Will only update sliders provided and will not delete any sliders not present in the request. If a slider is missing a required field, it will be ignored. Requires the &#x60;ADMIN&#x60; permission. 
     * Responses:
     *  - 200: Returned all newly updated discovery sliders
     *
     * @param discoverSlider 
     * @return [kotlin.collections.List<DiscoverSlider>]
     */
    @POST("settings/discover")
    suspend fun postSettingsDiscover(@Body discoverSlider: kotlin.collections.List<DiscoverSlider>): Response<kotlin.collections.List<DiscoverSlider>>

    /**
     * POST settings/discover/add
     * Add a new slider
     * Add a single slider and return the newly created slider. Requires the &#x60;ADMIN&#x60; permission. 
     * Responses:
     *  - 200: Returns newly added discovery slider
     *
     * @param postSettingsDiscoverAddRequest 
     * @return [DiscoverSlider]
     */
    @POST("settings/discover/add")
    suspend fun postSettingsDiscoverAdd(@Body postSettingsDiscoverAddRequest: PostSettingsDiscoverAddRequest): Response<DiscoverSlider>

    /**
     * POST settings/initialize
     * Initialize application
     * Sets the app as initialized, allowing the user to navigate to pages other than the setup page.
     * Responses:
     *  - 200: Public settings returned
     *
     * @return [PublicSettings]
     */
    @POST("settings/initialize")
    suspend fun postSettingsInitialize(): Response<PublicSettings>

    /**
     * POST settings/jellyfin
     * Update Jellyfin settings
     * Updates Jellyfin settings with the provided values.
     * Responses:
     *  - 200: Values were successfully updated
     *
     * @param jellyfinSettings 
     * @return [JellyfinSettings]
     */
    @POST("settings/jellyfin")
    suspend fun postSettingsJellyfin(@Body jellyfinSettings: JellyfinSettings): Response<JellyfinSettings>

    /**
     * POST settings/jellyfin/sync
     * Start full Jellyfin library sync
     * Runs a full Jellyfin library sync and returns the progress in a JSON array.
     * Responses:
     *  - 200: Status of Jellyfin sync
     *
     * @param postSettingsJellyfinSyncRequest  (optional)
     * @return [GetSettingsJellyfinSync200Response]
     */
    @POST("settings/jellyfin/sync")
    suspend fun postSettingsJellyfinSync(@Body postSettingsJellyfinSyncRequest: PostSettingsJellyfinSyncRequest? = null): Response<GetSettingsJellyfinSync200Response>

    /**
     * POST settings/jobs/{jobId}/cancel
     * Cancel a specific job
     * Cancels a specific job. Will return the new job status in JSON format.
     * Responses:
     *  - 200: Canceled job returned
     *
     * @param jobId 
     * @return [Job]
     */
    @POST("settings/jobs/{jobId}/cancel")
    suspend fun postSettingsJobsByJobIdCancel(@Path("jobId") jobId: kotlin.String): Response<Job>

    /**
     * POST settings/jobs/{jobId}/run
     * Invoke a specific job
     * Invokes a specific job to run. Will return the new job status in JSON format.
     * Responses:
     *  - 200: Invoked job returned
     *
     * @param jobId 
     * @return [Job]
     */
    @POST("settings/jobs/{jobId}/run")
    suspend fun postSettingsJobsByJobIdRun(@Path("jobId") jobId: kotlin.String): Response<Job>

    /**
     * POST settings/jobs/{jobId}/schedule
     * Modify job schedule
     * Re-registers the job with the schedule specified. Will return the job in JSON format.
     * Responses:
     *  - 200: Rescheduled job
     *
     * @param jobId 
     * @param postSettingsJobsByJobIdScheduleRequest 
     * @return [Job]
     */
    @POST("settings/jobs/{jobId}/schedule")
    suspend fun postSettingsJobsByJobIdSchedule(@Path("jobId") jobId: kotlin.String, @Body postSettingsJobsByJobIdScheduleRequest: PostSettingsJobsByJobIdScheduleRequest): Response<Job>

    /**
     * POST settings/main
     * Update main settings
     * Updates main settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param mainSettings 
     * @return [MainSettings]
     */
    @POST("settings/main")
    suspend fun postSettingsMain(@Body mainSettings: MainSettings): Response<MainSettings>

    /**
     * POST settings/main/regenerate
     * Get main settings with newly-generated API key
     * Returns main settings in a JSON object, using the new API key.
     * Responses:
     *  - 200: OK
     *
     * @return [MainSettings]
     */
    @POST("settings/main/regenerate")
    suspend fun postSettingsMainRegenerate(): Response<MainSettings>

    /**
     * POST settings/metadatas/test
     * Test Provider configuration
     * Tests if the TVDB configuration is valid. Returns a list of available languages on success.
     * Responses:
     *  - 200: Succesfully connected to TVDB
     *
     * @param postSettingsMetadatasTestRequest 
     * @return [PostSettingsMetadatasTest200Response]
     */
    @POST("settings/metadatas/test")
    suspend fun postSettingsMetadatasTest(@Body postSettingsMetadatasTestRequest: PostSettingsMetadatasTestRequest): Response<PostSettingsMetadatasTest200Response>

    /**
     * POST settings/network
     * Update network settings
     * Updates network settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param networkSettings 
     * @return [NetworkSettings]
     */
    @POST("settings/network")
    suspend fun postSettingsNetwork(@Body networkSettings: NetworkSettings): Response<NetworkSettings>

    /**
     * POST settings/notifications/discord
     * Update Discord notification settings
     * Updates Discord notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param discordSettings 
     * @return [DiscordSettings]
     */
    @POST("settings/notifications/discord")
    suspend fun postSettingsNotificationsDiscord(@Body discordSettings: DiscordSettings): Response<DiscordSettings>

    /**
     * POST settings/notifications/discord/test
     * Test Discord settings
     * Sends a test notification to the Discord agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param discordSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/discord/test")
    suspend fun postSettingsNotificationsDiscordTest(@Body discordSettings: DiscordSettings): Response<Unit>

    /**
     * POST settings/notifications/email
     * Update email notification settings
     * Updates email notification settings with provided values
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param notificationEmailSettings 
     * @return [NotificationEmailSettings]
     */
    @POST("settings/notifications/email")
    suspend fun postSettingsNotificationsEmail(@Body notificationEmailSettings: NotificationEmailSettings): Response<NotificationEmailSettings>

    /**
     * POST settings/notifications/email/test
     * Test email settings
     * Sends a test notification to the email agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param notificationEmailSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/email/test")
    suspend fun postSettingsNotificationsEmailTest(@Body notificationEmailSettings: NotificationEmailSettings): Response<Unit>

    /**
     * POST settings/notifications/gotify
     * Update Gotify notification settings
     * Update Gotify notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param gotifySettings 
     * @return [GotifySettings]
     */
    @POST("settings/notifications/gotify")
    suspend fun postSettingsNotificationsGotify(@Body gotifySettings: GotifySettings): Response<GotifySettings>

    /**
     * POST settings/notifications/gotify/test
     * Test Gotify settings
     * Sends a test notification to the Gotify agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param gotifySettings 
     * @return [Unit]
     */
    @POST("settings/notifications/gotify/test")
    suspend fun postSettingsNotificationsGotifyTest(@Body gotifySettings: GotifySettings): Response<Unit>

    /**
     * POST settings/notifications/ntfy
     * Update ntfy.sh notification settings
     * Update ntfy.sh notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param ntfySettings 
     * @return [NtfySettings]
     */
    @POST("settings/notifications/ntfy")
    suspend fun postSettingsNotificationsNtfy(@Body ntfySettings: NtfySettings): Response<NtfySettings>

    /**
     * POST settings/notifications/ntfy/test
     * Test ntfy.sh settings
     * Sends a test notification to the ntfy.sh agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param ntfySettings 
     * @return [Unit]
     */
    @POST("settings/notifications/ntfy/test")
    suspend fun postSettingsNotificationsNtfyTest(@Body ntfySettings: NtfySettings): Response<Unit>

    /**
     * POST settings/notifications/pushbullet
     * Update Pushbullet notification settings
     * Update Pushbullet notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param pushbulletSettings 
     * @return [PushbulletSettings]
     */
    @POST("settings/notifications/pushbullet")
    suspend fun postSettingsNotificationsPushbullet(@Body pushbulletSettings: PushbulletSettings): Response<PushbulletSettings>

    /**
     * POST settings/notifications/pushbullet/test
     * Test Pushbullet settings
     * Sends a test notification to the Pushbullet agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param pushbulletSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/pushbullet/test")
    suspend fun postSettingsNotificationsPushbulletTest(@Body pushbulletSettings: PushbulletSettings): Response<Unit>

    /**
     * POST settings/notifications/pushover
     * Update Pushover notification settings
     * Update Pushover notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param pushoverSettings 
     * @return [PushoverSettings]
     */
    @POST("settings/notifications/pushover")
    suspend fun postSettingsNotificationsPushover(@Body pushoverSettings: PushoverSettings): Response<PushoverSettings>

    /**
     * POST settings/notifications/pushover/test
     * Test Pushover settings
     * Sends a test notification to the Pushover agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param pushoverSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/pushover/test")
    suspend fun postSettingsNotificationsPushoverTest(@Body pushoverSettings: PushoverSettings): Response<Unit>

    /**
     * POST settings/notifications/slack
     * Update Slack notification settings
     * Updates Slack notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param slackSettings 
     * @return [SlackSettings]
     */
    @POST("settings/notifications/slack")
    suspend fun postSettingsNotificationsSlack(@Body slackSettings: SlackSettings): Response<SlackSettings>

    /**
     * POST settings/notifications/slack/test
     * Test Slack settings
     * Sends a test notification to the Slack agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param slackSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/slack/test")
    suspend fun postSettingsNotificationsSlackTest(@Body slackSettings: SlackSettings): Response<Unit>

    /**
     * POST settings/notifications/telegram
     * Update Telegram notification settings
     * Update Telegram notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param telegramSettings 
     * @return [TelegramSettings]
     */
    @POST("settings/notifications/telegram")
    suspend fun postSettingsNotificationsTelegram(@Body telegramSettings: TelegramSettings): Response<TelegramSettings>

    /**
     * POST settings/notifications/telegram/test
     * Test Telegram settings
     * Sends a test notification to the Telegram agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param telegramSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/telegram/test")
    suspend fun postSettingsNotificationsTelegramTest(@Body telegramSettings: TelegramSettings): Response<Unit>

    /**
     * POST settings/notifications/webhook
     * Update webhook notification settings
     * Updates webhook notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param webhookSettings 
     * @return [WebhookSettings]
     */
    @POST("settings/notifications/webhook")
    suspend fun postSettingsNotificationsWebhook(@Body webhookSettings: WebhookSettings): Response<WebhookSettings>

    /**
     * POST settings/notifications/webhook/test
     * Test webhook settings
     * Sends a test notification to the webhook agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param webhookSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/webhook/test")
    suspend fun postSettingsNotificationsWebhookTest(@Body webhookSettings: WebhookSettings): Response<Unit>

    /**
     * POST settings/notifications/webpush
     * Update Web Push notification settings
     * Updates Web Push notification settings with the provided values.
     * Responses:
     *  - 200: Values were sucessfully updated
     *
     * @param webPushSettings 
     * @return [WebPushSettings]
     */
    @POST("settings/notifications/webpush")
    suspend fun postSettingsNotificationsWebpush(@Body webPushSettings: WebPushSettings): Response<WebPushSettings>

    /**
     * POST settings/notifications/webpush/test
     * Test Web Push settings
     * Sends a test notification to the Web Push agent.
     * Responses:
     *  - 204: Test notification attempted
     *
     * @param webPushSettings 
     * @return [Unit]
     */
    @POST("settings/notifications/webpush/test")
    suspend fun postSettingsNotificationsWebpushTest(@Body webPushSettings: WebPushSettings): Response<Unit>

    /**
     * POST settings/plex
     * Update Plex settings
     * Updates Plex settings with the provided values.
     * Responses:
     *  - 200: Values were successfully updated
     *
     * @param plexSettings 
     * @return [PlexSettings]
     */
    @POST("settings/plex")
    suspend fun postSettingsPlex(@Body plexSettings: PlexSettings): Response<PlexSettings>

    /**
     * POST settings/plex/sync
     * Start full Plex library scan
     * Runs a full Plex library scan and returns the progress in a JSON array.
     * Responses:
     *  - 200: Status of Plex scan
     *
     * @param postSettingsJellyfinSyncRequest  (optional)
     * @return [GetSettingsPlexSync200Response]
     */
    @POST("settings/plex/sync")
    suspend fun postSettingsPlexSync(@Body postSettingsJellyfinSyncRequest: PostSettingsJellyfinSyncRequest? = null): Response<GetSettingsPlexSync200Response>

    /**
     * POST settings/radarr
     * Create Radarr instance
     * Creates a new Radarr instance from the request body.
     * Responses:
     *  - 201: New Radarr instance created
     *
     * @param radarrSettings 
     * @return [RadarrSettings]
     */
    @POST("settings/radarr")
    suspend fun postSettingsRadarr(@Body radarrSettings: RadarrSettings): Response<RadarrSettings>

    /**
     * POST settings/radarr/test
     * Test Radarr configuration
     * Tests if the Radarr configuration is valid. Returns profiles and root folders on success.
     * Responses:
     *  - 200: Succesfully connected to Radarr instance
     *
     * @param postSettingsRadarrTestRequest 
     * @return [PostSettingsRadarrTest200Response]
     */
    @POST("settings/radarr/test")
    suspend fun postSettingsRadarrTest(@Body postSettingsRadarrTestRequest: PostSettingsRadarrTestRequest): Response<PostSettingsRadarrTest200Response>

    /**
     * POST settings/sonarr
     * Create Sonarr instance
     * Creates a new Sonarr instance from the request body.
     * Responses:
     *  - 201: New Sonarr instance created
     *
     * @param sonarrSettings 
     * @return [SonarrSettings]
     */
    @POST("settings/sonarr")
    suspend fun postSettingsSonarr(@Body sonarrSettings: SonarrSettings): Response<SonarrSettings>

    /**
     * POST settings/sonarr/test
     * Test Sonarr configuration
     * Tests if the Sonarr configuration is valid. Returns profiles and root folders on success.
     * Responses:
     *  - 200: Succesfully connected to Sonarr instance
     *
     * @param postSettingsSonarrTestRequest 
     * @return [PostSettingsRadarrTest200Response]
     */
    @POST("settings/sonarr/test")
    suspend fun postSettingsSonarrTest(@Body postSettingsSonarrTestRequest: PostSettingsSonarrTestRequest): Response<PostSettingsRadarrTest200Response>

    /**
     * POST settings/tautulli
     * Update Tautulli settings
     * Updates Tautulli settings with the provided values.
     * Responses:
     *  - 200: Values were successfully updated
     *
     * @param tautulliSettings 
     * @return [TautulliSettings]
     */
    @POST("settings/tautulli")
    suspend fun postSettingsTautulli(@Body tautulliSettings: TautulliSettings): Response<TautulliSettings>

    /**
     * PUT settings/discover/{sliderId}
     * Update a single slider
     * Updates a single slider and return the newly updated slider. Requires the &#x60;ADMIN&#x60; permission. 
     * Responses:
     *  - 200: Returns newly added discovery slider
     *
     * @param sliderId 
     * @param putSettingsDiscoverBySliderIdRequest 
     * @return [DiscoverSlider]
     */
    @PUT("settings/discover/{sliderId}")
    suspend fun putSettingsDiscoverBySliderId(@Path("sliderId") sliderId: kotlin.Double, @Body putSettingsDiscoverBySliderIdRequest: PutSettingsDiscoverBySliderIdRequest): Response<DiscoverSlider>

    /**
     * PUT settings/metadatas
     * Update Metadata settings
     * Updates Metadata settings with the provided values.
     * Responses:
     *  - 200: Values were successfully updated
     *
     * @param metadataSettings 
     * @return [MetadataSettings]
     */
    @PUT("settings/metadatas")
    suspend fun putSettingsMetadatas(@Body metadataSettings: MetadataSettings): Response<MetadataSettings>

    /**
     * PUT settings/radarr/{radarrId}
     * Update Radarr instance
     * Updates an existing Radarr instance with the provided values.
     * Responses:
     *  - 200: Radarr instance updated
     *
     * @param radarrId Radarr instance ID
     * @param radarrSettings 
     * @return [RadarrSettings]
     */
    @PUT("settings/radarr/{radarrId}")
    suspend fun putSettingsRadarrByRadarrId(@Path("radarrId") radarrId: kotlin.Int, @Body radarrSettings: RadarrSettings): Response<RadarrSettings>

    /**
     * PUT settings/sonarr/{sonarrId}
     * Update Sonarr instance
     * Updates an existing Sonarr instance with the provided values.
     * Responses:
     *  - 200: Sonarr instance updated
     *
     * @param sonarrId Sonarr instance ID
     * @param sonarrSettings 
     * @return [SonarrSettings]
     */
    @PUT("settings/sonarr/{sonarrId}")
    suspend fun putSettingsSonarrBySonarrId(@Path("sonarrId") sonarrId: kotlin.Int, @Body sonarrSettings: SonarrSettings): Response<SonarrSettings>

}
