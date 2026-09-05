<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# PermissionOption

## Contract

One labeled permission control inside PermissionEdit. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/PermissionOption/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Exact flag bit/name, localized title/help, enabled/selected/read-only states and any dependent context.

## Actions

Emit a proposed toggle, never mutate a global mask or call the API.

## Endpoints

No calls.

## Permissions

Inherited from PermissionEdit; a disabled option has an inline reason.

## Acceptance criteria

TalkBack/VoiceOver announces label, checked state and restriction. Toggling one option changes only the intended bit; unknown flags are not assigned a guessed meaning.
