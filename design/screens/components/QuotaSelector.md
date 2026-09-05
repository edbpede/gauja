<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# QuotaSelector

## Contract

Movie/TV quota amount and rolling window editor or read-only usage summary. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/QuotaSelector/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Media scope, limit/window, usage/reset information when returned, and server-defined unlimited/disabled state.

## Actions

Edit amount/window in native controls and emit a draft; validation messages stay next to the field. Save belongs to the containing profile/user/default form.

## Endpoints

GET /user/{userId}/quota and user/default settings operations listed in the owning screen.

## Permissions

Own usage is readable; quota edits require MANAGE_USERS or ADMIN for defaults.

## Acceptance criteria

Do not invent a reset date or interpret missing usage as zero. Honor the server’s unlimited representation and validate numeric ranges before submit.
