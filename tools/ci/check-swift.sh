#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$root/apps/ios"
python3 - <<'PYTHON'
from pathlib import Path
import subprocess
files = [str(p) for p in Path('.').rglob('*.swift') if not set(p.parts) & {'Generated', '.build', 'DerivedData', 'build'}]
subprocess.run(['swift', 'format', 'lint', '--strict', '--configuration', '.swift-format', *files], check=True)
PYTHON
swiftlint lint --strict --quiet --config .swiftlint.yml
swiftlint lint --lenient --quiet --config .swiftlint-advisory.yml
