// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.feature.servers.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.gauja.core.common.ProbeError
import app.gauja.core.common.ProbeException
import app.gauja.core.data.servers.ServerProbe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ServerCheckViewModelTest {
    @get:Rule val main = MainDispatcherRule()

    @Test
    fun credentialsInRejectedAddressNeverEnterSavedState() {
        val saved = SavedStateHandle()
        val model = ServerCheckViewModel(saved, mockk())
        model.edit("https://user:password@example.invalid")
        assertEquals("", saved.get<String>("address"))
    }

    @Test
    fun invalidAddressNeverReachesTransport() = runTest {
        val probe = mockk<ServerProbe>()
        val model = ServerCheckViewModel(SavedStateHandle(), probe)
        model.state.test {
            awaitItem()
            model.edit("ftp://invalid")
            assertEquals("ftp://invalid", awaitItem().address)
            model.check()
            assertEquals(ProbeError.ADDRESS, awaitItem().error)
            coVerify(exactly = 0) { probe.check(any()) }
        }
    }

    @Test
    fun cancellationClearsLoadingAndPreservesDraftForRecreation() = runTest {
        val probe = mockk<ServerProbe>()
        coEvery { probe.check(any()) } coAnswers { awaitCancellation() }
        val saved = SavedStateHandle()
        val model = ServerCheckViewModel(saved, probe)
        model.state.test {
            awaitItem()
            model.edit("https://example.invalid")
            awaitItem()
            model.check()
            assertTrue(awaitItem().checking)
            runCurrent()
            model.cancel()
            assertFalse(awaitItem().checking)
            assertEquals(
                "https://example.invalid",
                ServerCheckViewModel(saved, probe).state.value.address,
            )
        }
    }

    @Test
    fun deniedResponseCanBeRetried() = runTest {
        val probe = mockk<ServerProbe>()
        coEvery { probe.check(any()) } coAnswers
            {
                kotlinx.coroutines.delay(1)
                throw ProbeException(ProbeError.DENIED)
            }
        val model =
            ServerCheckViewModel(
                SavedStateHandle(mapOf("address" to "https://example.invalid")),
                probe,
            )
        model.state.test {
            awaitItem()
            model.check()
            assertTrue(awaitItem().checking)
            assertEquals(ProbeError.DENIED, awaitItem().error)
            model.check()
            assertTrue(awaitItem().checking)
            assertEquals(ProbeError.DENIED, awaitItem().error)
            coVerify(exactly = 2) { probe.check(any()) }
        }
    }
}
