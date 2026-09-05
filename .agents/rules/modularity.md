---
type: "agent_requested"
description: "Gauja modularity doctrine: cohesive responsibilities and justified modules, dependency direction, generated-code isolation (both platforms)"
---

# Modularity

This file expands PRD §12.2 and §12.3 into rules that apply to every line under `apps/android/` and `apps/ios/`. It is normative: violations are review blockers, with import checks active now and dependency graph enforcement added with working modules in Phase 3. On language, framework and library usage the platform rule files (`kotlin-2_4-android-app.md`, `swift-6_3-ios-app.md`) govern; this file governs shape.

## Cohesive responsibilities

1. **Organize by cohesive responsibility.** Related types may share a file. Split when responsibilities diverge or navigation becomes difficult, not because a file contains a second type.
2. **Create build modules for an enforced boundary or demonstrated build/ownership benefit.** Folders are the default within that boundary. Start with one Data module/target and one Settings feature module/target; aggregate and notification-agent folders are not separate builds by default.
3. **Name the concern.** Avoid unrelated `Utils`, `Helpers` or catch-all repositories. Names describe purpose without repeating the entire folder path.
4. **Use folders as the domain grows.** Add depth when it improves navigation, without file-count quotas or empty-directory READMEs.
5. **Advisory size limits.** Files over ~300 lines and functions over ~40 lines may produce lint warnings, never blocking errors. Generated output follows supported generator structure and is exempt.
6. **Tests follow behavior.** Place meaningful tests where contributors can find them from the source. Do not create empty test trees or suites for package stubs.

## Inside a module

- As needed, feature modules use `ui/` (screens and state holders), `domain/` (use cases), `navigation/` (keys or routes), with nested subfolders per screen (`list/`, `detail/`, `edit/`).
- A screen is a stateless renderer plus a state owner (ViewModel / `@Observable` model) with clear ownership; closely related types may share a file. Its UI state is one immutable type per screen, modelling the applicable states in its screen spec.
- Every `when` / `switch` over an upstream-defined enum (`MediaStatus`, `MediaRequestStatus`, `IssueType`, `IssueStatus`, `MediaServerType`, `DiscoverSliderType`, permission bits) has an explicit unknown branch. Seerr adds values.
- Pure domain logic (`hasPermission`, `ServerVersion` parsing, `FeatureGate`, image-URL rewriting, deep-link parsing) lives in `core/model` / `core/common` / `Model` / `Common` with no platform imports, so it is unit-testable in isolation. Inject `Clock`, dispatchers and dependency keys; never read a global.

## Dependency direction

Preserve API → Data → domain exposure and feature isolation. The tables are an allowed responsibility map; add a module only with a working consumer.

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
- **Generated DTOs may cross only the API → Data mapping boundary (plus tests), and never Data’s outward interface.** `core/data` / `Data` wraps them in hand-written domain models with aggregate-focused mappers in Data folders.
- Each feature package on iOS exposes exactly one public entry view and one public route type; everything else is `internal`. On Android the feature module exposes its `NavKey`s and its `EntryProviderInstaller`; screens are `internal`.
- Enforcement: Gradle `dependency-analysis` plus `tools/ci/check-module-graph.sh` on Android; SPM package boundaries plus `tools/ci/check-package-graph.sh` on iOS. Implement graph assertions against the allowed dependencies above when the working modules land in Phase 3; do not create passing placeholder scripts. The tables describe permitted dependencies, not a requirement to create every listed module upfront.

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
