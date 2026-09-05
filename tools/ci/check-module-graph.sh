#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
set -euo pipefail
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
"$root/apps/android/gradlew" --project-dir "$root/apps/android" exportModuleGraph --quiet
exec "$root/tools/contract/python.sh" "$root/tools/ci/module_graph.py" android --root "$root"
