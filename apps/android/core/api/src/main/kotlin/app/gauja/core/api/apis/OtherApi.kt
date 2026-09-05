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

import app.gauja.core.api.models.CertificationResponse
import app.gauja.core.api.models.GetCertificationsMovie500Response
import app.gauja.core.api.models.GetCertificationsTv500Response
import app.gauja.core.api.models.GetKeywordByKeywordId500Response
import app.gauja.core.api.models.Keyword
import app.gauja.core.api.models.WatchProviderDetails
import app.gauja.core.api.models.WatchProviderRegion

interface OtherApi {
    /**
     * GET certifications/movie
     * Get movie certifications
     * Returns list of movie certifications from TMDB.
     * Responses:
     *  - 200: Movie certifications returned
     *  - 500: Unable to retrieve movie certifications
     *
     * @return [CertificationResponse]
     */
    @GET("certifications/movie")
    suspend fun getCertificationsMovie(): Response<CertificationResponse>

    /**
     * GET certifications/tv
     * Get TV certifications
     * Returns list of TV show certifications from TMDB.
     * Responses:
     *  - 200: TV certifications returned
     *  - 500: Unable to retrieve TV certifications
     *
     * @return [CertificationResponse]
     */
    @GET("certifications/tv")
    suspend fun getCertificationsTv(): Response<CertificationResponse>

    /**
     * GET keyword/{keywordId}
     * Get keyword
     * Returns a single keyword in JSON format. 
     * Responses:
     *  - 200: Keyword returned (null if not found)
     *  - 500: Internal server error
     *
     * @param keywordId 
     * @return [Keyword]
     */
    @GET("keyword/{keywordId}")
    suspend fun getKeywordByKeywordId(@Path("keywordId") keywordId: kotlin.Double): Response<Keyword>

    /**
     * GET watchproviders/movies
     * Get watch provider movies
     * Returns a list of all available watch providers for movies. 
     * Responses:
     *  - 200: Watch providers for movies returned
     *
     * @param watchRegion 
     * @return [kotlin.collections.List<WatchProviderDetails>]
     */
    @GET("watchproviders/movies")
    suspend fun getWatchprovidersMovies(@Query("watchRegion") watchRegion: kotlin.String): Response<kotlin.collections.List<WatchProviderDetails>>

    /**
     * GET watchproviders/regions
     * Get watch provider regions
     * Returns a list of all available watch provider regions. 
     * Responses:
     *  - 200: Watch provider regions returned
     *
     * @return [kotlin.collections.List<WatchProviderRegion>]
     */
    @GET("watchproviders/regions")
    suspend fun getWatchprovidersRegions(): Response<kotlin.collections.List<WatchProviderRegion>>

    /**
     * GET watchproviders/tv
     * Get watch provider series
     * Returns a list of all available watch providers for series. 
     * Responses:
     *  - 200: Watch providers for series returned
     *
     * @param watchRegion 
     * @return [kotlin.collections.List<WatchProviderDetails>]
     */
    @GET("watchproviders/tv")
    suspend fun getWatchprovidersTv(@Query("watchRegion") watchRegion: kotlin.String): Response<kotlin.collections.List<WatchProviderDetails>>

}
