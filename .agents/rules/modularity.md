---
type: "agent_requested"
description: "Gauja modularity doctrine: one purpose per file and module, dependency direction, generated-code isolation (both platforms)"
---

# Modularity

This file expands PRD §12.2 and §12.3 into rules that apply to every line under `apps/android/` and `apps/ios/`. It is normative: violations are review blockers, and the dependency graph is enforced mechanically. On language, framework and library usage the platform rule files (`kotlin-2_4-android-app.md`, `swift-6_3-ios-app.md`) govern; this file governs shape.

## One purpose per file

1. **One responsibility per file.** A file contains exactly one of: one type, one composable / view, one use case, one mapper, one DAO, one small family of tightly related extensions on one receiver. If a file needs a section comment to navigate, it is two files.
2. **No grab-bags.** No `Utils`, `Helpers`, `Extensions.kt`, `Constants`, `AppState`, `Misc`, `Common.swift`, or a `Repository` that knows more than one aggregate. Name the concern instead: `RedactingFormatter`, `IsoDurationParser`, `RequestRepository`.
3. **Names say what, folders say where.** A file name never repeats its path. `feature/requests/list/RequestListScreen.kt`, not `feature/requests/list/FeatureRequestsListScreen.kt`. The type inside is named like the file.
4. **Folders nest as deep as the domain.** `feature/settings/services/radarr/edit/` is right; forty siblings in `feature/settings/` is wrong. A flat folder with more than roughly a dozen files is a smell to fix in the same PR.
5. **Advisory size limits.** Files over ~300 lines and functions over ~40 lines produce lint *warnings* (detekt `LongMethod` / `LargeClass`, SwiftLint `file_length` / `function_body_length` at `warning`), never errors, and are never wired as blocking hooks. The doctrine is about purpose, not line counts.
6. **Tests mirror sources.** Every source folder has a sibling test folder with the same path; a test file is named for the unit it tests (`RequestMapperTest.kt`, `RequestMapperTests.swift`). Tool scripts follow the same rule under `tools/tests/`.

## Inside a module

- Feature modules use `ui/` (screens and state holders), `domain/` (use cases), `navigation/` (keys or routes), with nested subfolders per screen (`list/`, `detail/`, `edit/`).
- A screen is a stateless renderer plus a state owner (ViewModel / `@Observable` model) in separate files. Its UI state is one immutable type per screen, modelling all five states from the screen spec: loading, empty, error, offline, permission-denied.
- Every `when` / `switch` over an upstream-defined enum (`MediaStatus`, `MediaRequestStatus`, `IssueType`, `IssueStatus`, `MediaServerType`, `DiscoverSliderType`, permission bits) has an explicit unknown branch. Seerr adds values.
- Pure domain logic (`hasPermission`, `ServerVersion` parsing, `FeatureGate`, mappers, image-URL rewriting, deep-link parsing) lives in `core/model` / `core/common` / `Model` / `Common` with no platform imports, so it is unit-testable in isolation. Inject `Clock`, dispatchers and dependency keys; never read a global.

## Dependency direction

Arrows point in the only allowed direction. Anything else fails the module-graph check.

```
app ──► feature/*, core/ui, core/designsystem, core/navigation, core/data, core/common, core/compat, core/model, core/datastore (profile bootstrap only)
        feature/* ──► core/ui, core/designsystem, core/data, core/navigation, core/common, core/compat, core/model
                       core/data ──► core/api (generated), core/database, core/datastore, core/network, core/model, core/common, core/compat
                       core/network ──► core/common, core/model, core/datastore (secrets read)
                       core/ui ──► core/designsystem, core/model, core/common
                       core/api (generated) ──► nothing in-repo
                       core/model ──► nothing in-repo
                       core/testing ──► any core/* (test scope only)
```

### Android (`apps/android/`)

