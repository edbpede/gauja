// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import DesignSystem
import SwiftUI

@main
struct GaujaApp: App {
    var body: some Scene {
        WindowGroup { GaujaTheme { RootNavigation() } }
    }
}
