# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Compare or replace only a generator-owned source directory."""
from pathlib import Path
import shutil


def files(directory: Path):
    return {p.relative_to(directory): p.read_bytes() for p in directory.rglob("*") if p.is_file()}


def publish(source, destination, check):
    expected, actual = files(source), files(destination)
    if not expected:
        raise ValueError("Generator produced no sources")
    if check:
        changed = sorted(str(p) for p in expected.keys() | actual.keys() if expected.get(p) != actual.get(p))
        if changed:
            raise ValueError("Generated drift in " + str(destination) + ":\n" + "\n".join(changed))
    else:
        destination.mkdir(parents=True, exist_ok=True)
        for path in actual.keys() - expected.keys():
            (destination / path).unlink()
        for path in expected:
            if (destination / path).is_dir():
                shutil.rmtree(destination / path)
        shutil.copytree(source, destination, dirs_exist_ok=True)
