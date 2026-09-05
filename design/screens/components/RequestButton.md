<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# RequestButton

## Contract

Permission-aware entry point to movie/TV request creation. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/RequestButton/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Media identity, requestability, availability, existing request state, 4K server configuration and supported-version state. Use a textual state for already requested/available or unavailable features.

## Actions

Open the request sheet. TV chooses eligible seasons; advanced options appear only when permitted. Do not submit a request directly during rendering.

## Endpoints

POST /request, owned by the request sheet; configuration and service choices come from the shared Data layer.

## Permissions

Movie: REQUEST OR REQUEST_MOVIE; TV: REQUEST OR REQUEST_TV. 4K: REQUEST_4K OR corresponding REQUEST_4K_MOVIE/REQUEST_4K_TV, plus server 4K configuration. REQUEST_ADVANCED gates advanced controls. ADMIN short-circuits upstream permission checks.

## Acceptance criteria

Unavailable/already requested seasons cannot be duplicated. Offline or unsupported-version actions explain why they are disabled; submission is single-flight.
