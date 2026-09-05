<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# TitleCard

## Contract

Compact movie/TV identity shared by Discover, search and related-media lists. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/TitleCard/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Typed media identity, poster at layout size, title, year, media type, rating when present, availability and 4K markers. Missing artwork retains aspect ratio and a neutral placeholder.

## Actions

Open the correct movie/TV detail using its media type and TMDB ID. Request is a separate RequestButton; avoid competing nested tap regions.

## Endpoints

Read models from GET /search, GET /discover/trending, GET /discover/movies, GET /discover/tv. The card itself performs no I/O.

## Permissions

Signed-in viewing; request actions follow RequestButton. Availability does not imply request permission.

## Acceptance criteria

A recycled cell opens the currently bound identity; missing poster/title never crashes. Status and title remain audible and legible at largest text size.
