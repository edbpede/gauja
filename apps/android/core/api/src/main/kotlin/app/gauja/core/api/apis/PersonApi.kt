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

import app.gauja.core.api.models.GetPersonByPersonIdCombinedCredits200Response
import app.gauja.core.api.models.PersonDetails

interface PersonApi {
    /**
     * GET person/{personId}
     * Get person details
     * Returns person details based on provided personId in a JSON object.
     * Responses:
     *  - 200: Returned person
     *
     * @param personId 
     * @param language  (optional)
     * @return [PersonDetails]
     */
    @GET("person/{personId}")
    suspend fun getPersonByPersonId(@Path("personId") personId: kotlin.Double, @Query("language") language: kotlin.String? = null): Response<PersonDetails>

    /**
     * GET person/{personId}/combined_credits
     * Get combined credits
     * Returns the person&#39;s combined credits based on the provided personId in a JSON object.
     * Responses:
     *  - 200: Returned combined credits
     *
     * @param personId 
     * @param language  (optional)
     * @return [GetPersonByPersonIdCombinedCredits200Response]
     */
    @GET("person/{personId}/combined_credits")
    suspend fun getPersonByPersonIdCombinedCredits(@Path("personId") personId: kotlin.Double, @Query("language") language: kotlin.String? = null): Response<GetPersonByPersonIdCombinedCredits200Response>

}
