// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.network

import app.gauja.core.model.ServerAddress
import java.io.IOException
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ProbeTransportTest {
    @Test
    fun foreignOriginFailsBeforeSending() {
        MockWebServer().use { server ->
            server.start()
            val address = requireNotNull(ServerAddress.parse(server.url("/").toString()))
            var rejected = false
            val client = ProbeTransport().create(address) { rejected = true }
            try {
                assertThrows(IOException::class.java) {
                    client
                        .newCall(Request.Builder().url("https://example.invalid").build())
                        .execute()
                }
                assertTrue(rejected)
                assertEquals(0, server.requestCount)
            } finally {
                client.dispatcher.executorService.shutdown()
                client.connectionPool.evictAll()
            }
        }
    }

    @Test
    fun redirectsAndCookiesAreNotReplayed() {
        MockWebServer().use { server ->
            server.start()
            server.enqueue(
                MockResponse.Builder()
                    .code(302)
                    .addHeader("Location", "https://example.invalid")
                    .addHeader("Set-Cookie", "synthetic=test")
                    .build()
            )
            server.enqueue(MockResponse.Builder().body("{}").build())
            val address = requireNotNull(ServerAddress.parse(server.url("/").toString()))
            val client = ProbeTransport().create(address) { error("Unexpected egress") }
            try {
                val request = Request.Builder().url(server.url("/")).build()
                client.newCall(request).execute().use { assertEquals(302, it.code) }
                client.newCall(request).execute().close()
                server.takeRequest()
                assertNull(server.takeRequest().headers["Cookie"])
                assertEquals(2, server.requestCount)
            } finally {
                client.dispatcher.executorService.shutdown()
                client.connectionPool.evictAll()
            }
        }
    }
}
