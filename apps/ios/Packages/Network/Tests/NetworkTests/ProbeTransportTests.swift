// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Common
import Foundation
import HTTPTypes
import Model
import Testing

@testable import Network

@Test func forbiddenOriginFailsBeforeSending() async throws {
    let address = try #require(ServerAddress("https://example.invalid"))
    let transport = ProbeTransport(address: address)
    defer { transport.close() }
    let other = try #require(URL(string: "https://other.invalid"))
    await #expect(throws: ProbeError.redirect) {
        _ = try await transport.send(
            HTTPRequest(method: .get, scheme: "https", authority: "other.invalid", path: "/"),
            body: nil, baseURL: other, operationID: "forbidden")
    }
}

@Test(.enabled(if: ProcessInfo.processInfo.environment["GAUJA_EGRESS_SERVER"] != nil))
func redirectsAndCookiesAreNotReplayed() async throws {
    let base = try #require(ProcessInfo.processInfo.environment["GAUJA_EGRESS_SERVER"])
    let address = try #require(ServerAddress(base))
    let transport = ProbeTransport(address: address)
    defer { transport.close() }
    let (response, _) = try await transport.send(
        HTTPRequest(method: .get, scheme: nil, authority: nil, path: "/redirect"),
        body: nil, baseURL: address.url, operationID: "redirect")
    #expect(response.status.code == 302)
    let (_, body) = try await transport.send(
        HTTPRequest(method: .get, scheme: nil, authority: nil, path: "/echo"),
        body: nil, baseURL: address.url, operationID: "echo")
    let text = try await String(collecting: #require(body), upTo: 4096)
    #expect(text == "no-cookie")
}
