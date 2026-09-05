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

import app.gauja.core.api.models.GetSettingsJellyfinUsers200ResponseInner
import app.gauja.core.api.models.GetSettingsPlexUsers200ResponseInner
import app.gauja.core.api.models.GetUser200Response
import app.gauja.core.api.models.GetUserByUserIdPushSubscriptions200Response
import app.gauja.core.api.models.GetUserByUserIdQuota200Response
import app.gauja.core.api.models.GetUserByUserIdRequests200Response
import app.gauja.core.api.models.GetUserByUserIdSettingsPassword200Response
import app.gauja.core.api.models.GetUserByUserIdSettingsPermissions200Response
import app.gauja.core.api.models.GetUserByUserIdWatchData200Response
import app.gauja.core.api.models.GetUserByUserIdWatchlist200Response
import app.gauja.core.api.models.PostAuthJellyfinQuickconnectAuthenticateRequest
import app.gauja.core.api.models.PostAuthLogout200Response
import app.gauja.core.api.models.PostAuthPlexRequest
import app.gauja.core.api.models.PostAuthResetPasswordByGuidRequest
import app.gauja.core.api.models.PostAuthResetPasswordRequest
import app.gauja.core.api.models.PostUserByUserIdSettingsLinkedAccountsJellyfinRequest
import app.gauja.core.api.models.PostUserByUserIdSettingsPasswordRequest
import app.gauja.core.api.models.PostUserByUserIdSettingsPermissionsRequest
import app.gauja.core.api.models.PostUserImportFromJellyfinRequest
import app.gauja.core.api.models.PostUserImportFromPlexRequest
import app.gauja.core.api.models.PostUserRegisterPushSubscriptionRequest
import app.gauja.core.api.models.PostUserRequest
import app.gauja.core.api.models.PutUserRequest
import app.gauja.core.api.models.User
import app.gauja.core.api.models.UserSettings
import app.gauja.core.api.models.UserSettingsNotifications

interface UsersApi {
    /**
     * DELETE user/{userId}
     * Delete user by ID
     * Deletes the user with the provided userId. Requires the &#x60;MANAGE_USERS&#x60; permission.
     * Responses:
     *  - 200: User successfully deleted
     *
     * @param userId 
     * @return [User]
     */
    @DELETE("user/{userId}")
    suspend fun deleteUserByUserId(@Path("userId") userId: kotlin.Double): Response<User>

    /**
     * DELETE user/{userId}/pushSubscription/{endpoint}
     * Delete user push subscription by key
     * Deletes the user push subscription with the provided key.
     * Responses:
     *  - 204: Successfully removed user push subscription
     *
     * @param userId 
     * @param endpoint 
     * @return [Unit]
     */
    @DELETE("user/{userId}/pushSubscription/{endpoint}")
    suspend fun deleteUserByUserIdPushSubscriptionByEndpoint(@Path("userId") userId: kotlin.Double, @Path("endpoint") endpoint: kotlin.String): Response<Unit>

    /**
     * DELETE user/{userId}/settings/linked-accounts/jellyfin
     * Remove the linked Jellyfin account for a user
     * Removes the linked Jellyfin account for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.
     * Responses:
     *  - 204: Unlinking account succeeded
     *  - 400: Unlink request invalid
     *  - 404: User does not exist
     *
     * @param userId 
     * @return [Unit]
     */
    @DELETE("user/{userId}/settings/linked-accounts/jellyfin")
    suspend fun deleteUserByUserIdSettingsLinkedAccountsJellyfin(@Path("userId") userId: kotlin.Double): Response<Unit>

    /**
     * DELETE user/{userId}/settings/linked-accounts/plex
     * Remove the linked Plex account for a user
     * Removes the linked Plex account for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.
     * Responses:
     *  - 204: Unlinking account succeeded
     *  - 400: Unlink request invalid
     *  - 404: User does not exist
     *
     * @param userId 
     * @return [Unit]
     */
    @DELETE("user/{userId}/settings/linked-accounts/plex")
    suspend fun deleteUserByUserIdSettingsLinkedAccountsPlex(@Path("userId") userId: kotlin.Double): Response<Unit>

