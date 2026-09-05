<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# RequestButton

## Contract

Permission-aware entry point to movie/TV request creation. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/RequestButton/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Native chrome belongs to the containing screen.

## Content

Media identity, requestability, availability, existing request state, 4K server configuration and supported-version state. Use a textual state for already requested/available or unavailable features. Inputs are immutable domain values; state flows down and events flow up. Do not create transport, storage or navigation owners inside this renderer.

## States

- **Loading:** retain existing content during refresh; use a noninteractive skeleton for first load.
- **Empty:** omit absent optional metadata; show a meaningful placeholder or parent-owned empty state when the core value is absent.
- **Error:** keep valid content and draft; expose the parent’s retry/message without a retry loop.
- **Offline:** retain cached reads and their timestamp; all writes are disabled with an explanation.
- **Permission-denied:** hide unauthorized actions and use the parent’s explanation if the entire content is forbidden.

## Actions

Open the request sheet. TV chooses eligible seasons; advanced options appear only when permitted. Do not submit a request directly during rendering.

## Adaptive behavior

Compact uses native cards/chips/forms and stacked navigation. Medium/expanded size classes adjust wrapping, grid columns or form width without stretching text indefinitely. Lists preserve stable IDs and scroll state. The owning settings/request screen uses native list/detail scaffolds; this component never branches on raw screen width.

## Accessibility

Expose the full identity/value and action through TalkBack/VoiceOver. Avoid redundant decorative-image labels. Status and selection use text/semantics as well as color. Honor maximum Dynamic Type/font scaling without fixed text heights, native 48 dp/44 pt targets, keyboard/focus order and reduced motion. Theme values come from generated tokens, including the light theme.

## Endpoints

POST /request, owned by the request sheet; configuration and service choices come from the shared Data layer. The owning Data/screen layer performs I/O and handles pagination; the component does not.

## Permissions

Movie: REQUEST OR REQUEST_MOVIE; TV: REQUEST OR REQUEST_TV. 4K: REQUEST_4K OR corresponding REQUEST_4K_MOVIE/REQUEST_4K_TV, plus server 4K configuration. REQUEST_ADVANCED gates advanced controls. ADMIN short-circuits upstream permission checks. Re-evaluate after profile or permission changes. Server rejection is handled visibly even if the action was initially allowed.

## Acceptance criteria

Unavailable/already requested seasons cannot be duplicated. Offline or unsupported-version actions explain why they are disabled; submission is single-flight. Exercise loading, empty, error, offline and denied inputs on both platforms; verify compact and expanded presentations, largest text and screen-reader semantics. Image requests use rendered size and cancellation. Cached content contributes to the ≤300 ms offline budget and scrolling to <1% jank; measurements land in Phase 11.