| Module | May depend on |
|---|---|
| `app` | every `feature/*`, `core/ui`, `core/designsystem`, `core/navigation`, `core/data`, `core/common`, `core/compat`, `core/model`, `core/datastore` (profile bootstrap only) |
| `feature/*` | `core/ui`, `core/designsystem`, `core/data`, `core/navigation`, `core/common`, `core/compat`, `core/model` |
| `core/data` | `core/api`, `core/database`, `core/datastore`, `core/network`, `core/model`, `core/common`, `core/compat` |
| `core/network` | `core/common`, `core/model`, `core/datastore` (secrets read) |
| `core/database`, `core/datastore` | `core/model`, `core/common` |
| `core/compat` | `core/model`, `core/common` |
| `core/ui` | `core/designsystem`, `core/model`, `core/common` |
| `core/designsystem`, `core/navigation`, `core/common` | `core/model` |
| `core/model`, `core/api` | nothing in-repo |
| `core/testing` | any `core/*`; consumed by tests only |

### iOS (`apps/ios/Packages/`)

| Package | May depend on |
|---|---|
| `App` target | every `Features/*`, `UI`, `DesignSystem`, `Navigation`, `Data`, `Common`, `Compat`, `Model`, `Persistence` (profile bootstrap only) |
| `Features/*` | `UI`, `DesignSystem`, `Data`, `Navigation`, `Common`, `Compat`, `Model` |
| `Data` | `SeerrAPI`, `Persistence`, `Network`, `Model`, `Common`, `Compat` |
| `Network` | `Common`, `Model`, `Persistence` (secrets read) |
| `Persistence`, `Compat` | `Model`, `Common` |
| `UI` | `DesignSystem`, `Model`, `Common` |
| `DesignSystem`, `Navigation`, `Common` | `Model` |
| `Model`, `SeerrAPI` | nothing in-repo (`SeerrAPI` depends only on the swift-openapi runtime) |
| `Testing` | any non-feature package; consumed by tests only |

Rules that follow from the tables:

- **`feature/*` modules never depend on each other.** Cross-feature navigation goes through `core/navigation` / `Navigation` keys; cross-feature data goes through `core/data` / `Data`.
- **Nothing depends on `app` / the `App` target.**
- **Generated DTOs never leave `core/api` / `SeerrAPI`.** `core/data` / `Data` wraps them in hand-written domain models with one mapper file per aggregate.
- Each feature package on iOS exposes exactly one public entry view and one public route type; everything else is `internal`. On Android the feature module exposes its `NavKey`s and its `EntryProviderInstaller`; screens are `internal`.
- Enforcement: Gradle `dependency-analysis` plus `tools/ci/check-module-graph.sh` on Android; SPM package boundaries plus `tools/ci/check-package-graph.sh` on iOS. Both scripts read the tables above and land with the skeletons in Phase 3.

## Generated code is isolated and never edited

- Generated clients and themes live only in the four paths listed in `prek.toml`'s `GENERATED` comment. They are excluded from formatters and linters, annotated in `REUSE.toml`, marked `linguist-generated` and verified byte-for-byte against the generator in CI.
- A change in generated output is always the result of running `tools/codegen/` or `tools/tokens/`; a diff there without a corresponding contract or token change is a defect.
- Hand-written code wraps generated code at the module boundary so spec churn stops at `core/data` / `Data`.

## Anti-patterns

| Wrong | Why | Right |
|---|---|---|
| `Utils.kt` / `Helpers.swift` | No reason to change; grows forever | A named type per concern |
| `feature/requests` imports `feature/media` | Couples features; breaks independent PRs | Navigate via `core/navigation`; share data via `core/data` |
| A generated DTO in a ViewModel signature | Spec churn leaks into UI | Domain model from `core/model` via a mapper in `core/data` |
| `when (status) { APPROVED -> …; DECLINED -> … }` with no `else` | Crashes when Seerr adds a value | Explicit unknown branch that renders a neutral state |
| A blocking hook on file length | Contradicts the advisory rule | Warning-level lint only |
| Test at `test/RequestMapperTest.kt` for `data/requests/RequestMapper.kt` | Cannot be found from the source | `test/data/requests/RequestMapperTest.kt` |
