// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Common
import Testing

@testable import Servers

@Test func invalidAddressDoesNotStartRequest() {
    let model = ServerCheckModel()
    model.edit("ftp://invalid")
    model.check()
    #expect(model.state.error == .address)
    #expect(!model.state.checking)
}
