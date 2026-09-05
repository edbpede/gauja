<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# CompanyCard

## Contract

Studio or network browse entry. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/CompanyCard/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Company kind, typed ID, name and logo/placeholder. Studio and network IDs are distinct domains.

## Actions

Open the matching filtered movie/studio or TV/network grid.

## Endpoints

GET /studio/{studioId}; GET /network/{networkId}; discover studio/network operations from the effective contract.

## Permissions

Signed-in viewing.

## Acceptance criteria

The same numeric ID in studio and network namespaces yields different stable keys and destinations.
