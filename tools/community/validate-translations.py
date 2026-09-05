#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Validate translation catalogs (PRD §16, §14.1 `translations`).

Checks Android `strings.xml` catalogs (well-formed XML, no duplicate keys, locale
catalogs never define keys the default catalog lacks) and iOS `*.xcstrings` catalogs
(valid JSON with a top-level `strings` object), then key parity between the Android
default catalog and the iOS catalog. Exits 0 when no catalogs exist yet.
Standard library only.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ANDROID_RES = re.compile(r"/res/values(-[^/]+)?/strings\.xml$")


def android_catalogs(root: Path) -> list[Path]:
    android = root / "apps" / "android"
    if not android.is_dir():
        return []
    return sorted(p for p in android.rglob("strings.xml") if ANDROID_RES.search(p.as_posix()))


def ios_catalogs(root: Path) -> list[Path]:
    ios = root / "apps" / "ios"
    if not ios.is_dir():
        return []
    return sorted(p for p in ios.rglob("*.xcstrings") if ".build" not in p.parts)


def android_keys(path: Path, errors: list[str]) -> set[str]:
    try:
        tree = ET.parse(path)
    except ET.ParseError as exc:
        errors.append(f"{path}: not well-formed XML: {exc}")
        return set()
    keys: set[str] = set()
    for element in tree.getroot():
        if element.tag not in {"string", "plurals", "string-array"}:
            continue
        name = element.get("name")
        if not name:
            errors.append(f"{path}: <{element.tag}> without a name attribute")
            continue
        if name in keys:
            errors.append(f"{path}: duplicate key '{name}'")
        keys.add(name)
    return keys


def ios_keys(path: Path, errors: list[str]) -> set[str]:
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as exc:
        errors.append(f"{path}: not valid JSON: {exc}")
        return set()
    if not isinstance(data, dict):
        errors.append(f"{path}: top-level JSON must be an object")
        return set()
    strings = data.get("strings")
    if not isinstance(strings, dict):
        errors.append(f"{path}: missing top-level 'strings' object")
        return set()
    return set(strings)


def validate(root: Path, check_parity: bool) -> list[str]:
    errors: list[str] = []
    default_android: set[str] | None = None
    android_locale_keys: dict[Path, set[str]] = {}
    for catalog in android_catalogs(root):
        keys = android_keys(catalog, errors)
        if catalog.parent.name == "values":
            default_android = keys if default_android is None else default_android | keys
        else:
            android_locale_keys[catalog] = keys
    if default_android is not None:
        for catalog, keys in android_locale_keys.items():
            for extra in sorted(keys - default_android):
                errors.append(f"{catalog}: key '{extra}' is not in the default catalog")

    ios_all: set[str] | None = None
    for catalog in ios_catalogs(root):
        keys = ios_keys(catalog, errors)
        ios_all = keys if ios_all is None else ios_all | keys

    if check_parity and default_android is not None and ios_all is not None:
        for key in sorted(default_android - ios_all):
            errors.append(f"parity: '{key}' exists on Android but not on iOS")
        for key in sorted(ios_all - default_android):
            errors.append(f"parity: '{key}' exists on iOS but not on Android")
    return errors


def main(argv: list[str]) -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--root", default=".", help="repository root (default: current directory)")
    parser.add_argument("--no-parity", action="store_true", help="skip the Android/iOS key parity check")
    args = parser.parse_args(argv)
    root = Path(args.root)

    if not android_catalogs(root) and not ios_catalogs(root):
        print("translations: no catalogs yet; nothing to validate.")
        return 0
    errors = validate(root, check_parity=not args.no_parity)
    for error in errors:
        print(f"translations: {error}", file=sys.stderr)
    if errors:
        return 1
    print("translations: catalogs valid.")
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
