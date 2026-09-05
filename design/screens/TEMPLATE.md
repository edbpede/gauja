<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# <Screen name>

## Contract

Area, screen ID, consuming phase, S/M/L estimate, pinned upstream reference, and entry/exit destinations. Screen names and ordering follow Seerr; chrome follows each native platform.

## Content

Ordered information, source of each value, shared content components and staleness information. Identify secret inputs and their transient/persistent storage boundary.

## States

Document only applicable states and their observable behavior. The list below is a review prompt, not a required set of variants for every renderer. Components inherit the [shared baseline](components/INVENTORY.md#shared-behavior-baseline) and record their specific behavior.

- **Loading:** initial load and refresh behavior, preserving existing content where useful.
- **Empty:** meaningful explanation and the available next action.
- **Error:** actionable message, retry behavior and preservation of user input.
- **Offline:** cached content with timestamp; writes disabled and visibly explained.
- **Permission-denied:** explanation and safe destination; no unauthorized action is exposed.

## Actions

For each action: permission/config/version prerequisites, confirmation where destructive, endpoint, validation, success destination, cancellation and failure behavior. Mutations require connectivity. Do not define a native playback or web-view action.

## Adaptive behavior

Compact: native stacked navigation/sheets. Medium: adjust columns using window/size classes. Expanded: native list/detail where appropriate; keep selection and unsaved work across resize/folding. Android uses WindowSizeClass and native adaptive scaffolds, never raw-width branching; iOS uses size classes and NavigationSplitView.

## Accessibility

Labels and logical focus order, native minimum targets (48 dp Android / 44 pt iOS), largest font/Dynamic Type without clipping, status beyond color, screen-reader announcements and reduced motion. Avoid fixed text heights.

## Endpoints

Exact method/path from the effective contract (see [API usage and endpoint renderer](../../api/README.md)), relative to `/api/v1`; distinguish server endpoints from approved Plex sign-in endpoints. No guessed paths. Specify pagination and stable keys for lists.

## Permissions

Exact upstream Permission names and AND/OR behavior, plus server configuration and `compat.json` gates. ADMIN uses upstream short-circuit semantics. “Signed in” is a session requirement, not a new permission bit.

## Content components

List components from `components/INVENTORY.md`; link their specs. Keep platform chrome native.

## Acceptance criteria

Observable Given/When/Then cases for applicable states/actions, permission/config gates, resize, accessibility, offline behavior and per-profile isolation. Link corresponding auth matrix rows where relevant. Cached aggregates render in ≤300 ms offline; apply PRD §9 budgets without claiming they were measured in this documentation phase.