    /**
     * GET auth/me
     * Get logged-in user
     * Returns the currently logged-in user.
     * Responses:
     *  - 200: Object containing the logged-in user in JSON
     *
     * @return [User]
     */
    @GET("auth/me")
    suspend fun getAuthMe(): Response<User>

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
     * GET user
     * Get all users
     * Returns all users in a JSON object.
     * Responses:
     *  - 200: A JSON array of all users
     *
     * @param take  (optional)
     * @param skip  (optional)
     * @param sort  (optional, default to "created")
     * @param sortDirection Sort direction. When omitted, the server chooses the direction per sort field (e.g. displayname defaults to asc, requests/updated to desc).  (optional)
     * @param q  (optional)
     * @param includeIds  (optional)
     * @return [GetUser200Response]
     */
    @GET("user")
    suspend fun getUser(@Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null, @Query("sort") sort: kotlin.String? = "created", @Query("sortDirection") sortDirection: kotlin.String? = null, @Query("q") q: kotlin.String? = null, @Query("includeIds") includeIds: kotlin.String? = null): Response<GetUser200Response>

    /**
     * GET user/{userId}
     * Get user by ID
     * Retrieves user details in a JSON object. Requires the &#x60;MANAGE_USERS&#x60; permission. 
     * Responses:
     *  - 200: Users details in JSON
     *
     * @param userId 
     * @return [User]
     */
    @GET("user/{userId}")
    suspend fun getUserByUserId(@Path("userId") userId: kotlin.Double): Response<User>

    /**
     * GET user/{userId}/pushSubscription/{endpoint}
     * Get web push notification settings for a user
     * Returns web push notification settings for a user in a JSON object. 
     * Responses:
     *  - 200: User web push notification settings in JSON
     *
     * @param userId 
     * @param endpoint 
     * @return [GetUserByUserIdPushSubscriptions200Response]
     */
    @GET("user/{userId}/pushSubscription/{endpoint}")
    suspend fun getUserByUserIdPushSubscriptionByEndpoint(@Path("userId") userId: kotlin.Double, @Path("endpoint") endpoint: kotlin.String): Response<GetUserByUserIdPushSubscriptions200Response>

    /**
     * GET user/{userId}/pushSubscriptions
     * Get all web push notification settings for a user
     * Returns all web push notification settings for a user in a JSON object. 
     * Responses:
     *  - 200: User web push notification settings in JSON
     *
     * @param userId 
     * @return [GetUserByUserIdPushSubscriptions200Response]
     */
    @GET("user/{userId}/pushSubscriptions")
    suspend fun getUserByUserIdPushSubscriptions(@Path("userId") userId: kotlin.Double): Response<GetUserByUserIdPushSubscriptions200Response>

    /**
     * GET user/{userId}/quota
     * Get quotas for a specific user
     * Returns quota details for a user in a JSON object. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users. 
     * Responses:
     *  - 200: User quota details in JSON
     *
     * @param userId 
     * @return [GetUserByUserIdQuota200Response]
     */
    @GET("user/{userId}/quota")
    suspend fun getUserByUserIdQuota(@Path("userId") userId: kotlin.Double): Response<GetUserByUserIdQuota200Response>

    /**
     * GET user/{userId}/requests
     * Get requests for a specific user
     * Retrieves a user&#39;s requests in a JSON object. 
     * Responses:
     *  - 200: User's requests returned
     *
     * @param userId 
     * @param take  (optional)
     * @param skip  (optional)
     * @return [GetUserByUserIdRequests200Response]
     */
    @GET("user/{userId}/requests")
    suspend fun getUserByUserIdRequests(@Path("userId") userId: kotlin.Double, @Query("take") take: kotlin.Double? = null, @Query("skip") skip: kotlin.Double? = null): Response<GetUserByUserIdRequests200Response>

