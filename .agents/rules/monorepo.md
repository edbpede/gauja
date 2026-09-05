---
type: "agent_requested"
description: "Gauja monorepo rules: directory ownership, CI lane triggers, the apps/android ↔ apps/ios boundary"
---

# Monorepo

Gauja is two independent native apps plus a shared contract in one repository (PRD §13, Appendix C decision 1). This file is normative for what lives where, what triggers which CI lane, and what may cross the `apps/` boundary.

## Layout and ownership

| Path | Contents | Owners (`.github/CODEOWNERS`) |
|---|---|---|
| `api/` | Vendored spec, `UPSTREAM_COMMIT`, overlays, fixtures, `compat.json` | Both platform maintainers |
| `design/` | `tokens.json`, `screens/<area>/<screen>.md`, `assets/` | Both platform maintainers |
| `apps/android/` | Gradle project (`app`, `core/*`, `feature/*`, `build-logic/`) | Android maintainers |
| `apps/ios/` | `project.yml`, `App/`, `Packages/*` | iOS maintainers |
| `tools/` | `codegen/`, `tokens/`, `api-drift/`, `ci/`, `community/`, `tests/` | Project leads |
| `docs/` | PRD, implementation plan, `THIRD_PARTY.md` | Project leads |
| `.agents/rules/` | Normative rule files | Project leads |
| root | `LICENSE`, `APPSTORE_EXCEPTION.md`, `REUSE.toml`, `prek.toml`, `deny.toml`, `renovate.json`, `CONTRIBUTING.md`, `SECURITY.md`, `README.md` | Project leads |

A change under `apps/android/` needs no iOS reviewer and vice versa. A change under `api/` or `design/` needs both, because both apps consume it.

## The cross-boundary rule

- **Nothing under `apps/android/` references anything under `apps/ios/`, and vice versa.** No imports, no relative paths, no Gradle `includeBuild`, no SPM `path:` dependency, no symlink, no shared script. `pr-hygiene.yml` greps for the other tree's path and fails the PR.
- **No shared runtime code.** No Kotlin Multiplatform, no shared library, no shared `common/` directory. What both apps share is the contract: `api/`, `design/`, and the generators in `tools/` that read them.
- **Artifacts flow one way.** `api/` → generated clients and `FeatureGate` tables; `design/tokens.json` → generated themes; `design/screens/` → both apps' behaviour. Nothing flows from an app into `api/` or `design/`, and nothing flows sideways between the apps. If both apps need the same fact, it becomes part of the contract first.
- Each app builds, tests and lints alone. A contributor with only a JDK builds Android; one with only Xcode builds iOS. A lane never installs the other platform's toolchain.

## CI lanes and triggers (`.github/workflows/`)

| Workflow | Trigger | Scope |
|---|---|---|
| `pr-hygiene.yml` | every PR; push to `main` | `prek run --all-files`, `reuse lint`, gitleaks, commit-message and PR-title check, tool tests, cross-boundary grep. `REUSE`, `prek`, `commit-messages` (the DCO check), `gitleaks`, `tool-tests` and `boundary` are required status checks |
| `android.yml` | `apps/android/**`, `api/**`, `design/**`, `tools/codegen/**`, `tools/tokens/**` | Build, unit tests, ktfmt, detekt, Android Lint, module-graph check, generated-code drift, emulator smoke, baseline profile, egress test |
| `ios.yml` | `apps/ios/**`, `api/**`, `design/**`, `tools/codegen/**`, `tools/tokens/**` | XcodeGen, build, Swift Testing, swift-format lint, SwiftLint strict, package-graph check, generated-code drift, simulator smoke, egress test |
| `contract.yml` | `api/**`, `tools/codegen/**`; weekly | Seerr container, seeded fixtures, recorded contract tests for both generated clients, imminent-`Sunset` check |
| `api-sync.yml` | weekly schedule | Upstream diff, PR with new spec, pin and regenerated clients |
| `tokens-check.yml` | `design/**`, `tools/tokens/**` | Regenerate both themes, fail on diff |
| `release.yml` | tags `android/v*`, `ios/v*` | Reproducible build, SBOM, F-Droid metadata, store upload |

Rules:

- Every action is pinned by commit SHA; Renovate keeps the pins current.
- `paths:` filters are the contract above. Adding a new shared input means adding it to both platform lanes' filters in the same PR.
- A lane's `not yet wired` placeholder step is replaced, never bypassed, when the phase that owns it lands (Phase 3 for `android`/`ios`, Phase 2 for `tokens-check`, Phase 11 for `contract`/`api-sync`, Phase 12 for `release`).
- Complexity and length lints stay at warning level in every lane (PRD §12.2 rule 5).

## Branches, commits, merges

- `main` is protected. Never commit to it directly (the `no-commit-to-branch` hook refuses).
- Conventional Commits, signed off (`git commit -s`). Squash-merge only; the PR title is the commit subject.
- One module, one screen or one hook per PR where practical. A PR touching both `apps/android/` and `apps/ios/` is acceptable only for a contract change that both must consume in lockstep (a spec sync, a token change) and then each app's change must be reviewable on its own.

## Anti-patterns

| Wrong | Why | Right |
|---|---|---|
| `apps/ios/Packages/Model` reads `apps/android/core/model` fixtures | Sideways dependency | Fixtures live in `api/fixtures/` |
| A `shared/` directory with Kotlin and Swift side by side | Runtime sharing by the back door (PRD Appendix C decision 1) | Put the fact in `api/` or `design/`; generate per platform |
| Hand-copying a token value into a Swift theme | Drifts from `design/tokens.json` | Run `tools/tokens/`; the theme is generated |
| `uses: actions/checkout@v7` | Unpinned; supply-chain risk (PRD §10) | `uses: actions/checkout@<sha> # v7.x.y` |
| Installing Xcode in the Android lane to run a shared script | Breaks build-one-platform independence | Keep shared scripts in `tools/` and dependency-free (bash, python3 stdlib) |
