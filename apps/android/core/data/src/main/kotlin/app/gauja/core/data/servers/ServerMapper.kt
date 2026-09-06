// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.core.data.servers

import app.gauja.core.api.models.GetStatus200Response
import app.gauja.core.api.models.PublicSettings
import app.gauja.core.compat.ServerVersion
import app.gauja.core.model.Compatibility
import app.gauja.core.model.MediaServerType
import app.gauja.core.model.ServerAddress
import app.gauja.core.model.ServerSnapshot

internal fun mapServer(
    address: ServerAddress,
    status: GetStatus200Response,
    settings: PublicSettings,
): ServerSnapshot =
    ServerSnapshot(
        address,
        status.version,
        settings.applicationTitle,
        settings.initialized,
        status.restartRequired,
        settings.localLogin,
        settings.mediaServerLogin,
        when (settings.mediaServerType) {
            1.0 -> MediaServerType.PLEX
            2.0 -> MediaServerType.JELLYFIN
            3.0 -> MediaServerType.EMBY
            4.0 -> MediaServerType.NOT_CONFIGURED
            else -> MediaServerType.UNKNOWN
        },
        ServerVersion.parse(status.version)?.compatibility() ?: Compatibility.UNKNOWN,
    )
