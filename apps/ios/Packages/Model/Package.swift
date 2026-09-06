// swift-tools-version: 6.2
// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import PackageDescription

let package = Package(
    name: "Model",
    platforms: [.iOS(.v18), .macOS(.v15)],
    products: [.library(name: "Model", targets: ["Model"])],
    dependencies: [],
    targets: [
        .target(
            name: "Model", dependencies: [],
            swiftSettings: [
                .enableUpcomingFeature("NonisolatedNonsendingByDefault"),
                .enableUpcomingFeature("InferIsolatedConformances"),
            ]),
        .testTarget(name: "ModelTests", dependencies: ["Model"]),
    ],
    swiftLanguageModes: [.v6]
)
