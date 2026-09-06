// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Common
import Foundation
import Model
import Network
import OpenAPIRuntime
internal import SeerrAPI

struct LiveServerProbe: Sendable {
    @concurrent
    func check(_ address: ServerAddress) async throws -> ServerSnapshot {
        let transport = ProbeTransport(address: address)
        defer { transport.close() }
        let client = Client(serverURL: address.apiBase, transport: transport)
        do {
            let statusOutput = try await client.getStatus(.init(query: .init(checkUpdateAvailable: false)))
            guard case .ok(let statusBody) = statusOutput else {
                throw statusOutput.probeError
            }
            let settingsOutput = try await client.getSettingsPublic()
            guard case .ok(let settingsBody) = settingsOutput else {
                throw settingsOutput.probeError
            }
            return mapServer(address, try statusBody.body.json, try settingsBody.body.json)
        } catch is CancellationError {
            throw CancellationError()
        } catch let error as ProbeError {
            throw error
        } catch {
            if Task.isCancelled { throw CancellationError() }
            // Generated client errors wrap transport/decoding errors; never render their descriptions.
            throw classify(error)
        }
    }
}

private func classify(_ error: any Error) -> ProbeError {
    if let clientError = error as? ClientError { return classify(clientError.underlyingError) }
    if let probeError = error as? ProbeError { return probeError }
    if let urlError = error as? URLError {
        switch urlError.code {
        case .notConnectedToInternet, .cannotFindHost, .cannotConnectToHost: return .offline
        case .serverCertificateUntrusted, .serverCertificateHasBadDate, .secureConnectionFailed,
            .serverCertificateHasUnknownRoot, .serverCertificateNotYetValid:
            return .tls
        default: return .network
        }
    }
    return .response
}

private func statusError(_ code: Int) -> ProbeError {
    switch code {
    case 401, 403: .denied
    case 300..<400: .redirect
    case 500..<600: .server
    default: .response
    }
}

extension Operations.getStatus.Output {
    fileprivate var probeError: ProbeError {
        switch self {
        case .undocumented(let code, _): statusError(code)
        default: .response
        }
    }
}

extension Operations.getSettingsPublic.Output {
    fileprivate var probeError: ProbeError {
        switch self {
        case .undocumented(let code, _): statusError(code)
        default: .response
        }
    }
}
