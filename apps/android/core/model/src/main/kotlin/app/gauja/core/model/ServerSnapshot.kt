// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.model

data class ServerSnapshot(
    val address: ServerAddress,
    val version: String?,
    val title: String?,
    val initialized: Boolean?,
    val restartRequired: Boolean?,
    val localLogin: Boolean?,
    val mediaServerLogin: Boolean?,
    val mediaServerType: MediaServerType,
    val compatibility: Compatibility,
)

// Seerr server/constants/index.ts at 69f73a6f1486fdb51b8ddae9a94a8dfb629f461c.
enum class MediaServerType {
    PLEX,
    JELLYFIN,
    EMBY,
    NOT_CONFIGURED,
    UNKNOWN,
}

enum class Compatibility {
    TESTED,
    TOO_OLD,
    UNTESTED,
    UNKNOWN,
}
