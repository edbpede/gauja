// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Foundation
import Testing

@Test func appDoesNotRegisterBackgroundWorkOrThirdPartyQueries() {
    let info = Bundle.main.infoDictionary ?? [:]
    #expect(info["UIBackgroundModes"] == nil)
    #expect(info["LSApplicationQueriesSchemes"] == nil)
    #expect(info["NSUserTrackingUsageDescription"] == nil)
    #expect(info["MinimumOSVersion"] as? String == "18.0")
}