    /**
     * GET user/{userId}/settings/main
     * Get general settings for a user
     * Returns general settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.
     * Responses:
     *  - 200: User general settings returned
     *
     * @param userId 
     * @return [UserSettings]
     */
    @GET("user/{userId}/settings/main")
    suspend fun getUserByUserIdSettingsMain(@Path("userId") userId: kotlin.Double): Response<UserSettings>

    /**
     * GET user/{userId}/settings/notifications
     * Get notification settings for a user
     * Returns notification settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.
     * Responses:
     *  - 200: User notification settings returned
     *
     * @param userId 
     * @return [UserSettingsNotifications]
     */
    @GET("user/{userId}/settings/notifications")
    suspend fun getUserByUserIdSettingsNotifications(@Path("userId") userId: kotlin.Double): Response<UserSettingsNotifications>

    /**
     * GET user/{userId}/settings/password
     * Get password page informatiom
     * Returns important data for the password page to function correctly. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.
     * Responses:
     *  - 200: User password page information returned
     *
     * @param userId 
     * @return [GetUserByUserIdSettingsPassword200Response]
     */
    @GET("user/{userId}/settings/password")
    suspend fun getUserByUserIdSettingsPassword(@Path("userId") userId: kotlin.Double): Response<GetUserByUserIdSettingsPassword200Response>

    /**
     * GET user/{userId}/settings/permissions
     * Get permission settings for a user
     * Returns permission settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.
     * Responses:
     *  - 200: User permission settings returned
     *
     * @param userId 
     * @return [GetUserByUserIdSettingsPermissions200Response]
     */
    @GET("user/{userId}/settings/permissions")
    suspend fun getUserByUserIdSettingsPermissions(@Path("userId") userId: kotlin.Double): Response<GetUserByUserIdSettingsPermissions200Response>

    /**
     * GET user/{userId}/watch_data
     * Get watch data
     * Returns play count, play duration, and recently watched media.  Requires the &#x60;ADMIN&#x60; permission to fetch results for other users. 
     * Responses:
     *  - 200: Users
     *
     * @param userId 
     * @return [GetUserByUserIdWatchData200Response]
     */
    @GET("user/{userId}/watch_data")
    suspend fun getUserByUserIdWatchData(@Path("userId") userId: kotlin.Double): Response<GetUserByUserIdWatchData200Response>

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
     * GET user/jellyfin/{jellyfinUserId}
     * Get user by Jellyfin user ID
     * Retrieves user details by Jellyfin user ID in a JSON object. Returns filtered data based on the caller&#39;s permissions. 
     * Responses:
     *  - 200: User details in JSON
     *  - 404: User not found
     *
     * @param jellyfinUserId The Jellyfin user ID (32-character hexadecimal string)
     * @return [User]
     */
    @GET("user/jellyfin/{jellyfinUserId}")
    suspend fun getUserJellyfinByJellyfinUserId(@Path("jellyfinUserId") jellyfinUserId: kotlin.String): Response<User>

    /**
     * POST auth/reset-password
     * Send a reset password email
     * Sends a reset password email to the email if the user exists
     * Responses:
     *  - 200: OK
     *
     * @param postAuthResetPasswordRequest 
     * @return [PostAuthLogout200Response]
     */
    @POST("auth/reset-password")
    suspend fun postAuthResetPassword(@Body postAuthResetPasswordRequest: PostAuthResetPasswordRequest): Response<PostAuthLogout200Response>

    /**
     * POST auth/reset-password/{guid}
     * Reset the password for a user
     * Resets the password for a user if the given guid is connected to a user
     * Responses:
     *  - 200: OK
     *
     * @param guid 
     * @param postAuthResetPasswordByGuidRequest 
     * @return [PostAuthLogout200Response]
     */
    @POST("auth/reset-password/{guid}")
    suspend fun postAuthResetPasswordByGuid(@Path("guid") guid: kotlin.String, @Body postAuthResetPasswordByGuidRequest: PostAuthResetPasswordByGuidRequest): Response<PostAuthLogout200Response>

