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

import app.gauja.core.api.models.Blocklist
import app.gauja.core.api.models.GetBlocklist200Response

interface BlocklistApi {
    /**
     * DELETE blacklist/{tmdbId}
     * Remove media from blocklist
     * **DEPRECATED**: Use &#x60;/blocklist/{tmdbId}&#x60; instead. This endpoint will be deprecated soon. 
     * Responses:
     *  - 204: Succesfully removed media item
     *
     * @param tmdbId tmdbId ID
     * @param mediaType 
     * @return [Unit]
     */
    @Deprecated("This api was deprecated")
    @DELETE("blacklist/{tmdbId}")
    suspend fun deleteBlacklistByTmdbId(@Path("tmdbId") tmdbId: kotlin.String, @Query("mediaType") mediaType: kotlin.String): Response<Unit>

    /**
     * DELETE blocklist/{tmdbId}
     * Remove media from blocklist
     * 
     * Responses:
     *  - 204: Succesfully removed media item
     *
     * @param tmdbId tmdbId ID
     * @param mediaType 
     * @return [Unit]
     */
    @DELETE("blocklist/{tmdbId}")
    suspend fun deleteBlocklistByTmdbId(@Path("tmdbId") tmdbId: kotlin.String, @Query("mediaType") mediaType: kotlin.String): Response<Unit>

    /**
     * DELETE blocklist/collection/{collectionId}
     * Remove collection from blocklist
     * Removes all movies in a collection from the blocklist
     * Responses:
     *  - 204: Successfully removed collection from blocklist
     *  - 500: Error removing collection from blocklist
     *
     * @param collectionId Collection ID
     * @return [Unit]
     */
    @DELETE("blocklist/collection/{collectionId}")
    suspend fun deleteBlocklistCollectionByCollectionId(@Path("collectionId") collectionId: kotlin.String): Response<Unit>

    /**
     * GET blacklist
     * Returns blocklisted items
     * **DEPRECATED**: Use &#x60;/blocklist&#x60; instead. This endpoint will be deprecated soon. 
     * Responses:
     *  - 200: Blocklisted items returned
     *
     * @param take  (optional)
     * @param skip  (optional)
     * @param search  (optional)
     * @param filter  (optional, default to "manual")
     * @return [GetBlocklist200Response]
     */
    @Deprecated("This api was deprecated")
    @GET("blacklist")
    suspend fun getBlacklist(@Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null, @Query("search") search: kotlin.String? = null, @Query("filter") filter: kotlin.String? = "manual"): Response<GetBlocklist200Response>

    /**
     * GET blacklist/{tmdbId}
     * Get media from blocklist
     * **DEPRECATED**: Use &#x60;/blocklist/{tmdbId}&#x60; instead. This endpoint will be deprecated soon. 
     * Responses:
     *  - 200: Blocklist details in JSON
     *
     * @param tmdbId tmdbId ID
     * @param mediaType 
     * @return [Unit]
     */
    @Deprecated("This api was deprecated")
    @GET("blacklist/{tmdbId}")
    suspend fun getBlacklistByTmdbId(@Path("tmdbId") tmdbId: kotlin.String, @Query("mediaType") mediaType: kotlin.String): Response<Unit>

    /**
     * GET blocklist
     * Returns blocklisted items
     * Returns list of all blocklisted media
     * Responses:
     *  - 200: Blocklisted items returned
     *
     * @param take  (optional)
     * @param skip  (optional)
     * @param search  (optional)
     * @param filter  (optional, default to "manual")
     * @return [GetBlocklist200Response]
     */
    @GET("blocklist")
    suspend fun getBlocklist(@Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null, @Query("search") search: kotlin.String? = null, @Query("filter") filter: kotlin.String? = "manual"): Response<GetBlocklist200Response>

    /**
     * GET blocklist/{tmdbId}
     * Get media from blocklist
     * 
     * Responses:
     *  - 200: Blocklist details in JSON
     *
     * @param tmdbId tmdbId ID
     * @param mediaType 
     * @return [Unit]
     */
    @GET("blocklist/{tmdbId}")
    suspend fun getBlocklistByTmdbId(@Path("tmdbId") tmdbId: kotlin.String, @Query("mediaType") mediaType: kotlin.String): Response<Unit>

    /**
     * POST blacklist
     * Add media to blocklist
     * **DEPRECATED**: Use &#x60;/blocklist&#x60; instead. This endpoint will be deprecated soon. 
     * Responses:
     *  - 201: Item succesfully blocklisted
     *  - 412: Item has already been blocklisted
     *
     * @param blocklist 
     * @return [Unit]
     */
    @Deprecated("This api was deprecated")
    @POST("blacklist")
    suspend fun postBlacklist(@Body blocklist: Blocklist): Response<Unit>

    /**
     * POST blocklist
     * Add media to blocklist
     * 
     * Responses:
     *  - 201: Item succesfully blocklisted
     *  - 412: Item has already been blocklisted
     *
     * @param blocklist 
     * @return [Unit]
     */
    @POST("blocklist")
    suspend fun postBlocklist(@Body blocklist: Blocklist): Response<Unit>

    /**
     * POST blocklist/collection/{collectionId}
     * Add collection to blocklist
     * Adds all movies in a collection to the blocklist
     * Responses:
     *  - 201: Successfully added collection to blocklist
     *  - 500: Error adding collection to blocklist
     *
     * @param collectionId Collection ID
     * @param body  (optional)
     * @return [Unit]
     */
    @POST("blocklist/collection/{collectionId}")
    suspend fun postBlocklistCollectionByCollectionId(@Path("collectionId") collectionId: kotlin.String, @Body body: kotlin.Any? = null): Response<Unit>

}
