<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# KeywordTag

## Contract

Keyword label and filter control. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/KeywordTag/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Typed keyword ID, label, selection/removal state and optional lookup progress.

## Actions

Open keyword results or toggle/remove in an editor; never treat free text as an ID.

## Endpoints

GET /search/keyword; GET /keyword/{keywordId}; keyword-filtered discover operations.

## Permissions

Signed-in viewing; write permission comes from the parent editor.

## Acceptance criteria

Unknown labels retain their ID until resolved. Cancellation cannot apply an old lookup result to a different keyword.
