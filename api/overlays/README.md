<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# OpenAPI overlays

One corrected operation or schema per `<area>-<operation-or-schema>.yml`, in OpenAPI Overlay 1.0.0 format. The top comment block identifies the Seerr version, operation/schema, rationale and evidence. Cite an upstream issue/PR or recorded behavior. Before Phase 11 recording, a precise `blob/<40-character SHA>/...` source/spec link is accepted by maintainer decision. Phase 11 must verify source-backed corrections against a real container.

Actions use RFC 9535 JSONPath. Objects merge recursively, arrays append, scalars replace, and `remove: true` deletes the selected property or element. To replace an array, remove it first and then update its parent. Filename order determines application order. Every target must match. Changes to the operation set, external references and missing citations fail validation. The effective document is temporary; never edit the vendored YAML or commit the effective document.

The initial overlays fix the three undeclared tags (`tmdb`, `issue`, `overriderule`), watchlist creation's required media type and 201 response, person numeric fields/birthday, override-rule IDs, the nullable request modifier, media-status documentation and a missing array type. Corrections are scoped to evidence; no blanket removal of required fields or nullability expansion is permitted.

On each sync, compare every correction with the new upstream document. Delete overlays that upstream has fixed in the same spec/pin commit. Synthetic tooling tests live under `tools/tests/`; they are never described as recorded server behavior.
