<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# IssueBlock

## Contract

An issue summary or comment block with ownership and resolution state. Consumed by Phase 4 and the owning feature phases. Reference: Seerr `src/components/IssueBlock/` at `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c` (inspiration only). Uses the [shared behavior baseline](INVENTORY.md#shared-behavior-baseline).

## Content

Issue ID, media identity, type, season/episode context, open/resolved label, author, timestamp and message/comment text.

## Actions

Open issue detail; create/comment/edit/delete/resolve/reopen only when the screen and server permit. Preserve text after rejected writes.

## Endpoints

GET /issue; GET /issue/{issueId}; POST /issue/{issueId}/comment; PUT/DELETE /issueComment/{commentId}.

## Permissions

VIEW_ISSUES to browse, CREATE_ISSUES to report, MANAGE_ISSUES to moderate. Own-comment edit/delete also follows server ownership checks.

## Acceptance criteria

Unknown issue type/status renders a neutral label. Comment order and stable IDs prevent duplicate cells after refresh.
