<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# RequestCard

## Contract

Request summary for request lists and recent requests. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/RequestCard/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Request ID, media title/poster, requester, status, modified date, requested seasons, 4K marker and download summary when available.

## Actions

Open request detail. Approve/decline/retry/edit/delete are permission-gated list or context actions; destructive operations require confirmation owned by the screen.

## Endpoints

GET /request; POST /request/{requestId}/{status}; PUT /request/{requestId}; DELETE /request/{requestId}.

## Permissions

Owner visibility follows the server; REQUEST_VIEW for others and MANAGE_REQUESTS for management, with upstream ADMIN semantics.

## Acceptance criteria

Never show an approval action to an ordinary requester. Failed actions leave the visible request and error intact; server responses decide final status.
