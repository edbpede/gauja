#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Dependency license allow-list (PRD §10, §14.1 `license-check`), driven by deny.toml.
# Phase 1 scope: validates deny.toml and exits 0 while no manifests exist. Resolving each
# dependency's license from the Gradle catalog and Package.resolved lands in Phase 3.
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: tools/ci/check-licenses.sh [--help] [--deny FILE] [MANIFEST...]

  --deny FILE   allow-list file (default: deny.toml)
  MANIFEST      apps/android/gradle/libs.versions.toml, **/Package.resolved
                (default: discovered under apps/)
Exits 1 when deny.toml is missing or has an empty [licenses] allow list;
exits 0 when there are no manifests yet.
USAGE
}

deny="deny.toml"
manifests=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    --help|-h) usage; exit 0 ;;
    --deny) [[ $# -ge 2 ]] || { usage >&2; exit 2; }; deny="$2"; shift 2 ;;
    --deny=*) deny="${1#--deny=}"; shift ;;
    *) manifests+=("$1"); shift ;;
  esac
done

if [[ ! -f "$deny" ]]; then
  echo "license-check: $deny not found." >&2
  exit 1
fi

# Extract the quoted entries of `allow = [...]` inside the [licenses] table.
allowed="$(awk '
  /^\[/ { in_licenses = ($0 == "[licenses]") }
  in_licenses && /^[[:space:]]*allow[[:space:]]*=/ { in_allow = 1 }
  in_allow {
    line = $0
    while (match(line, /"[^"]+"/)) { print substr(line, RSTART + 1, RLENGTH - 2); line = substr(line, RSTART + RLENGTH) }
    if (line ~ /\]/) { in_allow = 0 }
  }
' "$deny")"
if [[ -z "$allowed" ]]; then
  echo "license-check: $deny has no [licenses] allow list." >&2
  exit 1
fi

if [[ ${#manifests[@]} -eq 0 && -d apps ]]; then
  while IFS= read -r m; do manifests+=("$m"); done < <(find apps -type f \( -name 'libs.versions.toml' -o -name 'Package.resolved' \) | grep -v '/\.build/' || true)
fi
if [[ ${#manifests[@]} -eq 0 ]]; then
  echo "license-check: allow-list ok ($(wc -l <<<"$allowed" | tr -d ' ') licenses); no manifests yet."
  exit 0
fi

echo "license-check: allow-list ok; ${#manifests[@]} manifest(s) found. Per-dependency resolution lands in Phase 3."
exit 0
