<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# JSONEditor

## Contract

Native editor for Seerr’s webhook payload template. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/JSONEditor/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Monospaced draft, insertion position, server-supported template-variable palette, inline JSON/template feedback and unsaved state.

## Actions

Insert a variable at the caret; undo/redo via native editing; test against Seerr and save only through the parent form. Treat the template as inert text and do not evaluate scripts or fetch referenced URLs.

## Endpoints

POST /settings/notifications/webhook/test and webhook settings operations from the effective contract.

## Permissions

ADMIN and enabled webhook configuration.

## Acceptance criteria

Preserve literal placeholders, whitespace and draft after test failure. Validation must account for template syntax rather than requiring prematurely rendered JSON. Export/logging never exposes credentials.
