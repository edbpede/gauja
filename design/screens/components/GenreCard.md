<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# GenreCard

## Contract

Visual genre entry into a media-type-specific browse grid. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/GenreCard/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Genre ID/name, movie or TV scope, and representative backdrop when supplied.

## Actions

Open the matching genre-filtered grid and preserve its media type.

## Endpoints

GET /genres/movie; GET /genres/tv; GET /discover/genreslider/movie; GET /discover/genreslider/tv; GET /discover/movies/genre/{genreId}; GET /discover/tv/genre/{genreId}.

## Permissions

Signed-in viewing.

## Acceptance criteria

Empty artwork still exposes the genre name. A TV genre never opens a movie grid.
