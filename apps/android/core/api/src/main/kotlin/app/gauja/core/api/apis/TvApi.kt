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

import app.gauja.core.api.models.GetDiscoverTv200Response
import app.gauja.core.api.models.GetTvByTvIdRatings200Response
import app.gauja.core.api.models.Season
import app.gauja.core.api.models.TvDetails

interface TvApi {
    /**
     * GET tv/{tvId}
     * Get TV details
     * Returns full TV details in a JSON object.
     * Responses:
     *  - 200: TV details
     *
     * @param tvId 
     * @param language  (optional)
     * @return [TvDetails]
     */
    @GET("tv/{tvId}")
    suspend fun getTvByTvId(@Path("tvId") tvId: kotlin.Double, @Query("language") language: kotlin.String? = null): Response<TvDetails>

    /**
     * GET tv/{tvId}/ratings
     * Get TV ratings
     * Returns ratings based on provided tvId in a JSON object.
     * Responses:
     *  - 200: Ratings returned
     *
     * @param tvId 
     * @return [GetTvByTvIdRatings200Response]
     */
    @GET("tv/{tvId}/ratings")
    suspend fun getTvByTvIdRatings(@Path("tvId") tvId: kotlin.Double): Response<GetTvByTvIdRatings200Response>

    /**
     * GET tv/{tvId}/recommendations
     * Get recommended TV series
     * Returns list of recommended TV series based on the provided tvId in a JSON object.
     * Responses:
     *  - 200: List of TV series
     *
     * @param tvId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverTv200Response]
     */
    @GET("tv/{tvId}/recommendations")
    suspend fun getTvByTvIdRecommendations(@Path("tvId") tvId: kotlin.Double, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverTv200Response>

    /**
     * GET tv/{tvId}/season/{seasonNumber}
     * Get season details and episode list
     * Returns season details with a list of episodes in a JSON object.
     * Responses:
     *  - 200: TV details
     *
     * @param tvId 
     * @param seasonNumber 
     * @param language  (optional)
     * @return [Season]
     */
    @GET("tv/{tvId}/season/{seasonNumber}")
    suspend fun getTvByTvIdSeasonBySeasonNumber(@Path("tvId") tvId: kotlin.Double, @Path("seasonNumber") seasonNumber: kotlin.Double, @Query("language") language: kotlin.String? = null): Response<Season>

    /**
     * GET tv/{tvId}/similar
     * Get similar TV series
     * Returns list of similar TV series based on the provided tvId in a JSON object.
     * Responses:
     *  - 200: List of TV series
     *
     * @param tvId 
     * @param page  (optional, default to 1.0)
     * @param language  (optional)
     * @return [GetDiscoverTv200Response]
     */
    @GET("tv/{tvId}/similar")
    suspend fun getTvByTvIdSimilar(@Path("tvId") tvId: kotlin.Double, @Query("page") page: kotlin.Double? = 1.0, @Query("language") language: kotlin.String? = null): Response<GetDiscoverTv200Response>

}
