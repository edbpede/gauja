<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

## Summary

<!-- What changes and why. Link the implementation-plan task (e.g. §4.3) where one applies. -->

## Checklist

- [ ] PR title is a Conventional Commit subject (it becomes the squash-merge commit)
- [ ] Every commit is signed off (`git commit -s`, DCO)
- [ ] The rule files in `.agents/rules/` were followed (platform file, `modularity.md`, `api-contract.md`, `monorepo.md`)
- [ ] UI changes link their screen spec in `design/screens/` (written first if it did not exist)
- [ ] Tests mirror sources folder-for-folder and cover the change
- [ ] No generated code was hand-edited (`core/api`, `SeerrAPI/Generated`, generated themes)
- [ ] `prek run --all-files` passes locally
- [ ] One module / one screen / one hook per PR where practical

## Validation

<!-- Commands run and their results (gradle / xcodebuild / tools/tests/run.sh / reuse lint). -->
