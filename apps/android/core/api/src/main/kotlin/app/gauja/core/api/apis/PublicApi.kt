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

import app.gauja.core.api.models.GetStatus200Response
import app.gauja.core.api.models.GetStatusAppdata200Response

interface PublicApi {
    /**
     * GET status
     * Get Seerr status
     * Returns the current Seerr status in a JSON object. updateAvailable and commitsBehind are omitted when checkUpdateAvailable is false.
     * Responses:
     *  - 200: Returned status
     *
     * @param checkUpdateAvailable If false, updateAvailable and commitsBehind will be omitted from the response. Defaults to the versionCheck setting. (optional)
     * @return [GetStatus200Response]
     */
    @GET("status")
    suspend fun getStatus(@Query("checkUpdateAvailable") checkUpdateAvailable: kotlin.Boolean? = null): Response<GetStatus200Response>

    /**
     * GET status/appdata
     * Get application data volume status
     * For Docker installs, returns whether or not the volume mount was configured properly. Always returns true for non-Docker installs.
     * Responses:
     *  - 200: Application data volume status and path
     *
     * @return [GetStatusAppdata200Response]
     */
    @GET("status/appdata")
    suspend fun getStatusAppdata(): Response<GetStatusAppdata200Response>

}
