---
type: "agent_requested"
description: "Gauja Android decisions and pitfalls: Kotlin, Compose Adaptive, Navigation 3 and Hilt"
---
# Android project decisions

Build a native Compose app with Material 3 Adaptive, Navigation 3 and Hilt/KSP. Preserve **minSdk 30**; newer platform APIs require guards. Planned compileSdk/targetSdk is 37. Exact application dependencies belong in `apps/android/gradle/libs.versions.toml` when Phase 3 creates the build; the initial selection is in [plan §3.2](../../docs/gauja-implementation-plan.md#32-android-skeleton-appsandroid). Do not duplicate a version catalog in these rules.

Current generated-client compilation is configured in the [smoke manifest](../../tools/codegen/android/smoke/build.gradle.kts.in) and its [lock](../../tools/codegen/android/smoke/gradle.lockfile); generator pins are in [versions.env](../../tools/codegen/versions.env). Keep that independent compile check until a working API module replaces it.

## Build and boundaries

- The planned AGP 9 build uses built-in Kotlin. Do not apply `org.jetbrains.kotlin.android` to Android modules; Kotlin Compose/serialization plugin versions belong in the root build/catalog. Pure JVM tooling is a separate case.
- Use KSP for Hilt and Room, without kapt. Preserve the planned Hilt selection that avoids the previously identified 2.59.0 ComponentTreeDeps failure.
- Use Gradle convention plugins when multiple working modules need the same configuration. Do not scaffold the full future module map or a catalog of unused dependencies.
- Follow [modularity](modularity.md) and [API boundaries](api-contract.md): Data owns generated DTO mapping; UI consumes domain values. Pure models have no Android imports.

## UI and navigation

- Hoist immutable screen state; expose it through StateFlow and collect with `collectAsStateWithLifecycle()`. Keep renderers separate in responsibility from state owners. Use stable keys and immutable collections where state stability requires them.
- `Modifier` is the first optional parameter, defaults to `Modifier`, and is applied once to the root. Modifier order changes layout and hit regions.
- Keep side effects outside composition bodies. Key effects and remembered values by their dependencies; cancel work and dispose listeners with their owners.
- Use `currentWindowAdaptiveInfo()` and window-size-class breakpoints. Use native navigation-suite and list/detail or supporting-pane scaffolds/scenes; do not build a raw-width phone/tablet switch. A detail pane does not draw a redundant full-screen back affordance.
- Use serializable `NavKey` routes, `rememberNavBackStack`, `NavDisplay` and entry providers. Do not import Navigation 2’s NavController, NavHost or string routes.
- Preserve per-entry saved state and ViewModel ownership with the saveable-state and ViewModel-store decorators. Respect the pinned API’s requested back-pop count and process-death restoration; a singleton mutable list alone does not restore navigation.
- Keep feature entry points small; use Hilt multibindings for entry installers when features need them. Pass route IDs explicitly into the state owner; verify route decoding against the pinned Navigation 3 API.
- Represent one-off navigation/snackbar events with explicit consumption so rotation does not replay them. Preview and test relevant states at phone/tablet widths.

## Data and concurrency

- Constructor-inject collaborators. Use `@Binds` for owned implementations, `@Provides` for external types, and assisted injection only when runtime inputs cannot be route identifiers.
- Repository suspend functions are main-safe; inject dispatchers and clocks for deterministic tests. Use lifecycle-owned scopes and honor cancellation.
- Room is the relational persistence choice, with coroutine reads/writes and an explicit SQLite driver. Use DataStore for preferences; secret storage follows PRD §10, never an ordinary settings field.
- Retrofit/OkHttp uses kotlinx.serialization. Ignore unknown keys, preserve required fields and map unknown wire values to explicit domain Unknown variants. Do not add a BODY logger or global authenticated client.
- Coil 3 image loading needs its network artifact; size requests to the rendered image and cancel obsolete loads. Domain, persistence and generated wire types remain distinct.

## Verification and references

Use JUnit4, coroutines-test and Turbine for JVM behavior; MockK/fakes for collaborators. Inject a test Main dispatcher and advance queued work deliberately. Compose tests use semantics; Robolectric covers suitable JVM UI cases, Hilt instrumentation uses HiltTestApplication and test bindings. Verify navigation state and restoration with working consumers; no empty stub suites.

ktfmt owns formatting; detekt with Compose rules and Android Lint own correctness checks. Length/complexity remain warnings. Commands and versions belong in the real Gradle configuration; generated paths are excluded as defined by [prek.toml](../../prek.toml).

API examples and setup belong upstream: [Compose state](https://developer.android.com/develop/ui/compose/state), [adaptive layouts](https://developer.android.com/develop/ui/compose/layouts/adaptive), [Navigation 3](https://developer.android.com/guide/navigation/navigation-3), [Hilt](https://developer.android.com/training/dependency-injection/hilt-android), and [Room](https://developer.android.com/training/data-storage/room). Check the pinned dependency’s API before copying an example.
