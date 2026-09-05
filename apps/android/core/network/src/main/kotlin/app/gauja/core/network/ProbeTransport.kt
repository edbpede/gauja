// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.network

import app.gauja.core.model.ServerAddress
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import okhttp3.CookieJar
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class ProbeTransport @Inject constructor() {
    fun create(address: ServerAddress, onViolation: () -> Unit): OkHttpClient {
        val origin = address.value.toHttpUrl()
        return OkHttpClient.Builder()
            .cookieJar(CookieJar.NO_COOKIES)
            .cache(null)
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(false)
            .callTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(
                Interceptor { chain ->
                    val target = chain.request().url
                    if (
                        target.scheme != origin.scheme ||
                            target.host != origin.host ||
                            target.port != origin.port
                    ) {
                        onViolation()
                        throw java.io.IOException("EGRESS_REJECTED")
                    }
                    chain.proceed(chain.request())
                }
            )
            .build()
    }
}
