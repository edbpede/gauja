<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Architecture decision records

An ADR records one decision that shapes the codebase, so that nobody has to relitigate it in code review. The index mirrors PRD Appendix C; the PRD stays the product source of truth and each ADR links to the section that governs it.

## Format

Each file is `NNNN-short-slug.md` with these sections:

1. **Status** — Proposed, Accepted, Superseded by NNNN, or Deprecated.
2. **Context** — the forces and constraints; what the alternatives were.
3. **Decision** — one paragraph, stated in the active voice.
4. **Consequences** — what becomes easier, what becomes harder, what is now forbidden.

## Numbering

Four digits, sequential, never reused. A superseded ADR keeps its number and gains a *Superseded by* status line; the new ADR takes the next number. Changing a decision is a PR that adds the new ADR and edits the old status only.

## Index

| ADR | Decision | PRD |
|---|---|---|
| [0001](0001-native-only-no-kmp.md) | Pure Kotlin + pure Swift in one repository; no KMP; contract-only sharing | §11, §12, §13 |
| [0002](0002-notifications-deferred.md) | Notifications deferred; investigated design recorded | §7 |
| [0003](0003-agpl-with-app-store-exception.md) | AGPL-3.0-or-later with App Store Distribution Exception; DCO, no CLA | §15 |
| [0004](0004-no-play-services.md) | Single Android build; no Google Play Services / Firebase; F-Droid eligible | §10, §11.2, §15.4 |
| [0005](0005-vendored-spec-overlays-contract-tests.md) | Vendored OpenAPI spec pinned by upstream commit; overlays; contract tests against a real container | §4 |
| [0006](0006-same-ia-native-chrome.md) | Same IA / tokens / content components; platform-native chrome | §8 |
| [0007](0007-full-native-admin-v1.md) | Full native admin configuration in v1 | §5.10 |
| [0008](0008-android-minsdk-30.md) | Android minSdk 30 | §11.2, §18 risk 6 |
| [0009](0009-name.md) | Name: Gauja | §1 |
