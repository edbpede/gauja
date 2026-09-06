// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Common
import Foundation
import HTTPTypes
import Model
import OSLog
import OpenAPIRuntime
import OpenAPIURLSession

public struct ProbeTransport: ClientTransport {
    private let address: ServerAddress
    private let session: URLSession
    private let transport: URLSessionTransport

    public init(address: ServerAddress) {
        self.address = address
        let configuration = URLSessionConfiguration.ephemeral
        configuration.httpCookieStorage = nil
        configuration.httpShouldSetCookies = false
        configuration.urlCache = nil
        configuration.timeoutIntervalForRequest = 20
        configuration.timeoutIntervalForResource = 20
        let session = URLSession(configuration: configuration, delegate: RejectRedirects(), delegateQueue: nil)
        self.session = session
        self.transport = URLSessionTransport(configuration: .init(session: session))
    }

    public func close() { session.invalidateAndCancel() }

    public func send(_ request: HTTPRequest, body: HTTPBody?, baseURL: URL, operationID: String) async throws
        -> (HTTPResponse, HTTPBody?)
    {
        guard baseURL.scheme == address.url.scheme, baseURL.host == address.url.host,
            baseURL.port == address.url.port
        else {
            Logger(subsystem: "app.gauja", category: "network").error("EGRESS_REJECTED")
            throw ProbeError.redirect
        }
        return try await transport.send(request, body: body, baseURL: baseURL, operationID: operationID)
    }
}

private final class RejectRedirects: NSObject, URLSessionTaskDelegate {
    func urlSession(
        _ session: URLSession, task: URLSessionTask,
        willPerformHTTPRedirection response: HTTPURLResponse, newRequest request: URLRequest,
        completionHandler: @escaping @Sendable (URLRequest?) -> Void
    ) {
        completionHandler(nil)
    }
}
