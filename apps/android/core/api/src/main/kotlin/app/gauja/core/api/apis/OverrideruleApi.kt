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

import app.gauja.core.api.models.OverrideRule

interface OverrideruleApi {
    /**
     * DELETE overrideRule/{ruleId}
     * Delete override rule by ID
     * Deletes the override rule with the provided ruleId.
     * Responses:
     *  - 200: Override rule successfully deleted
     *
     * @param ruleId 
     * @return [OverrideRule]
     */
    @DELETE("overrideRule/{ruleId}")
    suspend fun deleteOverrideRuleByRuleId(@Path("ruleId") ruleId: kotlin.Double): Response<OverrideRule>

    /**
     * GET overrideRule
     * Get override rules
     * Returns a list of all override rules with their conditions and settings
     * Responses:
     *  - 200: Override rules returned
     *
     * @return [kotlin.collections.List<OverrideRule>]
     */
    @GET("overrideRule")
    suspend fun getOverrideRule(): Response<kotlin.collections.List<OverrideRule>>

    /**
     * POST overrideRule
     * Create override rule
     * Creates a new Override Rule from the request body.
     * Responses:
     *  - 200: Values were successfully created
     *
     * @return [kotlin.collections.List<OverrideRule>]
     */
    @POST("overrideRule")
    suspend fun postOverrideRule(): Response<kotlin.collections.List<OverrideRule>>

    /**
     * PUT overrideRule/{ruleId}
     * Update override rule
     * Updates an Override Rule from the request body.
     * Responses:
     *  - 200: Values were successfully updated
     *
     * @param ruleId 
     * @return [kotlin.collections.List<OverrideRule>]
     */
    @PUT("overrideRule/{ruleId}")
    suspend fun putOverrideRuleByRuleId(@Path("ruleId") ruleId: kotlin.Double): Response<kotlin.collections.List<OverrideRule>>

}
