// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Testing

@testable import Model

@Test(arguments: [
    "", "ftp://example.com", "https://user:pass@example.com", "https://example.com?q=x",
    "https://example.com#fragment", "https://", "https://example.com:0", "https://example.com/a/../b",
    "https://example.com/a/%2e%2e/b",
    "https://example.com/a%2fb", "https://example.com/a%5cb",
])
func invalidAddress(_ text: String) { #expect(ServerAddress(text) == nil) }

@Test func proxyPrefixSurvives() throws {
    let address = try #require(ServerAddress("  EXAMPLE.com:5055/seerr/  "))
    #expect(address.url.absoluteString == "https://example.com:5055/seerr")
    #expect(address.apiBase.absoluteString == "https://example.com:5055/seerr/api/v1")
    #expect(!address.isPlainHTTP)
    #expect(address.description == "[SERVER]")
}
