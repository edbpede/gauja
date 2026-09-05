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

import app.gauja.core.api.models.GetMedia200Response
import app.gauja.core.api.models.GetMediaByMediaIdWatchData200Response
import app.gauja.core.api.models.MediaInfo
import app.gauja.core.api.models.PostMediaByMediaIdByStatusRequest

interface MediaApi {
    /**
     * DELETE media/{mediaId}
     * Delete media item
     * Removes a media item. The &#x60;MANAGE_REQUESTS&#x60; permission is required to perform this action.
     * Responses:
     *  - 204: Succesfully removed media item
     *
     * @param mediaId Media ID
     * @return [Unit]
     */
    @DELETE("media/{mediaId}")
    suspend fun deleteMediaByMediaId(@Path("mediaId") mediaId: kotlin.String): Response<Unit>

    /**
     * DELETE media/{mediaId}/file
     * Delete media file
     * Removes a media file from radarr/sonarr. The &#x60;ADMIN&#x60; permission is required to perform this action.
     * Responses:
     *  - 204: Successfully removed media item
     *
     * @param mediaId Media ID
     * @param is4k Whether to remove from 4K service instance (true) or regular service instance (false) (optional)
     * @return [Unit]
     */
    @DELETE("media/{mediaId}/file")
    suspend fun deleteMediaByMediaIdFile(@Path("mediaId") mediaId: kotlin.String, @Query("is4k") is4k: kotlin.Boolean? = null): Response<Unit>

    /**
     * GET media
     * Get media
     * Returns all media (can be filtered and limited) in a JSON object.
     * Responses:
     *  - 200: Returned media
     *
     * @param take  (optional)
     * @param skip  (optional)
     * @param filter  (optional)
     * @param sort  (optional, default to "added")
     * @return [GetMedia200Response]
     */
    @GET("media")
    suspend fun getMedia(@Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null, @Query("filter") filter: kotlin.String? = null, @Query("sort") sort: kotlin.String? = "added"): Response<GetMedia200Response>

    /**
     * GET media/{mediaId}/watch_data
     * Get watch data
     * Returns play count, play duration, and users who have watched the media.  Requires the &#x60;ADMIN&#x60; permission. 
     * Responses:
     *  - 200: Users
     *
     * @param mediaId Media ID
     * @return [GetMediaByMediaIdWatchData200Response]
     */
    @GET("media/{mediaId}/watch_data")
    suspend fun getMediaByMediaIdWatchData(@Path("mediaId") mediaId: kotlin.String): Response<GetMediaByMediaIdWatchData200Response>

    /**
     * POST media/{mediaId}/{status}
     * Update media status
     * Updates a media item&#39;s status and returns the media in JSON format
     * Responses:
     *  - 200: Returned media
     *
     * @param mediaId Media ID
     * @param status New status
     * @param postMediaByMediaIdByStatusRequest  (optional)
     * @return [MediaInfo]
     */
    @POST("media/{mediaId}/{status}")
    suspend fun postMediaByMediaIdByStatus(@Path("mediaId") mediaId: kotlin.String, @Path("status") status: kotlin.String, @Body postMediaByMediaIdByStatusRequest: PostMediaByMediaIdByStatusRequest? = null): Response<MediaInfo>

}
