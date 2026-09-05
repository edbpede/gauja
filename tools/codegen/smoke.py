#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Compile committed generated clients and run synthetic serialization tests."""
import argparse
from pathlib import Path
import shutil
import subprocess
import re


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--platform", choices=["android", "ios"], required=True)
    args = parser.parse_args()
    root = Path(__file__).resolve().parents[2]
    pins = dict(line.split("=", 1) for line in (root / "tools/codegen/versions.env").read_text().splitlines()
                if line and not line.startswith("#"))
    work = root / ".cache" / ("smoke-" + args.platform)
    work.mkdir(parents=True, exist_ok=True)
    if args.platform == "android":
        installed = subprocess.check_output(["gradle", "--version"], text=True)
        version = re.search(r"^Gradle (\S+)$", installed, re.M)
        if not version or version[1] != pins["GRADLE_VERSION"]:
            parser.exit(1, f"smoke: Gradle {pins['GRADLE_VERSION']} is required\n")
        source = root / "apps/android/core/api/src/main/kotlin"
        destination = work / "src/main/kotlin"
        test_destination = work / "src/test/kotlin"
        template, manifest = "build.gradle.kts.in", "build.gradle.kts"
        (work / "settings.gradle.kts").write_text('rootProject.name = "SeerrAPISmoke"\n')
        command = ["gradle", "--no-daemon", "--console=plain", "--project-dir", str(work), "test"]
    else:
        source = root / "apps/ios/Packages/SeerrAPI/Generated"
        destination = work / "Sources/SeerrAPI"
        test_destination = work / "Tests/WireTests"
        template, manifest = "Package.swift.in", "Package.swift"
        command = ["swift", "test", "--disable-automatic-resolution", "--package-path", str(work)]
    for directory in (destination, test_destination):
        if directory.exists():
            shutil.rmtree(directory)
    shutil.copytree(source, destination)
    shutil.copytree(root / "tools/tests/codegen" / args.platform, test_destination)
    text = (root / "tools/codegen" / args.platform / "smoke" / template).read_text()
    for key, value in pins.items():
        text = text.replace("@" + key + "@", value)
    (work / manifest).write_text(text)
    lock = "gradle.lockfile" if args.platform == "android" else "Package.resolved"
    shutil.copyfile(root / "tools/codegen" / args.platform / "smoke" / lock, work / lock)
    subprocess.run(command, check=True)


if __name__ == "__main__":
    main()
