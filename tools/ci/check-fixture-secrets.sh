#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# Recorded fixtures must never carry credentials (PRD §10, §14.1 `fixture-secrets`).
# Prints file:line and the pattern name only; the matched value is never echoed.
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: tools/ci/check-fixture-secrets.sh [--help] [FIXTURE_DIR]

Scans FIXTURE_DIR (default: api/fixtures) for credential patterns:
  Seerr X-Api-Key / apiKey values, connect.sid session cookies, X-Plex-Token,
  Jellyfin AccessToken / MediaBrowser tokens, VAPID keys, PEM private keys,
  and HTTP Basic authorization headers.
Exits 0 when the directory does not exist yet; 1 when any pattern matches.
USAGE
}

dir="api/fixtures"
for arg in "$@"; do
  case "$arg" in
    --help|-h) usage; exit 0 ;;
    *) dir="$arg" ;;
  esac
done

if [[ ! -d "$dir" ]]; then
  echo "fixture-secrets: $dir does not exist yet; nothing to scan."
  exit 0
fi

# name|extended-regex (case-insensitive). Placeholders such as REDACTED, <redacted>,
# *** or empty strings do not match because every value class requires real key material.
patterns=(
  'x-api-key|x-api-key["'"'"']?[[:space:]]*[:=][[:space:]]*["'"'"']?[A-Za-z0-9+/=_-]{16,}'
  'seerr-apiKey|"apikey"[[:space:]]*:[[:space:]]*"[A-Za-z0-9+/=_-]{16,}"'
  'connect.sid|connect\.sid[[:space:]]*[:=][[:space:]]*["'"'"']?s(%3A|:)[A-Za-z0-9%._-]{20,}'
  'x-plex-token|x-plex-token["'"'"']?[[:space:]]*[:=][[:space:]]*["'"'"']?[A-Za-z0-9_-]{16,}'
  'plex-authToken|"authtoken"[[:space:]]*:[[:space:]]*"[A-Za-z0-9_-]{16,}"'
  'jellyfin-AccessToken|accesstoken["'"'"']?[[:space:]]*[:=][[:space:]]*["'"'"']?[A-Fa-f0-9]{32}'
  'jellyfin-MediaBrowser|mediabrowser.*token="?[A-Fa-f0-9]{32}'
  'jellyfin-x-emby-token|x-(emby|mediabrowser)-token["'"'"']?[[:space:]]*[:=][[:space:]]*["'"'"']?[A-Fa-f0-9]{32}'
  'vapid-key|vapid(private|public)?key["'"'"']?[[:space:]]*[:=][[:space:]]*["'"'"']?[A-Za-z0-9_-]{40,}'
  'pem-private-key|-----BEGIN [A-Z ]*PRIVATE KEY-----'
  'basic-authorization|authorization["'"'"']?[[:space:]]*[:=][[:space:]]*["'"'"']?basic [A-Za-z0-9+/=]{8,}'
)

status=0
for entry in "${patterns[@]}"; do
  name="${entry%%|*}"
  regex="${entry#*|}"
  # -I skips binaries; -n gives line numbers; cut drops the matched text so no value is printed.
  hits="$(grep -rEIin -e "$regex" -- "$dir" 2>/dev/null | cut -d: -f1,2 || true)"
  if [[ -n "$hits" ]]; then
    status=1
    while IFS= read -r hit; do
      echo "fixture-secrets: $hit: matches credential pattern '$name'" >&2
    done <<<"$hits"
  fi
done

if [[ $status -ne 0 ]]; then
  echo "fixture-secrets: scrub the values above before committing (PRD §10)." >&2
  exit 1
fi
echo "fixture-secrets: no credentials found under $dir."
