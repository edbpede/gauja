// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Foundation
import SeerrAPI
import Testing

@Test func optionalAndUnknownFieldsDecode() throws {
    let data = Data(#"{"status":99,"newField":true}"#.utf8)
    let media = try JSONDecoder().decode(Components.Schemas.MediaInfo.self, from: data)
    #expect(media.status == 99)
    #expect(media.tmdbId == nil)
}

@Test func unknownWireEnumSurvivesForDomainMapping() throws {
    let data = Data(#"{"tmdbId":12,"mediaType":"future"}"#.utf8)
    let value = try JSONDecoder().decode(Components.Schemas.WatchlistRequest.self, from: data)
    #expect(value.mediaType == "future")
}

@Test func authEncodesButDescriptionsAreRedacted() throws {
    let input = Operations.postAuthLocal.Input.Body.jsonPayload(
        email: "test@example.invalid", password: "synthetic-password")
    #expect(!String(describing: input).contains("synthetic-password"))
    #expect(!String(reflecting: input).contains("synthetic-password"))
    #expect(Mirror(reflecting: input).children.count == 0)
    let data = try JSONEncoder().encode(input)
    let encoded = try #require(String(bytes: data, encoding: .utf8))
    #expect(encoded.contains("synthetic-password"))
}

@Test func notificationCredentialsAreRedacted() throws {
    let data = Data(#"{"authHeader":"synthetic-webhook-credential"}"#.utf8)
    let value = try JSONDecoder().decode(Components.Schemas.WebhookSettings.optionsPayload.self, from: data)
    #expect(!String(reflecting: value).contains("synthetic-webhook-credential"))
}
