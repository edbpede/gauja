# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
"""Read the generator version pins."""
from pathlib import Path


def versions():
    path = Path(__file__).with_name("versions.env")
    return dict(line.split("=", 1) for line in path.read_text().splitlines()
                if line and not line.startswith("#"))
