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

import app.gauja.core.api.models.GetUserByUserIdWatchlist200Response
import app.gauja.core.api.models.Watchlist
import app.gauja.core.api.models.WatchlistRequest

interface WatchlistApi {
    /**
     * DELETE watchlist/{tmdbId}
     * Delete watchlist item
     * Removes a watchlist item.
     * Responses:
     *  - 204: Succesfully removed watchlist item
     *
     * @param tmdbId tmdbId ID
     * @param mediaType 
     * @return [Unit]
     */
    @DELETE("watchlist/{tmdbId}")
    suspend fun deleteWatchlistByTmdbId(@Path("tmdbId") tmdbId: kotlin.String, @Query("mediaType") mediaType: kotlin.String): Response<Unit>

    /**
     * GET user/{userId}/watchlist
     * Get the Plex watchlist for a specific user
     * Retrieves a user&#39;s Plex Watchlist in a JSON object. 
     * Responses:
     *  - 200: Watchlist data returned
     *
     * @param userId 
     * @param page  (optional, default to 1.0)
     * @return [GetUserByUserIdWatchlist200Response]
     */
    @GET("user/{userId}/watchlist")
    suspend fun getUserByUserIdWatchlist(@Path("userId") userId: kotlin.Double, @Query("page") page: kotlin.Double? = 1.0): Response<GetUserByUserIdWatchlist200Response>

    /**
     * POST watchlist
     * Add media to watchlist
     * 
     * Responses:
     *  - 201: Watchlist data returned
     *
     * @param watchlistRequest 
     * @return [Watchlist]
     */
    @POST("watchlist")
    suspend fun postWatchlist(@Body watchlistRequest: WatchlistRequest): Response<Watchlist>

}
