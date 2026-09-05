// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Navigation
import SwiftUI

public typealias ServersRoute = ServerRoute

public struct ServersView: View {
    @State private var model = ServerCheckModel()
    @Environment(\.scenePhase) private var scenePhase

    public init() {}

    public var body: some View {
        ServerCheckScreen(state: model.state, onEdit: model.edit, onCheck: model.check, onCancel: model.cancel)
            .onChange(of: scenePhase) { _, phase in
                if phase == .active { model.foreground() } else { model.cancel() }
            }
            .onDisappear { model.cancel() }
    }
}
