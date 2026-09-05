<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# PermissionEdit

## Contract

Full Seerr permission matrix used by user/default/bulk editors. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/PermissionEdit/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

All 30 named flags, current mask, target context and unknown bits retained unchanged. Group labels and dependency explanations mirror Seerr’s PermissionEdit.

## Actions

Toggle an allowed bit through the state owner; save the complete mask once. Self-view is read-only. Explain 4K child flags in their REQUEST_4K context rather than silently clearing unrelated bits.

## Endpoints

GET/POST /user/{userId}/settings/permissions; PUT /user for bulk; /settings/main for defaults (use exact methods from the effective contract).

## Permissions

MANAGE_USERS for user edits and ADMIN for server defaults. The server is authoritative for restrictions on granting administrative permissions.

## Acceptance criteria

Round-trip every bit including reserved bit 29 and any unknown future bits without loss. Parameterized tests use the pinned permission table, and rejected saves preserve the draft.
