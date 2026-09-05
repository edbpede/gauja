<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# DownloadBlock

## Contract

Read-only request download progress, without playback. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/DownloadBlock/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Title, 4K/season/episode context, progress, remaining/total size, estimated time and service state where supplied.

## Actions

Expand for details. External service link-outs require the screen’s permission and system browser; no embedded web content.

## Endpoints

Embedded download status in media/request aggregates; no direct service-server calls.

## Permissions

Parent request visibility; service/admin details require MANAGE_REQUESTS or ADMIN as upstream specifies.

## Acceptance criteria

Zero/unknown size produces indeterminate progress; clamp displayed progress to 0–100 percent. Reduced motion stops decorative animation and screen readers receive textual progress.
