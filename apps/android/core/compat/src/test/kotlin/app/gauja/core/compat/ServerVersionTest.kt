// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.compat

import app.gauja.core.model.Compatibility
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ServerVersionTest {
    @Test
    fun rejectsMalformedVersion() {
        listOf("", "3.4", "03.4.1", "3.4.1/bogus", "999999999999999999999.4.1").forEach {
            assertNull(ServerVersion.parse(it))
        }
    }

    @Test
    fun classifiesSupportedAndUnknownReleases() {
        assertEquals(Compatibility.TESTED, ServerVersion.parse("3.4.1")?.compatibility())
        assertEquals(Compatibility.TOO_OLD, ServerVersion.parse("3.4.0")?.compatibility())
        assertEquals(Compatibility.UNTESTED, ServerVersion.parse("3.5.0")?.compatibility())
        assertEquals(
            Compatibility.UNTESTED,
            ServerVersion.parse("3.4.1-develop+abc")?.compatibility(),
        )
    }
}
