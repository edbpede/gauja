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

import app.gauja.core.api.models.GetIssue200Response
import app.gauja.core.api.models.GetIssueCount200Response
import app.gauja.core.api.models.Issue
import app.gauja.core.api.models.IssueComment
import app.gauja.core.api.models.PostIssueByIssueIdCommentRequest
import app.gauja.core.api.models.PostIssueRequest
import app.gauja.core.api.models.PutIssueCommentByCommentIdRequest

interface IssueApi {
    /**
     * DELETE issue/{issueId}
     * Delete issue
     * Removes an issue. If the user has the &#x60;MANAGE_ISSUES&#x60; permission, any issue can be removed. Otherwise, only a users own issues can be removed.
     * Responses:
     *  - 204: Succesfully removed issue
     *
     * @param issueId Issue ID
     * @return [Unit]
     */
    @DELETE("issue/{issueId}")
    suspend fun deleteIssueByIssueId(@Path("issueId") issueId: kotlin.String): Response<Unit>

    /**
     * DELETE issueComment/{commentId}
     * Delete issue comment
     * Deletes an issue comment. Only users with &#x60;MANAGE_ISSUES&#x60; or the user who created the comment can perform this action. 
     * Responses:
     *  - 204: Succesfully removed issue comment
     *
     * @param commentId Issue Comment ID
     * @return [Unit]
     */
    @DELETE("issueComment/{commentId}")
    suspend fun deleteIssueCommentByCommentId(@Path("commentId") commentId: kotlin.String): Response<Unit>

    /**
     * GET issue
     * Get all issues
     * Returns a list of issues in JSON format. 
     * Responses:
     *  - 200: Issues returned
     *
     * @param take  (optional)
     * @param skip  (optional)
     * @param sort  (optional, default to "added")
     * @param filter  (optional, default to "open")
     * @param requestedBy  (optional)
     * @return [GetIssue200Response]
     */
    @GET("issue")
    suspend fun getIssue(@Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null, @Query("sort") sort: kotlin.String? = "added", @Query("filter") filter: kotlin.String? = "open", @Query("requestedBy") requestedBy: kotlin.Double? = null): Response<GetIssue200Response>

    /**
     * GET issue/{issueId}
     * Get issue
     * Returns a single issue in JSON format. 
     * Responses:
     *  - 200: Issues returned
     *
     * @param issueId 
     * @return [Issue]
     */
    @GET("issue/{issueId}")
    suspend fun getIssueByIssueId(@Path("issueId") issueId: kotlin.Double): Response<Issue>

    /**
     * GET issueComment/{commentId}
     * Get issue comment
     * Returns a single issue comment in JSON format. 
     * Responses:
     *  - 200: Comment returned
     *
     * @param commentId 
     * @return [IssueComment]
     */
    @GET("issueComment/{commentId}")
    suspend fun getIssueCommentByCommentId(@Path("commentId") commentId: kotlin.String): Response<IssueComment>

    /**
     * GET issue/count
     * Gets issue counts
     * Returns the number of open and closed issues, as well as the number of issues of each type. 
     * Responses:
     *  - 200: Issue counts returned
     *
     * @return [GetIssueCount200Response]
     */
    @GET("issue/count")
    suspend fun getIssueCount(): Response<GetIssueCount200Response>

    /**
     * POST issue
     * Create new issue
     * Creates a new issue 
     * Responses:
     *  - 201: Succesfully created the issue
     *
     * @param postIssueRequest 
     * @return [Issue]
     */
    @POST("issue")
    suspend fun postIssue(@Body postIssueRequest: PostIssueRequest): Response<Issue>

    /**
     * POST issue/{issueId}/{status}
     * Update an issue&#39;s status
     * Updates an issue&#39;s status to approved or declined. Also returns the issue in a JSON object.  Requires the &#x60;MANAGE_ISSUES&#x60; permission or &#x60;ADMIN&#x60;. 
     * Responses:
     *  - 200: Issue status changed
     *
     * @param issueId Issue ID
     * @param status New status
     * @return [Issue]
     */
    @POST("issue/{issueId}/{status}")
    suspend fun postIssueByIssueIdByStatus(@Path("issueId") issueId: kotlin.String, @Path("status") status: kotlin.String): Response<Issue>

    /**
     * POST issue/{issueId}/comment
     * Create a comment
     * Creates a comment and returns associated issue in JSON format. 
     * Responses:
     *  - 200: Issue returned with new comment
     *
     * @param issueId 
     * @param postIssueByIssueIdCommentRequest 
     * @return [Issue]
     */
    @POST("issue/{issueId}/comment")
    suspend fun postIssueByIssueIdComment(@Path("issueId") issueId: kotlin.Double, @Body postIssueByIssueIdCommentRequest: PostIssueByIssueIdCommentRequest): Response<Issue>

    /**
     * PUT issueComment/{commentId}
     * Update issue comment
     * Updates and returns a single issue comment in JSON format. 
     * Responses:
     *  - 200: Comment updated
     *
     * @param commentId 
     * @param putIssueCommentByCommentIdRequest 
     * @return [IssueComment]
     */
    @PUT("issueComment/{commentId}")
    suspend fun putIssueCommentByCommentId(@Path("commentId") commentId: kotlin.String, @Body putIssueCommentByCommentIdRequest: PutIssueCommentByCommentIdRequest): Response<IssueComment>

}
