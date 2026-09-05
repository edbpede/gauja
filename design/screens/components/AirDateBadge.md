<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# AirDateBadge

## Contract

Localized release/air date and upcoming state. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/AirDateBadge/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

An optional date, media/episode context and an injected clock/time zone for relative presentation. Distinguish unknown date from a released item.

## Actions

No default action; opening details belongs to the parent.

## Endpoints

No calls; dates from movie/TV/season domain models.

## Permissions

No additional permission beyond the containing media.

## Acceptance criteria

Boundary tests cover midnight, time zones, missing/invalid dates and future dates. The label is meaningful without color or animation.
