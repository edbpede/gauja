// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Compat
import Model
internal import SeerrAPI

func mapServer(
    _ address: ServerAddress, _ status: Operations.getStatus.Output.Ok.Body.jsonPayload,
    _ settings: Components.Schemas.PublicSettings
) -> ServerSnapshot {
    let mediaServerType: MediaServerType
    switch settings.mediaServerType {
    case 1: mediaServerType = .plex
    case 2: mediaServerType = .jellyfin
    case 3: mediaServerType = .emby
    case 4: mediaServerType = .notConfigured
    default: mediaServerType = .unknown
    }
    return ServerSnapshot(
        address: address, version: status.version, title: settings.applicationTitle,
        initialized: settings.initialized, restartRequired: status.restartRequired,
        localLogin: settings.localLogin, mediaServerLogin: settings.mediaServerLogin,
        mediaServerType: mediaServerType,
        compatibility: ServerVersion(status.version)?.compatibility ?? .unknown)
}
