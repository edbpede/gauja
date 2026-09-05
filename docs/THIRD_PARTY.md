<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Third-party material

## Seerr OpenAPI specification

`api/seerr-api.yml` is vendored verbatim from [seerr-team/seerr](https://github.com/seerr-team/seerr) (MIT; text in `LICENSES/MIT.txt`), pinned by `api/UPSTREAM_COMMIT`. See [contract baseline](../api/README.md). The pinned notice is **Copyright (c) 2020 sct**. Generated clients inherit specification descriptions; their REUSE annotations include this attribution alongside Gauja’s contributions.

## Seerr translation seed

No translation catalogs have been imported. Phase 11.3 may seed UI strings whose meaning is identical to Seerr's (status names, permission labels, settings section titles) from Seerr's `src/i18n/locale/` catalogs (MIT). When that one-time import happens, this section records the upstream commit, the catalogs used and the MIT notice. Seeding is not an ongoing dependency (PRD §16).

## Dependencies

Runtime and build dependencies are listed with their licenses in the SBOM published with each release (Phase 12). Every dependency license must be in the `deny.toml` allow-list.

## Design values

`design/tokens.json` records stock Tailwind CSS 3.4.19 palette values and Seerr
class references. Tailwind CSS is MIT licensed; attribution: Copyright (c) Tailwind Labs, Inc. Its MIT terms are reproduced in `design/LICENSE.tailwind`.
The token file records adaptations for accessible light/dark semantic pairs.
No Seerr artwork or logos are imported.

## Phase 2 tooling and smoke dependencies

These are development inputs, not an application dependency manifest. Exact
versions and transitive resolution are recorded in `tools/contract/requirements.txt`,
`tools/codegen/versions.env`, the generator `Package.resolved`, and the app Gradle/SwiftPM locks.

| Dependency family | License | Scope |
|---|---|---|
| PyYAML, attrs, jsonschema, jsonschema-specifications, referencing, rpds-py, python-jsonpath | MIT | Contract parsing/validation only |
| typing-extensions | PSF-2.0 | Python 3.12 contract tooling only |
| OpenAPI Generator, Kotlin, Gradle, kotlinx serialization/coroutines, Retrofit, OkHttp, Okio, JetBrains annotations | Apache-2.0 | Generation and JVM compile harness |
| Swift OpenAPI Generator/runtime/URLSession, Swift Algorithms/Argument Parser/Numerics/Collections/HTTP Types | Apache-2.0 | Generation and Swift compile harness |
| OpenAPIKit, Yams | MIT | Swift generator dependencies |
| SwiftLint 0.65.1 | MIT | Handwritten Swift tooling lint only |
| JUnit 4.13.2 | EPL-1.0 | Tests only; narrow build-only allowance in `deny.toml` |
| Hamcrest Core 1.3 | BSD-3-Clause | Tests only |

Resolved-dependency checks now run per platform. [The license review](dependency-license-review.md) records exact build-only exceptions and evidence-backed metadata corrections. Unknown metadata fails closed. Release SBOMs and complete bundled notices remain Phase 12 work.

## Distribution responsibility

Release packaging owns bundled notices for the material in each artifact. Source archives retain [LICENSE](../LICENSE), [APPSTORE_EXCEPTION.md](../APPSTORE_EXCEPTION.md), `LICENSES/` (including the complete Seerr notice in `MIT.txt`), [the Tailwind notice](../design/LICENSE.tailwind), and REUSE metadata. Binary apps bundle the complete Seerr and Tailwind notices when their generated descriptions/palette material ships, plus the applicable notices and license texts for resolved runtime dependencies. Verify both binary bundles and source archives before release; a link to this repository alone does not bundle a notice.

Gauja contributions retain AGPL-3.0-or-later with the existing additional permission. Inherited MIT material retains its MIT notice; combined REUSE expressions record both sets of obligations without changing ownership. Keep generator output unchanged and maintain attribution through repository annotations and packaging.

Merely running a build tool does not copy all of its dependencies into Gauja. If code or templates are actually copied or distributed, record their provenance and include the applicable license, modification and NOTICE material. Resolve dependencies and check `deny.toml` separately from REUSE lint: metadata completeness does not establish ownership or verify a release bundle.

The [MIT condition](https://opensource.org/license/mit) applies to copies or substantial
portions of upstream material, including the vendored specification and inherited descriptions;
calling Seerr's API alone does not require distributing its license. The [pinned Seerr notice](https://github.com/seerr-team/seerr/blob/69f73a6f1486fdb51b8ddae9a94a8dfb629f461c/LICENSE)
is stored once in `LICENSES/MIT.txt` and checked against upstream bytes. Its location is not
prescribed by MIT. Keep the Tailwind copyright alongside its applicable terms.

[OpenAPI Generator distinguishes its templates from generated output](https://github.com/OpenAPITools/openapi-generator/blob/v7.25.0/README.md#3---usage):
templates are Apache-2.0; generated output is not automatically under the generator's license.
Copied templates/code and distributed runtime libraries require separate review. For Apache
material actually distributed, [§4](https://www.apache.org/licenses/LICENSE-2.0) requires the
license, applicable retained notices, prominent modification notices and relevant upstream
NOTICE content. Do not copy a build tool's entire dependency notice inventory into app releases.

[REUSE](https://reuse.software/spec-3.3/) annotations associate attribution with files and
`LICENSES/` provides the license texts; neither replaces checking the contents of release bundles.
