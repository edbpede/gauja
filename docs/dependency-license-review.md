<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Native dependency license review

The Phase 3 export resolves 497 Maven coordinates across application, library,
test, processor, lint/plugin and included convention-build configurations. The
Swift export resolves 17 external package revisions across the application and
generator. Resolution uses the committed manifests and locks; catalogs alone do
not establish license coverage. Reports are generated under `.cache/licenses/`.

## Approved build exceptions

The maintainer approved these exact coordinates for build/test tooling during the
Phase 3 implementation. `deny.toml` is the policy owner; its exceptions never apply
to app runtime configurations or another dependency version.

| Coordinate | Applicable license | Evidence / consumer |
|---|---|---|
| `org.jetbrains.intellij.deps:trove4j:1.0.20200330` | LGPL-2.1-only | detekt → Kotlin compiler; [POM](https://repo.maven.apache.org/maven2/org/jetbrains/intellij/deps/trove4j/1.0.20200330/trove4j-1.0.20200330.pom) |
| `com.googlecode.juniversalchardet:juniversalchardet:1.0.3` | MPL-1.1 | AGP → databinding-compiler-common, even without app data binding; [POM](https://repo.maven.apache.org/maven2/com/googlecode/juniversalchardet/juniversalchardet/1.0.3/juniversalchardet-1.0.3.pom) |
| `com.ibm.icu:icu4j:77.1` | Unicode-3.0 | Build tooling; [license](https://raw.githubusercontent.com/unicode-org/icu/maint/maint-77/LICENSE) |
| `javax.annotation:javax.annotation-api:1.3.2` | CDDL-1.1 | Build tooling; the [license](https://github.com/javaee/javax.annotation/blob/master/LICENSE) offers CDDL 1.1 or GPLv2 with the Classpath exception; CDDL 1.1 is the selected alternative |

## Additional approved build exceptions

The maintainer approved these two additional exact build dependencies on
2026-09-06. Their exceptions have the same build-only and exact-version scope as
the entries above. kXML's exception requires both applicable licenses together.

| Coordinate | Applicable licenses | Evidence |
|---|---|---|
| `org.jdom:jdom2:2.0.6` | JDOM | The [POM](https://repo.maven.apache.org/maven2/org/jdom/jdom2/2.0.6/jdom2-2.0.6.pom) embeds the license, including its product naming restriction; generic BSD classification would lose that condition |
| `net.sf.kxml:kxml2:2.3.0` | MIT and `LicenseRef-XmlPull-Public-Domain` | The [POM](https://repo.maven.apache.org/maven2/net/sf/kxml/kxml2/2.3.0/kxml2-2.3.0.pom) dedicates bundled `org.xmlpull.v1` classes to the public domain; `KXmlParser.java` in the source JAR carries MIT. Both apply |

## Metadata and enforcement

`tools/ci/license-metadata.json` owns evidence-backed corrections for missing or
ambiguous Maven metadata. Each correction binds to an exact coordinate and POM
SHA-256. JNA explicitly offers Apache-2.0 as an alternative; the Checker qualifier
artifact is MIT; the ktfmt Gradle plugin's missing POM license is supported by its
upstream MIT text. These are metadata corrections, not unknown-license allowances.
Bouncy Castle's named license is its MIT permission text. All license declarations
must pass; the checker does not arbitrarily choose the most permissive entry.

The Android report requires every evaluated native module's resolution report,
plus root plugins and the included build. Empty or partial graphs fail. A dependency
present in any runtime configuration is checked in runtime scope. Swift reads the
resolved checkout state and license text, using build scope for generator/plugin
packages and conservatively treating other app packages as runtime. Missing or
unrecognized metadata fails; source-archive REUSE checks remain separate.

```sh
apps/android/gradlew --project-dir apps/android exportResolvedDependencies
python3 tools/ci/check-licenses.py android
python3 tools/ci/check-licenses.py ios
```

The Swift command requires completed app resolution under `apps/ios/DerivedData`
and generator resolution under `tools/codegen/ios/.build`. Each native lane creates
its own inputs without installing the other platform's toolchain. Tests reject
unknown licenses, mixed acceptable/unacceptable declarations, empty/partial graphs,
and build-only exceptions entering runtime scope or a different version.

Release SBOMs, complete bundled notices and redistribution review remain Phase 12
responsibilities described in [THIRD_PARTY.md](THIRD_PARTY.md).
