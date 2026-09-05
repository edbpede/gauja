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

import app.gauja.core.api.models.Collection

interface CollectionApi {
    /**
     * GET collection/{collectionId}
     * Get collection details
     * Returns full collection details in a JSON object.
     * Responses:
     *  - 200: Collection details
     *
     * @param collectionId 
     * @param language  (optional)
     * @return [Collection]
     */
    @GET("collection/{collectionId}")
    suspend fun getCollectionByCollectionId(@Path("collectionId") collectionId: kotlin.Double, @Query("language") language: kotlin.String? = null): Response<Collection>

}
