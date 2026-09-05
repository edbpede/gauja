// swift-tools-version: 6.2
// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import PackageDescription

let package = Package(
    name: "Navigation",
    platforms: [.iOS(.v18), .macOS(.v15)],
    products: [.library(name: "Navigation", targets: ["Navigation"])],
    dependencies: [],
    targets: [
        .target(
            name: "Navigation", dependencies: [],
            swiftSettings: [
                .defaultIsolation(MainActor.self),
                .enableUpcomingFeature("NonisolatedNonsendingByDefault"),
                .enableUpcomingFeature("InferIsolatedConformances"),
            ])
    ],
    swiftLanguageModes: [.v6]
)
