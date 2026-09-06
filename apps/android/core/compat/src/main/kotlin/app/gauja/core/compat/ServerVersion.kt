// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.compat

import app.gauja.core.model.Compatibility

// The supported baseline is the recorded release in api/README.md; future versions are not tested
// claims.
data class ServerVersion(val major: Int, val minor: Int, val patch: Int, val suffix: String) :
    Comparable<ServerVersion> {
    override fun compareTo(other: ServerVersion): Int =
        compareValuesBy(this, other, { it.major }, { it.minor }, { it.patch })

    fun compatibility(): Compatibility {
        val baseline = ServerVersion(3, 4, 1, "")
        return when {
            this < baseline -> Compatibility.TOO_OLD
            this > baseline || suffix.isNotEmpty() -> Compatibility.UNTESTED
            else -> Compatibility.TESTED
        }
    }

    companion object {
        fun parse(raw: String?): ServerVersion? {
            val match =
                Regex(
                        "^v?(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)([-+][0-9A-Za-z.+-]+)?$"
                    )
                    .matchEntire(raw.orEmpty()) ?: return null
            return ServerVersion(
                match.groupValues[1].toIntOrNull() ?: return null,
                match.groupValues[2].toIntOrNull() ?: return null,
                match.groupValues[3].toIntOrNull() ?: return null,
                match.groupValues[4],
            )
        }
    }
}
