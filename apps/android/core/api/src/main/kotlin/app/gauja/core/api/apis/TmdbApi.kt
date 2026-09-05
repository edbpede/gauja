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

import app.gauja.core.api.models.GetGenresMovie200ResponseInner
import app.gauja.core.api.models.GetGenresTv200ResponseInner
import app.gauja.core.api.models.GetLanguages200ResponseInner
import app.gauja.core.api.models.GetRegions200ResponseInner
import app.gauja.core.api.models.ProductionCompany

interface TmdbApi {
    /**
     * GET backdrops
     * Get backdrops of trending items
     * Returns a list of backdrop image paths in a JSON array.
     * Responses:
     *  - 200: Results
     *
     * @return [kotlin.collections.List<kotlin.String>]
     */
    @GET("backdrops")
    suspend fun getBackdrops(): Response<kotlin.collections.List<kotlin.String>>

    /**
     * GET genres/movie
     * Get list of official TMDB movie genres
     * Returns a list of genres in a JSON array.
     * Responses:
     *  - 200: Results
     *
     * @param language  (optional)
     * @return [kotlin.collections.List<GetGenresMovie200ResponseInner>]
     */
    @GET("genres/movie")
    suspend fun getGenresMovie(@Query("language") language: kotlin.String? = null): Response<kotlin.collections.List<GetGenresMovie200ResponseInner>>

    /**
     * GET genres/tv
     * Get list of official TMDB movie genres
     * Returns a list of genres in a JSON array.
     * Responses:
     *  - 200: Results
     *
     * @param language  (optional)
     * @return [kotlin.collections.List<GetGenresTv200ResponseInner>]
     */
    @GET("genres/tv")
    suspend fun getGenresTv(@Query("language") language: kotlin.String? = null): Response<kotlin.collections.List<GetGenresTv200ResponseInner>>

    /**
     * GET languages
     * Languages supported by TMDB
     * Returns a list of languages in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @return [kotlin.collections.List<GetLanguages200ResponseInner>]
     */
    @GET("languages")
    suspend fun getLanguages(): Response<kotlin.collections.List<GetLanguages200ResponseInner>>

    /**
     * GET network/{networkId}
     * Get TV network details
     * Returns TV network details in a JSON object.
     * Responses:
     *  - 200: TV network details
     *
     * @param networkId 
     * @return [ProductionCompany]
     */
    @GET("network/{networkId}")
    suspend fun getNetworkByNetworkId(@Path("networkId") networkId: kotlin.Double): Response<ProductionCompany>

    /**
     * GET regions
     * Regions supported by TMDB
     * Returns a list of regions in a JSON object.
     * Responses:
     *  - 200: Results
     *
     * @return [kotlin.collections.List<GetRegions200ResponseInner>]
     */
    @GET("regions")
    suspend fun getRegions(): Response<kotlin.collections.List<GetRegions200ResponseInner>>

    /**
     * GET studio/{studioId}
     * Get movie studio details
     * Returns movie studio details in a JSON object.
     * Responses:
     *  - 200: Movie studio details
     *
     * @param studioId 
     * @return [ProductionCompany]
     */
    @GET("studio/{studioId}")
    suspend fun getStudioByStudioId(@Path("studioId") studioId: kotlin.Double): Response<ProductionCompany>

}
