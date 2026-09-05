<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# PermissionEdit

## Contract

Full Seerr permission matrix used by user/default/bulk editors. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/PermissionEdit/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Native chrome belongs to the containing screen.

## Content

All 30 named flags, current mask, target context and unknown bits retained unchanged. Group labels and dependency explanations mirror Seerr’s PermissionEdit. Inputs are immutable domain values; state flows down and events flow up. Do not create transport, storage or navigation owners inside this renderer.

## States

- **Loading:** retain existing content during refresh; use a noninteractive skeleton for first load.
- **Empty:** omit absent optional metadata; show a meaningful placeholder or parent-owned empty state when the core value is absent.
- **Error:** keep valid content and draft; expose the parent’s retry/message without a retry loop.
- **Offline:** retain cached reads and their timestamp; all writes are disabled with an explanation.
- **Permission-denied:** hide unauthorized actions and use the parent’s explanation if the entire content is forbidden.

## Actions

Toggle an allowed bit through the state owner; save the complete mask once. Self-view is read-only. Explain 4K child flags in their REQUEST_4K context rather than silently clearing unrelated bits.

## Adaptive behavior

Compact uses native cards/chips/forms and stacked navigation. Medium/expanded size classes adjust wrapping, grid columns or form width without stretching text indefinitely. Lists preserve stable IDs and scroll state. The owning settings/request screen uses native list/detail scaffolds; this component never branches on raw screen width.

## Accessibility

Expose the full identity/value and action through TalkBack/VoiceOver. Avoid redundant decorative-image labels. Status and selection use text/semantics as well as color. Honor maximum Dynamic Type/font scaling without fixed text heights, native 48 dp/44 pt targets, keyboard/focus order and reduced motion. Theme values come from generated tokens, including the light theme.

## Endpoints

GET/POST /user/{userId}/settings/permissions; PUT /user for bulk; /settings/main for defaults (use exact methods from ENDPOINTS.md). The owning Data/screen layer performs I/O and handles pagination; the component does not.

## Permissions

MANAGE_USERS for user edits and ADMIN for server defaults. The server is authoritative for restrictions on granting administrative permissions. Re-evaluate after profile or permission changes. Server rejection is handled visibly even if the action was initially allowed.

## Acceptance criteria

Round-trip every bit including reserved bit 29 and any unknown future bits without loss. Parameterized tests use the pinned permission table, and rejected saves preserve the draft. Exercise loading, empty, error, offline and denied inputs on both platforms; verify compact and expanded presentations, largest text and screen-reader semantics. Image requests use rendered size and cancellation. Cached content contributes to the ≤300 ms offline budget and scrolling to <1% jank; measurements land in Phase 11.
