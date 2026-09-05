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

import app.gauja.core.api.models.GetRequestCount200Response
import app.gauja.core.api.models.GetUserByUserIdRequests200Response
import app.gauja.core.api.models.MediaRequest
import app.gauja.core.api.models.PostRequestRequest
import app.gauja.core.api.models.PutRequestByRequestIdRequest

interface RequestApi {
    /**
     * DELETE request/{requestId}
     * Delete request
     * Removes a request. If the user has the &#x60;MANAGE_REQUESTS&#x60; permission, any request can be removed. Otherwise, only pending requests can be removed.
     * Responses:
     *  - 204: Succesfully removed request
     *
     * @param requestId Request ID
     * @return [Unit]
     */
    @DELETE("request/{requestId}")
    suspend fun deleteRequestByRequestId(@Path("requestId") requestId: kotlin.String): Response<Unit>

    /**
     * GET request
     * Get all requests
     * Returns all requests if the user has the &#x60;ADMIN&#x60; or &#x60;MANAGE_REQUESTS&#x60; permissions. Otherwise, only the logged-in user&#39;s requests are returned.  If the &#x60;requestedBy&#x60; parameter is specified, only requests from that particular user ID will be returned. 
     * Responses:
     *  - 200: Requests returned
     *
     * @param take  (optional)
     * @param skip  (optional)
     * @param filter  (optional)
     * @param sort  (optional, default to "added")
     * @param sortDirection  (optional, default to "desc")
     * @param requestedBy  (optional)
     * @param mediaType  (optional, default to "all")
     * @return [GetUserByUserIdRequests200Response]
     */
    @GET("request")
    suspend fun getRequest(@Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null, @Query("filter") filter: kotlin.String? = null, @Query("sort") sort: kotlin.String? = "added", @Query("sortDirection") sortDirection: kotlin.String? = "desc", @Query("requestedBy") requestedBy: kotlin.Double? = null, @Query("mediaType") mediaType: kotlin.String? = "all"): Response<GetUserByUserIdRequests200Response>

    /**
     * GET request/{requestId}
     * Get MediaRequest
     * Returns a specific MediaRequest in a JSON object.
     * Responses:
     *  - 200: Succesfully returns request
     *
     * @param requestId Request ID
     * @return [MediaRequest]
     */
    @GET("request/{requestId}")
    suspend fun getRequestByRequestId(@Path("requestId") requestId: kotlin.String): Response<MediaRequest>

    /**
     * GET request/count
     * Gets request counts
     * Returns the number of requests by status including pending, approved, available, and completed requests. 
     * Responses:
     *  - 200: Request counts returned
     *
     * @return [GetRequestCount200Response]
     */
    @GET("request/count")
    suspend fun getRequestCount(): Response<GetRequestCount200Response>

    /**
     * POST request
     * Create new request
     * Creates a new request with the provided media ID and type. The &#x60;REQUEST&#x60; permission is required.  If the user has the &#x60;ADMIN&#x60; or &#x60;AUTO_APPROVE&#x60; permissions, their request will be auomatically approved. 
     * Responses:
     *  - 201: Succesfully created the request
     *
     * @param postRequestRequest 
     * @return [MediaRequest]
     */
    @POST("request")
    suspend fun postRequest(@Body postRequestRequest: PostRequestRequest): Response<MediaRequest>

    /**
     * POST request/{requestId}/{status}
     * Update a request&#39;s status
     * Updates a request&#39;s status to approved or declined. Also returns the request in a JSON object.  Requires the &#x60;MANAGE_REQUESTS&#x60; permission or &#x60;ADMIN&#x60;. 
     * Responses:
     *  - 200: Request status changed
     *
     * @param requestId Request ID
     * @param status New status
     * @return [MediaRequest]
     */
    @POST("request/{requestId}/{status}")
    suspend fun postRequestByRequestIdByStatus(@Path("requestId") requestId: kotlin.String, @Path("status") status: kotlin.String): Response<MediaRequest>

    /**
     * POST request/{requestId}/retry
     * Retry failed request
     * Retries a request by resending requests to Sonarr or Radarr.  Requires the &#x60;MANAGE_REQUESTS&#x60; permission or &#x60;ADMIN&#x60;. 
     * Responses:
     *  - 200: Retry triggered
     *
     * @param requestId Request ID
     * @return [MediaRequest]
     */
    @POST("request/{requestId}/retry")
    suspend fun postRequestByRequestIdRetry(@Path("requestId") requestId: kotlin.String): Response<MediaRequest>

    /**
     * PUT request/{requestId}
     * Update MediaRequest
     * Updates a specific media request and returns the request in a JSON object. Requires the &#x60;MANAGE_REQUESTS&#x60; permission.
     * Responses:
     *  - 200: Succesfully updated request
     *
     * @param requestId Request ID
     * @param putRequestByRequestIdRequest 
     * @return [MediaRequest]
     */
    @PUT("request/{requestId}")
    suspend fun putRequestByRequestId(@Path("requestId") requestId: kotlin.String, @Body putRequestByRequestIdRequest: PutRequestByRequestIdRequest): Response<MediaRequest>

}
