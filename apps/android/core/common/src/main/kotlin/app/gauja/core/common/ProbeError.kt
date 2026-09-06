// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.common

enum class ProbeError {
    ADDRESS,
    OFFLINE,
    TLS,
    DENIED,
    REDIRECT,
    RESPONSE,
    SERVER,
    NETWORK,
}

class ProbeException(val reason: ProbeError) : Exception(reason.name)
