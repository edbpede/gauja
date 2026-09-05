#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Check evaluated native dependency graphs against the modularity responsibility map."""
import argparse
import json
from pathlib import Path
import re
import subprocess

import yaml


CORE = {
    "data": {"api", "database", "datastore", "network", "model", "common", "compat"},
    "network": {"common", "model", "datastore"},
    "database": {"model", "common"}, "datastore": {"model", "common"},
    "compat": {"model", "common"}, "ui": {"designsystem", "model", "common"},
    "designsystem": {"model"}, "navigation": {"model"}, "common": {"model"},
    "model": set(), "api": set(),
}
FEATURE = {"ui", "designsystem", "data", "navigation", "common", "compat", "model"}
IOS_NAMES = {"SeerrAPI": "api", "Persistence": "datastore"}


def allowed(source, target, test=False):
    if target == source:
        return test
    if target == "app":
        return False
    if target == "core/testing":
        return test
    if source == "core/testing":
        return target.startswith("core/")
    if source == "app":
        return target.startswith("feature/") or target in {
            "core/" + name for name in FEATURE | {"datastore"}}
    if source.startswith("feature/"):
        return target in {"core/" + name for name in FEATURE}
    return target in {"core/" + name for name in CORE.get(source.removeprefix("core/"), set())}


def android(root):
    graph = json.loads((root / "apps/android/build/reports/module-graph.json").read_text())
    for module in graph:
        source = module["name"].strip(":").replace(":", "/")
        for edge in module["edges"]:
            target = edge["target"].strip(":").replace(":", "/")
            if not allowed(source, target, "test" in edge["scope"].lower()):
                yield f"{source} -> {target} ({edge['scope']})"


def ios_name(path):
    return "feature/" + path.name.lower() if path.parent.name == "Features" else "core/" + IOS_NAMES.get(path.name, path.name.lower())


def ios(root):
    tree = root / "apps/ios"
    manifests = sorted((tree / "Packages").glob("*/Package.swift"))
    manifests += sorted((tree / "Packages/Features").glob("*/Package.swift"))
    identities = {p.parent.name: ios_name(p.parent) for p in manifests}
    for manifest in manifests:
        description = json.loads(subprocess.check_output(
            ["swift", "package", "--package-path", str(manifest.parent), "dump-package"], text=True))
        source = ios_name(manifest.parent)
        for target in description["targets"]:
            for dependency in target["dependencies"]:
                name = next(iter(dependency.values()))[0]
                if name in identities and not allowed(source, identities[name], target["type"] == "test"):
                    yield f"{source} -> {identities[name]} ({target['name']})"
        for dependency in description["dependencies"]:
            for local in dependency.get("fileSystem", []):
                path = Path(local["path"]).resolve()
                if not path.is_relative_to((tree / "Packages").resolve()):
                    yield f"{source}: local dependency outside Packages"
    project = yaml.safe_load((tree / "project.yml").read_text())
    for target in project["targets"].values():
        if target["type"] != "application":
            continue
        for dependency in target.get("dependencies", []):
            package = project["packages"].get(dependency.get("package"), {})
            if "path" in package and not allowed("app", ios_name(Path(package["path"]))):
                yield f"app -> {package['path']}"


def imports(root, platform):
    tree = root / "apps" / platform
    extension = "kt" if platform == "android" else "swift"
    for path in tree.rglob("*." + extension):
        relative = path.relative_to(tree)
        if any(p in {"build", "build-logic", ".build", ".gradle", "DerivedData", "Generated", "generated"} for p in relative.parts):
            continue
        text = path.read_text()
        if platform == "android":
            source = "/".join(relative.parts[:2])
            if source == "core/model" and re.search(r"^import (android\.|androidx\.)", text, re.M):
                yield f"{relative}: platform import in Model"
            for match in re.finditer(r"^import app\.gauja\.feature\.([a-z]+)", text, re.M):
                if source.startswith("feature/") and source != "feature/" + match[1]:
                    yield f"{relative}: cross-feature import"
        elif relative.parts[:2] == ("Packages", "Model"):
            if re.search(r"^\s*(?:(?:public|internal|private|@testable)\s+)?import (UIKit|SwiftUI|SwiftData|AppKit)\b", text, re.M):
                yield f"{relative}: platform import in Model"
        elif relative.parts[:2] == ("Packages", "Features"):
            own = relative.parts[2]
            features = {p.name for p in (tree / "Packages/Features").iterdir() if p.is_dir()}
            for match in re.finditer(r"^(?:(?:public|internal|private|@testable)\s+)?import ([A-Za-z]+)", text, re.M):
                if match[1] in features - {own}:
                    yield f"{relative}: cross-feature import"


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("platform", choices=["android", "ios"])
    parser.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[2])
    args = parser.parse_args()
    errors = list((android if args.platform == "android" else ios)(args.root))
    errors.extend(imports(args.root, args.platform))
    if errors:
        parser.exit(1, "module-graph:\n" + "\n".join(errors) + "\n")
    print(f"module-graph: {args.platform} valid")


if __name__ == "__main__":
    main()
