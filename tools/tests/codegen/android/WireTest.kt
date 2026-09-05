// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import app.gauja.core.api.models.MediaInfo
import app.gauja.core.api.models.PostAuthLocalRequest
import app.gauja.core.api.models.WatchlistRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WireTest {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    @Test fun optionalAndUnknownFieldsDecode() {
        val media = json.decodeFromString<MediaInfo>("""{"status":99,"newField":true}""")
        assertEquals(99, media.status)
        assertEquals(null, media.tmdbId)
    }

    @Test fun unknownWireEnumSurvivesForDomainMapping() {
        val value = json.decodeFromString<WatchlistRequest>("""{"tmdbId":12,"mediaType":"future"}""")
        assertEquals("future", value.mediaType)
    }

    @Test fun authEncodesButDescriptionIsRedacted() {
        val input = PostAuthLocalRequest(email = "test@example.invalid", password = "synthetic-password")
        assertFalse(input.toString().contains("synthetic-password"))
        assertTrue(json.encodeToString(input).contains("synthetic-password"))
    }
}
