<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Contributing to Gauja

Gauja is two independent native apps (Kotlin on Android, Swift on iOS) that share only a contract (`api/`, `design/`). Read `docs/gauja-prd.md` for what and why, `docs/gauja-implementation-plan.md` for the order of work, and the rule files in `.agents/rules/` for how code must be written. The rule files are normative and win over both documents.

## Developer Certificate of Origin (DCO)

Every commit must be signed off:

```bash
git commit -s
```

The sign-off certifies the [Developer Certificate of Origin 1.1](https://developercertificate.org/) for that commit. There is no CLA; you keep your copyright.

> The license of this project is the GNU Affero General Public License, version 3 or (at your option) any later version, **together with** the additional permission set out in `APPSTORE_EXCEPTION.md`. Every reference to "the license" in this repository, including in SPDX headers and in the Developer Certificate of Origin, means the AGPL together with that additional permission.

By signing off you license your contribution under AGPL-3.0-or-later together with the App Store Distribution Exception. The GitHub DCO check is a required status on every pull request; the local `dco-signoff` hook rejects unsigned commits before they leave your machine.

## Local hooks (prek)

[prek](https://prek.j178.dev) runs the hooks in `prek.toml` on `pre-commit` and `commit-msg`.

```bash
prek install            # once per clone: installs both shims
prek run --all-files    # after changing prek.toml, or before opening a PR
```

Install prek from https://prek.j178.dev/installation/ (`brew install prek`, `uv tool install prek`, or the install script).

Hooks that fire on every commit:

| Hook | What it enforces |
|---|---|
| builtin (`trailing-whitespace`, `end-of-file-fixer`, `mixed-line-ending`, `check-merge-conflict`, `check-case-conflict`, `check-added-large-files` 512 KB, `detect-private-key`, `check-json`/`toml`/`yaml`, `no-commit-to-branch main`) | Hygiene; generated directories are excluded so generator output stays byte-identical |
| `conventional-pre-commit` | Conventional Commit message |
| `dco-signoff` | `Signed-off-by:` trailer |
| `gitleaks` | No credentials in the diff. prek downloads a Go toolchain and builds gitleaks itself; nothing to install. For standalone scans: `brew install gitleaks` (macOS) or a release binary from https://github.com/gitleaks/gitleaks/releases (Linux) |
| `api-drift`, `fixture-secrets`, `tokens-check`, `check-secret-logging`, `translations`, `license-check` | Path-scoped project checks under `tools/` (each has `--help`) |
| `swift-format`, `swiftlint`, `ktfmt`, `detekt` | Formatting and lint for `apps/ios/` and `apps/android/`; inert until those trees exist |

Formatting and lint hooks for one platform need that platform's toolchain. If you only work on Android, the iOS hooks skip because no Swift files are staged, and vice versa.

The scripts under `tools/` have tests: `tools/tests/run.sh`.

## Licensing headers (REUSE)

Every file carries `SPDX-FileCopyrightText: 2026 Gauja contributors` and `SPDX-License-Identifier: AGPL-3.0-or-later`, either as a header or through `REUSE.toml`. CI runs `reuse lint`; run it locally with `pipx install reuse` / `brew install reuse`. Dependencies must have a license in the `deny.toml` allow-list.

## Branches, commits and pull requests

- Never commit to `main`; branch from it with a `feat/`, `fix/`, `refactor/`, `docs/`, `chore/` or `ci/` prefix.
- Conventional Commits, one logical change per commit, every commit signed off.
- One module, one screen or one hook per PR where practical. Link the screen spec (`design/screens/<area>/<screen>.md`) for any UI change; write it first if it does not exist.
- Tests mirror sources folder-for-folder.
- Generated code (`apps/android/core/api/`, `apps/ios/Packages/SeerrAPI/Generated/`, generated themes) is never hand-edited. Regenerate with `tools/codegen/` or `tools/tokens/`.
- `main` is protected: squash-merge only, the PR title becomes the commit subject, and the `REUSE`, `DCO`, `prek` and `commit-messages` checks must pass.
- Nothing under `apps/android/` may reference `apps/ios/` or vice versa. Artifacts flow from `api/` and `design/` into the apps only.

## Building one platform without the other's toolchain

Each app is a complete, standalone build. Only `api/`, `design/` and `tools/` are shared inputs.

- **Android only:** JDK 17 and Android SDK 37. `cd apps/android && ./gradlew assembleDebug testDebugUnitTest detekt ktfmtCheck lint`. No Xcode needed.
- **iOS only:** Xcode 26 with the Swift 6.3 toolchain, XcodeGen 2.46.x, SwiftLint 0.65.x. `cd apps/ios && xcodegen generate` then build and test from Xcode or `xcodebuild`. No JDK needed.

Until Phase 3 of the implementation plan lands, neither app tree exists yet.

## Reporting security issues

See `SECURITY.md`. Never open a public issue for a vulnerability.
