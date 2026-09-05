<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# MediaSlider

## Contract

Ordered, titled horizontal media collection on Discover or a detail screen. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/MediaSlider/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Stable slider ID and title, ordered TitleCard list, page/total state, loading-more state and cached timestamp. Respect enabled/order/data from the server.

## Actions

Open an item; See all opens the same filter as a paginated grid. Prefetch the next page and correctly sized artwork near the end; avoid duplicate requests.

## Endpoints

GET /settings/discover supplies home ordering and configuration. Home collection sources are GET /media (recently added), GET /request (recent requests), GET /discover/watchlist (Plex watchlist), GET /discover/trending, GET /discover/movies and GET /discover/tv (popular/upcoming and filtered custom collections), GET /discover/movies/studio/{studioId}, GET /discover/tv/network/{networkId}, and GET /search (custom search). Genre collections use the [GenreCard](GenreCard.md#endpoints) operations.

The owning screen spec must pin the selected operation, query filters and pagination before implementation; detail-screen collections use their owning screen’s exact operation from the effective contract. The owning Data/screen layer performs I/O and supplies domain values; the component does not select or call endpoints.

## Permissions

Signed-in; RECENT_VIEW / REQUEST_VIEW / WATCHLIST_VIEW apply to restricted slider data according to its server endpoint.

## Acceptance criteria

Disabled sliders are absent. Order matches the server. A failed next page preserves loaded cards and offers retry; stable IDs preserve scroll position.
