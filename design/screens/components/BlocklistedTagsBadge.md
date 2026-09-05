<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# BlocklistedTagsBadge

## Contract

Show that a title/collection is excluded from discovery. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/BlocklistedTagsBadge/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Blocklist status, tag/reason labels when supplied, title/collection scope and typed identity.

## Actions

Open permitted blocklist management; add/remove requires an explicit action and refreshes the aggregate after success.

## Endpoints

/blocklist and /blocklist/collection/{collectionId}; never the sunset /blacklist alias at the v3.4.1 baseline.

## Permissions

VIEW_BLOCKLIST for blocklist views; MANAGE_BLOCKLIST for mutations, with ADMIN short-circuit.

## Acceptance criteria

Unsupported versions disable management with explanation. A failed removal leaves the blocklist label intact; offline writes are unavailable.
