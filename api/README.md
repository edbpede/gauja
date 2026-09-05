<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Seerr contract

The supported baseline is **Seerr v3.4.1**, commit `69f73a6f1486fdb51b8ddae9a94a8dfb629f461c`, fetched 2026-09-05. The vendored OpenAPI 3.0.2 document is unchanged upstream material (MIT); its `info.version: 1.0.0` is not the server release version. The base path is `/api/v1`.

`UPSTREAM_COMMIT` contains the SHA on line one and a commented fetch date on line two. Update it with the verbatim spec in the same commit. Check upstream bytes before accepting any sync. The supported floor is 3.4.1; later untested releases receive a soft warning in Phase 4. Per-feature `max: null` means no known removal, not a claim that future releases are tested.

The release has 163 paths and 212 operations, rather than the plan's historical 167/187. Counts per family are generated in [ENDPOINTS.md](ENDPOINTS.md). The four develop-only library paths are documented there; no endpoints are invented to fill those gaps.

Install shared tooling with `tools/contract/python.sh --install`. Validate with `tools/api-drift/check-local.sh --working-tree`. Run `tools/codegen/generate.sh` to update both clients, or add `--platform android` / `--platform ios`. `--check` compares without rewriting. See `tools/codegen/README.md` for toolchain setup.

`coverage.json` is editable implementation status, keyed by operation ID. Regenerate the endpoint checklist with `tools/contract/python.sh tools/contract/endpoints.py`. No feature is implemented merely because its client exists. Excluded initialization, push-subscription and sunset blacklist operations must not be called by Gauja.

Missing operation IDs are synthesized only for the effective spec: HTTP method, then camel-cased path segments, with `By` before each parameter name (`GET /user/{userId}` → `getUserByUserId`). Existing upstream IDs are retained; invalid names and collisions fail. Both generators and future recordings use these IDs.

The JSON Schema governs `compat.json`: required `min`, nullable `max`, `endpoint`, and explanatory `note`. Initial minima are Gauja's supported floor, not historical introduction claims. Phase 4 consumes this file for runtime gating; capabilities also depend on server configuration and permissions.
