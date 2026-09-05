// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.feature.servers.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.gauja.core.designsystem.GaujaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class ServerCheckScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun emptyAddressCannotSubmit() {
        compose.setContent { GaujaTheme { ServerCheckScreen(ServerCheckState(), {}, {}, {}) } }
        compose.onNodeWithText("Check server").assertIsNotEnabled()
    }

    @Test
    fun pendingRequestOffersCancellation() {
        var cancelled = false
        compose.setContent {
            GaujaTheme {
                ServerCheckScreen(ServerCheckState(checking = true), {}, {}, { cancelled = true })
            }
        }
        compose.onNodeWithText("Cancel").assertIsDisplayed().performClick()
        assertTrue(cancelled)
    }
}
