// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import SwiftUI

public struct GaujaTheme<Content: View>: View {
    private let content: Content
    private let scheme: ColorScheme

    public init(scheme: ColorScheme = .dark, @ViewBuilder content: () -> Content) {
        self.scheme = scheme
        self.content = content()
    }

    public var body: some View {
        content.tint(.gaujaPrimary(scheme))
            .background(Color.gaujaBackground(scheme))
            .preferredColorScheme(scheme)
    }
}
