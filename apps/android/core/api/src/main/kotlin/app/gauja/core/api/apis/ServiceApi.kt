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

import app.gauja.core.api.models.GetServiceRadarrByRadarrId200Response
import app.gauja.core.api.models.GetServiceSonarrBySonarrId200Response
import app.gauja.core.api.models.RadarrSettings
import app.gauja.core.api.models.SonarrSeries
import app.gauja.core.api.models.SonarrSettings

interface ServiceApi {
    /**
     * GET service/radarr
     * Get non-sensitive Radarr server list
     * Returns a list of Radarr server IDs and names in a JSON object.
     * Responses:
     *  - 200: Request successful
     *
     * @return [kotlin.collections.List<RadarrSettings>]
     */
    @GET("service/radarr")
    suspend fun getServiceRadarr(): Response<kotlin.collections.List<RadarrSettings>>

    /**
     * GET service/radarr/{radarrId}
     * Get Radarr server quality profiles and root folders
     * Returns a Radarr server&#39;s quality profile and root folder details in a JSON object.
     * Responses:
     *  - 200: Request successful
     *
     * @param radarrId 
     * @return [GetServiceRadarrByRadarrId200Response]
     */
    @GET("service/radarr/{radarrId}")
    suspend fun getServiceRadarrByRadarrId(@Path("radarrId") radarrId: kotlin.Double): Response<GetServiceRadarrByRadarrId200Response>

    /**
     * GET service/sonarr
     * Get non-sensitive Sonarr server list
     * Returns a list of Sonarr server IDs and names in a JSON object.
     * Responses:
     *  - 200: Request successful
     *
     * @return [kotlin.collections.List<SonarrSettings>]
     */
    @GET("service/sonarr")
    suspend fun getServiceSonarr(): Response<kotlin.collections.List<SonarrSettings>>

    /**
     * GET service/sonarr/{sonarrId}
     * Get Sonarr server quality profiles and root folders
     * Returns a Sonarr server&#39;s quality profile and root folder details in a JSON object.
     * Responses:
     *  - 200: Request successful
     *
     * @param sonarrId 
     * @return [GetServiceSonarrBySonarrId200Response]
     */
    @GET("service/sonarr/{sonarrId}")
    suspend fun getServiceSonarrBySonarrId(@Path("sonarrId") sonarrId: kotlin.Double): Response<GetServiceSonarrBySonarrId200Response>

    /**
     * GET service/sonarr/lookup/{tmdbId}
     * Get series from Sonarr
     * Returns a list of series returned by searching for the name in Sonarr.
     * Responses:
     *  - 200: Request successful
     *
     * @param tmdbId 
     * @return [kotlin.collections.List<SonarrSeries>]
     */
    @GET("service/sonarr/lookup/{tmdbId}")
    suspend fun getServiceSonarrLookupByTmdbId(@Path("tmdbId") tmdbId: kotlin.Double): Response<kotlin.collections.List<SonarrSeries>>

}
