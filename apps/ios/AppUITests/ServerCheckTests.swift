// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import XCTest

nonisolated final class ServerCheckTests: XCTestCase {
    @MainActor func testAddressValidation() {
        let app = XCUIApplication()
        app.launch()
        let address = app.textFields["server-address"]
        XCTAssertTrue(address.waitForExistence(timeout: 10))
        address.tap()
        address.typeText("ftp://invalid")
        app.buttons["Check server"].tap()
        XCTAssertTrue(app.staticTexts["server-error"].waitForExistence(timeout: 3))
    }
}
