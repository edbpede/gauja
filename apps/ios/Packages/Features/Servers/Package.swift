// swift-tools-version: 6.2
// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import PackageDescription

let package = Package(
    name: "Servers",
    platforms: [.iOS(.v18), .macOS(.v15)],
    products: [.library(name: "Servers", targets: ["Servers"])],
    dependencies: [
        .package(path: "../../Data"),
        .package(path: "../../Model"),
        .package(path: "../../Common"),
        .package(path: "../../DesignSystem"),
        .package(path: "../../Navigation"),
        .package(url: "https://github.com/pointfreeco/swift-dependencies", exact: "1.17.1"),
    ],
    targets: [
        .target(
            name: "Servers",
            dependencies: [
                "Data", "Model", "Common", "DesignSystem", "Navigation",
                .product(name: "Dependencies", package: "swift-dependencies"),
            ],
            swiftSettings: [
                .defaultIsolation(MainActor.self),
                .enableUpcomingFeature("NonisolatedNonsendingByDefault"),
                .enableUpcomingFeature("InferIsolatedConformances"),
            ]),
        .testTarget(
            name: "ServersTests", dependencies: ["Servers"], swiftSettings: [.defaultIsolation(MainActor.self)]),
    ],
    swiftLanguageModes: [.v6]
)
