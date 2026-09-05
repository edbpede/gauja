<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# StatusBadge

## Contract

Compact availability or request-state label; semantic families are shared across platforms. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/StatusBadge/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Media status: UNKNOWN=1, PENDING=2, PROCESSING=3, PARTIALLY_AVAILABLE=4, AVAILABLE=5, BLOCKLISTED=6, DELETED=7. Request status: PENDING=1, APPROVED=2, DECLINED=3, FAILED=4, COMPLETED=5. Optional 4K label.

## Actions

Static by default. If interactive, open permitted request/download or media-management detail. No playback action.

## Endpoints

No network calls; domain status from the containing aggregate. Values cite server/constants/media.ts at the contract pin.

## Permissions

Status visibility follows its parent. Management requires MANAGE_REQUESTS; never infer permission from badge color.

## Acceptance criteria

Pending uses warning, processing/approved use indigo, available/partial/completed use green, failed/declined/blocklisted/deleted use danger, unknown uses neutral. Unknown raw values remain visible and never map to a known ordinal.
