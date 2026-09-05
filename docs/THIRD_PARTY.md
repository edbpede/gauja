<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Third-party material

## Seerr OpenAPI specification

`api/seerr-api.yml` is vendored verbatim from [seerr-team/seerr](https://github.com/seerr-team/seerr) (MIT; text in `api/LICENSE.upstream`), pinned by `api/UPSTREAM_COMMIT`. Added in Phase 2.

## Seerr translation seed

Placeholder. Phase 11.3 may seed UI strings whose meaning is identical to Seerr's (status names, permission labels, settings section titles) from Seerr's `server/i18n/locale/` catalogs (MIT). When that one-time import happens, this section records the upstream commit, the catalogs used and the MIT notice. Seeding is not an ongoing dependency (PRD §16).

## Dependencies

Runtime and build dependencies are listed with their licenses in the SBOM published with each release (Phase 12). Every dependency license must be in the `deny.toml` allow-list.
