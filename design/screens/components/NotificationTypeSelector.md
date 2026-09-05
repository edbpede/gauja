<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# NotificationTypeSelector

## Contract

Server notification-type matrix for user preferences or an agent form. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/NotificationTypeSelector/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Notification types supported by the enabled agent, current bitmask and disabled reasons. Preserve unknown bits.

## Actions

Toggle supported types; saving/testing belongs to the parent form.

## Endpoints

/user/{userId}/settings/notifications or /settings/notifications/{agent} for the selected real agent.

## Permissions

Own preference access follows the server; admin agent editing requires ADMIN.

## Acceptance criteria

Unsupported types are explained. Configuring an agent never subscribes this app to push or starts background work; the Coming later app entry remains separate.
