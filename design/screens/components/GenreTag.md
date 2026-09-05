<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# GenreTag

## Contract

Small selectable genre label in details and filter editors. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/GenreTag/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Typed genre ID, localized label, media type, selection state where applicable.

## Actions

Details navigate to the genre grid; editors toggle selection through an event to the state owner.

## Endpoints

No direct calls; genre lookup/discover operations are owned by Data.

## Permissions

Signed-in viewing; editor save permission belongs to its screen.

## Acceptance criteria

Selected state is conveyed through semantics and an icon, not just color. Touch area meets native minimums even when the visual chip is small.
