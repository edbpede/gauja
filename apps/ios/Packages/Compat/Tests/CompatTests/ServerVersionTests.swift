// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Testing

@testable import Compat

@Test(arguments: ["", "3.4", "03.4.1", "3.4.1/bogus", "999999999999999999999.4.1"])
func invalidVersion(_ value: String) { #expect(ServerVersion(value) == nil) }

@Test func baselineAndUntestedVersions() {
    #expect(ServerVersion("3.4.1")?.compatibility == .tested)
    #expect(ServerVersion("v3.4.1")?.compatibility == .tested)
    #expect(ServerVersion("3.4.0")?.compatibility == .tooOld)
    #expect(ServerVersion("3.5.0")?.compatibility == .untested)
    #expect(ServerVersion("3.4.1-develop+abc")?.compatibility == .untested)
}