    /**
     * POST user
     * Create new user
     * Creates a new user. Requires the &#x60;MANAGE_USERS&#x60; permission. 
     * Responses:
     *  - 201: The created user
     *
     * @param postUserRequest 
     * @return [User]
     */
    @POST("user")
    suspend fun postUser(@Body postUserRequest: PostUserRequest): Response<User>

    /**
     * POST user/{userId}/settings/linked-accounts/jellyfin
     * Link the provided Jellyfin account to the current user
     * Logs in to Jellyfin with the provided credentials, then links the associated Jellyfin account with the user&#39;s account. Users can only link external accounts to their own account.
     * Responses:
     *  - 204: Linking account succeeded
     *  - 403: Invalid credentials
     *  - 422: Account already linked to a user
     *
     * @param userId 
     * @param postUserByUserIdSettingsLinkedAccountsJellyfinRequest 
     * @return [Unit]
     */
    @POST("user/{userId}/settings/linked-accounts/jellyfin")
    suspend fun postUserByUserIdSettingsLinkedAccountsJellyfin(@Path("userId") userId: kotlin.Double, @Body postUserByUserIdSettingsLinkedAccountsJellyfinRequest: PostUserByUserIdSettingsLinkedAccountsJellyfinRequest): Response<Unit>

    /**
     * POST user/{userId}/settings/linked-accounts/jellyfin/quickconnect
     * Link Jellyfin/Emby account with Quick Connect
     * Links a Jellyfin/Emby account to the user&#39;s profile using Quick Connect authentication
     * Responses:
     *  - 400: Invalid Quick Connect secret
     *  - 204: Account successfully linked
     *  - 401: Unauthorized
     *  - 422: Account already linked
     *  - 500: Server error
     *
     * @param userId 
     * @param postAuthJellyfinQuickconnectAuthenticateRequest 
     * @return [Unit]
     */
    @POST("user/{userId}/settings/linked-accounts/jellyfin/quickconnect")
    suspend fun postUserByUserIdSettingsLinkedAccountsJellyfinQuickconnect(@Path("userId") userId: kotlin.Double, @Body postAuthJellyfinQuickconnectAuthenticateRequest: PostAuthJellyfinQuickconnectAuthenticateRequest): Response<Unit>

    /**
     * POST user/{userId}/settings/linked-accounts/plex
     * Link the provided Plex account to the current user
     * Logs in to Plex with the provided auth token, then links the associated Plex account with the user&#39;s account. Users can only link external accounts to their own account.
     * Responses:
     *  - 204: Linking account succeeded
     *  - 403: Invalid credentials
     *  - 422: Account already linked to a user
     *
     * @param userId 
     * @param postAuthPlexRequest 
     * @return [Unit]
     */
    @POST("user/{userId}/settings/linked-accounts/plex")
    suspend fun postUserByUserIdSettingsLinkedAccountsPlex(@Path("userId") userId: kotlin.Double, @Body postAuthPlexRequest: PostAuthPlexRequest): Response<Unit>

    /**
     * POST user/{userId}/settings/main
     * Update general settings for a user
     * Updates and returns general settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.
     * Responses:
     *  - 200: Updated user general settings returned
     *
     * @param userId 
     * @param userSettings 
     * @return [UserSettings]
     */
    @POST("user/{userId}/settings/main")
    suspend fun postUserByUserIdSettingsMain(@Path("userId") userId: kotlin.Double, @Body userSettings: UserSettings): Response<UserSettings>

    /**
     * POST user/{userId}/settings/notifications
     * Update notification settings for a user
     * Updates and returns notification settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.
     * Responses:
     *  - 200: Updated user notification settings returned
     *
     * @param userId 
     * @param userSettingsNotifications 
     * @return [UserSettingsNotifications]
     */
    @POST("user/{userId}/settings/notifications")
    suspend fun postUserByUserIdSettingsNotifications(@Path("userId") userId: kotlin.Double, @Body userSettingsNotifications: UserSettingsNotifications): Response<UserSettingsNotifications>

