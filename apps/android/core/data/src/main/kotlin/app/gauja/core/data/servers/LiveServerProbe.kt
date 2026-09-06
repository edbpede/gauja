// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.data.servers

import android.util.Log
import app.gauja.core.api.apis.PublicApi
import app.gauja.core.api.apis.SettingsApi
import app.gauja.core.common.IoDispatcher
import app.gauja.core.common.ProbeError
import app.gauja.core.common.ProbeException
import app.gauja.core.model.ServerAddress
import app.gauja.core.model.ServerSnapshot
import app.gauja.core.network.ProbeTransport
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal class LiveServerProbe
@Inject
constructor(
    private val transport: ProbeTransport,
    @IoDispatcher private val io: CoroutineDispatcher,
    private val json: Json,
) : ServerProbe {
    override suspend fun check(address: ServerAddress): ServerSnapshot =
        withContext(io) {
            val client = transport.create(address) { Log.e("Gauja", "EGRESS_REJECTED") }
            try {
                val retrofit =
                    Retrofit.Builder()
                        .baseUrl(address.apiBase)
                        .client(client)
                        .addConverterFactory(
                            json.asConverterFactory("application/json".toMediaType())
                        )
                        .build()
                val status = retrofit.create(PublicApi::class.java).getStatus(false).checkedBody()
                val settings =
                    retrofit.create(SettingsApi::class.java).getSettingsPublic().checkedBody()
                mapServer(address, status, settings)
            } catch (error: CancellationException) {
                throw error
            } catch (error: ProbeException) {
                throw error
            } catch (_: SSLException) {
                throw ProbeException(ProbeError.TLS)
            } catch (_: UnknownHostException) {
                throw ProbeException(ProbeError.OFFLINE)
            } catch (_: ConnectException) {
                throw ProbeException(ProbeError.OFFLINE)
            } catch (_: SerializationException) {
                throw ProbeException(ProbeError.RESPONSE)
            } catch (_: java.io.IOException) {
                throw ProbeException(ProbeError.NETWORK)
            } finally {
                client.dispatcher.cancelAll()
                client.connectionPool.evictAll()
                client.dispatcher.executorService.shutdown()
            }
        }
}

private fun <T> Response<T>.checkedBody(): T {
    if (!isSuccessful) {
        errorBody()?.close()
        throw ProbeException(
            when (code()) {
                401,
                403 -> ProbeError.DENIED
                in 300..399 -> ProbeError.REDIRECT
                in 500..599 -> ProbeError.SERVER
                else -> ProbeError.RESPONSE
            }
        )
    }
    return body() ?: throw ProbeException(ProbeError.RESPONSE)
}
