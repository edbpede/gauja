#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Generate or check one or both native clients from the pinned shared contract."""
import argparse
import hashlib
import json
from pathlib import Path
import subprocess
import sys
import tempfile
import urllib.request

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "contract"))
from validate import load_contract
from outputs import publish
from redaction import kotlin_sources, swift_descriptions
from wire import wire_document

ROOT = Path(__file__).resolve().parents[2]


def versions():
    return dict(line.split("=", 1) for line in (ROOT / "tools/codegen/versions.env").read_text().splitlines()
                if line and not line.startswith("#"))


def run(command):
    result = subprocess.run(list(map(str, command)), cwd=ROOT, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    if result.returncode:
        print(result.stdout[-12000:], file=sys.stderr)
        raise ValueError(f"Command failed: {command[0]}")


def android(spec, temporary, pins):
    version = pins["OPENAPI_GENERATOR_VERSION"]
    jar = ROOT / ".cache" / f"openapi-generator-{version}.jar"
    jar.parent.mkdir(exist_ok=True)
    if not jar.exists() or hashlib.sha256(jar.read_bytes()).hexdigest() != pins["GENERATOR_ARCHIVE_SHA256"]:
        url = f"https://repo.maven.apache.org/maven2/org/openapitools/openapi-generator-cli/{version}/openapi-generator-cli-{version}.jar"
        with tempfile.TemporaryDirectory(dir=jar.parent, prefix="openapi-download-") as download:
            candidate = Path(download) / jar.name
            urllib.request.urlretrieve(url, candidate)
            if hashlib.sha256(candidate.read_bytes()).hexdigest() != pins["GENERATOR_ARCHIVE_SHA256"]:
                raise ValueError("OpenAPI Generator checksum mismatch")
            candidate.replace(jar)
    output = temporary / "android"
    run(["java", "-jar", jar, "generate", "-i", spec, "-g", "kotlin", "--library", "jvm-retrofit2",
         "-c", ROOT / "tools/codegen/android/config.json", "--global-property",
         "apis,models,supportingFiles=CollectionFormats.kt,apiDocs=false,modelDocs=false,apiTests=false,modelTests=false", "-o", output])
    sources = output / "src/main/kotlin"
    kotlin_sources(sources)
    return sources, ROOT / "apps/android/core/api/src/main/kotlin"


def ios(spec, temporary, pins):
    package = ROOT / "tools/codegen/ios"
    manifest = (package / "Package.swift").read_text()
    if f'exact: "{pins["SWIFT_OPENAPI_GENERATOR_VERSION"]}"' not in manifest:
        raise ValueError("Swift generator pin disagrees with versions.env")
    output = temporary / "ios"
    run(["swift", "run", "--package-path", package, "--disable-automatic-resolution",
         "swift-openapi-generator", "generate", spec, "--config", package / "config.yml", "--output-directory", output])
    swift_descriptions(output)
    return output, ROOT / "apps/ios/Packages/SeerrAPI/Generated"


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="Compare temporary output; never rewrite committed sources")
    parser.add_argument("--platform", choices=["android", "ios", "all"], default="all")
    args = parser.parse_args()
    try:
        spec = wire_document(load_contract(ROOT / "api"))
        with tempfile.TemporaryDirectory(prefix="gauja-codegen-") as directory:
            temporary = Path(directory)
            effective = temporary / "openapi.json"
            effective.write_text(json.dumps(spec, indent=2, ensure_ascii=False) + "\n")
            for platform, generator in [("android", android), ("ios", ios)]:
                if args.platform in {platform, "all"}:
                    source, destination = generator(effective, temporary, versions())
                    publish(source, destination, args.check)
                    print(f"codegen: {platform} {'matches' if args.check else 'generated'}")
    except (ValueError, OSError, KeyError) as error:
        parser.exit(1, f"codegen: {error}\n")


if __name__ == "__main__":
    main()