    /**
     * POST user/{userId}/settings/password
     * Update password for a user
     * Updates a user&#39;s password. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.
     * Responses:
     *  - 204: User password updated
     *
     * @param userId 
     * @param postUserByUserIdSettingsPasswordRequest 
     * @return [Unit]
     */
    @POST("user/{userId}/settings/password")
    suspend fun postUserByUserIdSettingsPassword(@Path("userId") userId: kotlin.Double, @Body postUserByUserIdSettingsPasswordRequest: PostUserByUserIdSettingsPasswordRequest): Response<Unit>

    /**
     * POST user/{userId}/settings/permissions
     * Update permission settings for a user
     * Updates and returns permission settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.
     * Responses:
     *  - 200: Updated user general settings returned
     *
     * @param userId 
     * @param postUserByUserIdSettingsPermissionsRequest 
     * @return [GetUserByUserIdSettingsPermissions200Response]
     */
    @POST("user/{userId}/settings/permissions")
    suspend fun postUserByUserIdSettingsPermissions(@Path("userId") userId: kotlin.Double, @Body postUserByUserIdSettingsPermissionsRequest: PostUserByUserIdSettingsPermissionsRequest): Response<GetUserByUserIdSettingsPermissions200Response>

    /**
     * POST user/import-from-jellyfin
     * Import all users from Jellyfin
     * Fetches and imports users from the Jellyfin server.  Requires the &#x60;MANAGE_USERS&#x60; permission. 
     * Responses:
     *  - 201: A list of the newly created users
     *
     * @param postUserImportFromJellyfinRequest  (optional)
     * @return [kotlin.collections.List<User>]
     */
    @POST("user/import-from-jellyfin")
    suspend fun postUserImportFromJellyfin(@Body postUserImportFromJellyfinRequest: PostUserImportFromJellyfinRequest? = null): Response<kotlin.collections.List<User>>

    /**
     * POST user/import-from-plex
     * Import all users from Plex
     * Fetches and imports users from the Plex server. If a list of Plex IDs is provided in the request body, only the specified users will be imported. Otherwise, all users will be imported.  Requires the &#x60;MANAGE_USERS&#x60; permission. 
     * Responses:
     *  - 201: A list of the newly created users
     *
     * @param postUserImportFromPlexRequest  (optional)
     * @return [kotlin.collections.List<User>]
     */
    @POST("user/import-from-plex")
    suspend fun postUserImportFromPlex(@Body postUserImportFromPlexRequest: PostUserImportFromPlexRequest? = null): Response<kotlin.collections.List<User>>

    /**
     * POST user/registerPushSubscription
     * Register a web push /user/registerPushSubscription
     * Registers a web push subscription for the logged-in user
     * Responses:
     *  - 204: Successfully registered push subscription
     *
     * @param postUserRegisterPushSubscriptionRequest 
     * @return [Unit]
     */
    @POST("user/registerPushSubscription")
    suspend fun postUserRegisterPushSubscription(@Body postUserRegisterPushSubscriptionRequest: PostUserRegisterPushSubscriptionRequest): Response<Unit>

    /**
     * PUT user
     * Update batch of users
     * Update users with given IDs with provided values in request &#x60;body.settings&#x60;. You cannot update users&#39; Plex tokens through this request.  Requires the &#x60;MANAGE_USERS&#x60; permission. 
     * Responses:
     *  - 200: Successfully updated user details
     *
     * @param putUserRequest 
     * @return [kotlin.collections.List<User>]
     */
    @PUT("user")
    suspend fun putUser(@Body putUserRequest: PutUserRequest): Response<kotlin.collections.List<User>>

    /**
     * PUT user/{userId}
     * Update a user by user ID
     * Update a user with the provided values. You cannot update a user&#39;s Plex token through this request.  Requires the &#x60;MANAGE_USERS&#x60; permission. 
     * Responses:
     *  - 200: Successfully updated user details
     *
     * @param userId 
     * @param user 
     * @return [User]
     */
    @PUT("user/{userId}")
    suspend fun putUserByUserId(@Path("userId") userId: kotlin.Double, @Body user: User): Response<User>

}
