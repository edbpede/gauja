---
type: "agent_requested"
description: "Gauja iOS decisions and pitfalls: Swift 6, iOS 18, SwiftUI, Observation and SwiftData"
---
# iOS project decisions

Use Swift 6 language mode with complete strict concurrency, SwiftUI, Observation, SwiftData, SwiftPM and XcodeGen. Preserve the **iOS/iPadOS 18 deployment floor** while building with the selected Xcode 26 / iOS 26 SDK toolchain. Toolchain, language mode and deployment target are separate settings; do not raise the floor to use a newer API.

[Plan §3.3](../../docs/gauja-implementation-plan.md#33-ios-skeleton-appsios) records the initial application build selection. Exact settings belong in `apps/ios/project.yml` and real `Package.swift` manifests when those consumers land. Current generation uses the [tooling package](../../tools/codegen/ios/Package.swift), [generator pins](../../tools/codegen/versions.env) and [smoke manifest](../../tools/codegen/ios/smoke/Package.swift.in). Do not reproduce sample applications or dependency catalogs here.

## Isolation and ownership

- UI targets use approachable concurrency and default MainActor isolation. Networking, domain, generated API and persistence targets remain nonisolated by default. Declare package isolation deliberately, with the selected `NonisolatedNonsendingByDefault` and `InferIsolatedConformances` flags where appropriate.
- Under caller-actor async semantics, `await` alone does not move CPU work off the main actor. Use `@concurrent` deliberately for heavy decoding/parsing with Sendable inputs and outputs.
- Do not use `@unchecked Sendable` or `nonisolated(unsafe)` to silence diagnostics. Fix ownership with actors and Sendable values.
- Prefer structured concurrency. Tie tasks to an owner, honor cancellation, and let view disappearance cancel in-flight work. Keep destination builders free of side effects and safe to evaluate more than once.
- Follow [modularity](modularity.md) and [API boundaries](api-contract.md). Data imports generated DTOs and exposes domain values. Keep the supported generator file structure; never manually split generated namespace files.

## State, navigation and persistence

- Use `@Observable` models. An owner uses `@State`; consumers use `@Bindable` for object bindings, `@Binding` for values, or `@Environment` for shared injected models. Mark services/caches `@ObservationIgnored` as needed. Do not mix Observation with ObservableObject/Published/StateObject.
- Use value-based NavigationStack/NavigationPath routes and NavigationSplitView for appropriate regular-width layouts. Centralize destination mapping and preserve selection/unsaved work when resizing. Feature public entry views/routes stay small.
- SwiftData model objects and ModelContext never cross actor boundaries. Pass PersistentIdentifier values and refetch inside a ModelActor; the container may be shared. Set relationship inverse and delete rules explicitly.
- Add indexes only for measured fetch needs. Schema changes, including indexes/uniqueness, require migration and existing-store tests; use in-memory stores for isolated behavior tests. Plan VersionedSchema/SchemaMigrationPlan with the first persisted schema.
- Guard post-iOS-18 APIs, including Observations, SwiftData inheritance and typed notification messages, and retain an iOS 18 path. Verify availability against the SDK rather than assuming the new compiler changes runtime support.

## Build and verification

- Generate the Xcode project from `project.yml`; never commit `.xcodeproj`. XcodeGen `options.deploymentTarget` is a platform map, target `deploymentTarget` a string. Do not mix flat settings with `base`/`configs`; use actual Xcode build settings, not a `swiftSettings` key in XcodeGen.
- Swift Testing is the unit-test default (`@Test`, `@Suite`, `#expect`, `#require`). XCTest remains for UI/performance. Tests run in parallel: isolate stores and clocks, serialize only shared resources, use confirmations for callback expectations and track known failures explicitly.
- Add packages and tests with working consumers; folders are the default within Data and Settings. Retire smoke manifests/locks only once the real API target independently compiles and passes equivalent serialization/redaction tests.
- Toolchain swift-format owns layout; pinned SwiftLint owns conventions and correctness. Run formatting before lint. Generated code is excluded, and length/complexity remain advisory. Current tool configuration is in [swift-format](../../tools/codegen/ios/.swift-format) and [SwiftLint](../../tools/codegen/ios/.swiftlint.yml); app settings land with the app.

Examples and API details belong upstream: [Observation](https://developer.apple.com/documentation/observation), [Swift concurrency](https://docs.swift.org/swift-book/documentation/the-swift-programming-language/concurrency/), [SwiftData](https://developer.apple.com/documentation/swiftdata), [Swift Testing](https://developer.apple.com/documentation/testing), and [XcodeGen configuration](https://github.com/yonaskolb/XcodeGen/blob/master/Docs/ProjectSpec.md). Check selected toolchain behavior and availability when implementing.
