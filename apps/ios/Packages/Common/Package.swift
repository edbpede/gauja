// swift-tools-version: 6.2
// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import PackageDescription

let package = Package(
    name: "Common",
    platforms: [.iOS(.v18), .macOS(.v15)],
    products: [.library(name: "Common", targets: ["Common"])],
    dependencies: [
        .package(path: "../Model")
    ],
    targets: [
        .target(
            name: "Common", dependencies: ["Model"],
            swiftSettings: [
                .enableUpcomingFeature("NonisolatedNonsendingByDefault"),
                .enableUpcomingFeature("InferIsolatedConformances"),
            ])
    ],
    swiftLanguageModes: [.v6]
)
