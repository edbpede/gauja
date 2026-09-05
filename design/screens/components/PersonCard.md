<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# PersonCard

## Contract

Cast, crew and search-result person identity. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/PersonCard/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Typed person ID, name, profile image, role/character when present and missing-image placeholder.

## Actions

Open person detail; do not substitute a movie ID for a person ID.

## Endpoints

GET /person/{personId}; GET /person/{personId}/combined_credits via its screen.

## Permissions

Signed-in viewing.

## Acceptance criteria

Long names and multiple roles wrap or use an accessible full label. Missing image never changes the stable cell identity.
