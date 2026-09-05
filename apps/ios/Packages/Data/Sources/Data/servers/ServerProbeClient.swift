// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Common
import Dependencies
import Model

public struct ServerProbeClient: Sendable {
    public var check: @Sendable (ServerAddress) async throws -> ServerSnapshot

    public init(check: @escaping @Sendable (ServerAddress) async throws -> ServerSnapshot) {
        self.check = check
    }
}

extension ServerProbeClient: DependencyKey {
    public static let liveValue = Self(check: { try await LiveServerProbe().check($0) })
    public static let testValue = Self(check: { _ in throw ProbeError.network })
}

extension DependencyValues {
    public var serverProbe: ServerProbeClient {
        get { self[ServerProbeClient.self] }
        set { self[ServerProbeClient.self] = newValue }
    }
}
