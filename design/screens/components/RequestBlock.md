<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# RequestBlock

## Contract

Inline request history on movie/TV details. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/RequestBlock/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Request status, requester, created/modified timestamps, season scope and 4K variant; a stable request ID is mandatory.

## Actions

Open request detail; expose permitted manage actions without duplicating the whole RequestCard layout.

## Endpoints

Embedded media requests from GET /movie/{movieId} or GET /tv/{tvId}; request mutations use /request/{requestId}.

## Permissions

Request visibility follows the containing media response; management requires MANAGE_REQUESTS.

## Acceptance criteria

Multiple season requests remain distinguishable; loading management state does not replace unrelated requests.
