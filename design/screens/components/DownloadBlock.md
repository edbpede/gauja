<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# DownloadBlock

## Contract

Read-only request download progress, without playback. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/DownloadBlock/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Native chrome belongs to the containing screen.

## Content

Title, 4K/season/episode context, progress, remaining/total size, estimated time and service state where supplied. Inputs are immutable domain values; state flows down and events flow up. Do not create transport, storage or navigation owners inside this renderer.

## States

- **Loading:** retain existing content during refresh; use a noninteractive skeleton for first load.
- **Empty:** omit absent optional metadata; show a meaningful placeholder or parent-owned empty state when the core value is absent.
- **Error:** keep valid content and draft; expose the parent’s retry/message without a retry loop.
- **Offline:** retain cached reads and their timestamp; all writes are disabled with an explanation.
- **Permission-denied:** hide unauthorized actions and use the parent’s explanation if the entire content is forbidden.

## Actions

Expand for details. External service link-outs require the screen’s permission and system browser; no embedded web content.

## Adaptive behavior

Compact uses native cards/chips/forms and stacked navigation. Medium/expanded size classes adjust wrapping, grid columns or form width without stretching text indefinitely. Lists preserve stable IDs and scroll state. The owning settings/request screen uses native list/detail scaffolds; this component never branches on raw screen width.

## Accessibility

Expose the full identity/value and action through TalkBack/VoiceOver. Avoid redundant decorative-image labels. Status and selection use text/semantics as well as color. Honor maximum Dynamic Type/font scaling without fixed text heights, native 48 dp/44 pt targets, keyboard/focus order and reduced motion. Theme values come from generated tokens, including the light theme.

## Endpoints

Embedded download status in media/request aggregates; no direct service-server calls. The owning Data/screen layer performs I/O and handles pagination; the component does not.

## Permissions

Parent request visibility; service/admin details require MANAGE_REQUESTS or ADMIN as upstream specifies. Re-evaluate after profile or permission changes. Server rejection is handled visibly even if the action was initially allowed.

## Acceptance criteria

Zero/unknown size produces indeterminate progress; clamp displayed progress to 0–100 percent. Reduced motion stops decorative animation and screen readers receive textual progress. Exercise loading, empty, error, offline and denied inputs on both platforms; verify compact and expanded presentations, largest text and screen-reader semantics. Image requests use rendered size and cancellation. Cached content contributes to the ≤300 ms offline budget and scrolling to <1% jank; measurements land in Phase 11.
