#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Run root and feature smoke tests on the iOS 18 floor family and current SDK family."""
import json
from pathlib import Path
import subprocess

ROOT = Path(__file__).resolve().parents[2]


def select_runtimes(runtimes):
    versions = [runtime for runtime in runtimes if runtime.get("isAvailable") and runtime["name"].startswith("iOS ")]
    selected = []
    for major in (18, 26):
        candidates = [runtime for runtime in versions if runtime["version"].split(".")[0] == str(major)]
        if not candidates:
            raise ValueError(f"Install an iOS {major} simulator runtime before running smoke tests")
        selected.append(max(candidates, key=lambda value: tuple(map(int, value["version"].split(".")))))
    return selected


def main():
    data = json.loads(subprocess.check_output(["xcrun", "simctl", "list", "runtimes", "--json"], text=True))
    for runtime, device in zip(select_runtimes(data["runtimes"]), ["iPad-Pro-13-inch-M4-8GB", "iPhone-16-Pro"]):
        identifier = subprocess.check_output(["xcrun", "simctl", "create", "Gauja smoke", "com.apple.CoreSimulator.SimDeviceType." + device, runtime["identifier"]], text=True).strip()
        try:
            destination = "platform=iOS Simulator,id=" + identifier
            subprocess.run(["xcodebuild", "-project", "Gauja.xcodeproj", "-scheme", "Gauja",
                            "-destination", destination, "-derivedDataPath", "DerivedData",
                            "-skipPackagePluginValidation", "test", "CODE_SIGNING_ALLOWED=NO"], cwd=ROOT / "apps/ios", check=True)
            subprocess.run(["xcodebuild", "-scheme", "Servers", "-destination", destination,
                            "-skipPackagePluginValidation", "test"], cwd=ROOT / "apps/ios/Packages/Features/Servers", check=True)
        finally:
            subprocess.run(["xcrun", "simctl", "shutdown", identifier], check=False)
            subprocess.run(["xcrun", "simctl", "delete", identifier], check=True)


if __name__ == "__main__":
    main()
