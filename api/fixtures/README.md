<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Recorded server fixtures

Recording starts in Phase 11 against an initialized, seeded upstream container. No response fixtures are recorded or hand-authored in Phase 2.

Layout: `<seerr-version>/<tag>/<operationId>.json` for the default scenario; `<operationId>-<scenario>.json` for alternatives such as `page1`, `empty`, `forbidden`, or `restart-required`. Use the shared effective operation ID from `ENDPOINTS.md`. One file holds one recorded response body. The recorder tracks method, path, status and selected non-secret headers separately; it must not save raw auth headers.

Before committing, scrub credentials to `REDACTED` and run `tools/ci/check-fixture-secrets.sh` plus gitleaks. Never record cookies, API keys, Basic passwords, Plex/Jellyfin tokens, Quick Connect secrets, VAPID private material or PEM keys. Do not print rejected values. Private server recordings use ignored `*.local.*` names and are never uploaded to CI.

Both platforms consume these files from the shared contract. Decode failures become evidence for an overlay; they are never solved by editing the recording or weakening a mapper. Phase 11 must replay every source-backed Phase 2 overlay against the container. Synthetic generator tests remain in `tools/tests/codegen/`.
