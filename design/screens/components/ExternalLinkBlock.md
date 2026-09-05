<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# ExternalLinkBlock

## Contract

Explicit external metadata and trailer link-outs. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/ExternalLinkBlock/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Provider label, URL and accessible external-destination description; only links supplied or safely constructed from known external IDs.

## Actions

Open supported HTTP(S) links in the system browser; trailers are link-outs. Reject unsupported URL schemes. No in-app playback or web view.

## Endpoints

No in-app fetch; external browsing is delegated to the operating system.

## Permissions

Parent viewing permission; service-management links require their administrative permission.

## Acceptance criteria

Missing/malformed URLs hide that link. Opening a URL sends no Seerr cookie, key or Basic credential to the external destination.
