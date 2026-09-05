#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Gauja contributors
# SPDX-License-Identifier: AGPL-3.0-or-later
#
# CI mirror of the two commit-msg hooks in prek.toml (conventional-pre-commit, dco-signoff),
# which `prek run --all-files` cannot exercise. This job is the project's DCO check: every
# commit in the range needs a Conventional Commit subject and a Signed-off-by trailer whose
# email matches the author or the committer (PRD §14.2, §15.3). Optionally checks the PR title.
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: tools/ci/check-commit-messages.sh [--help] [--title TITLE] BASE HEAD

Every non-merge commit in BASE..HEAD must have a Conventional Commit subject
and a `Signed-off-by: Name <email>` trailer (DCO 1.1) whose email matches the commit's
author or committer email. --title checks a PR title against the same subject rule.
USAGE
}

conventional='^(build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test)(\([A-Za-z0-9._/ -]+\))?!?: [^ ].*$'
signoff='^Signed-off-by: .+ <.+@.+>$'

title=""
positional=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    --help|-h) usage; exit 0 ;;
    --title) title="$2"; shift 2 ;;
    --title=*) title="${1#--title=}"; shift ;;
    *) positional+=("$1"); shift ;;
  esac
done

status=0
if [[ -n "$title" ]] && ! grep -Eq "$conventional" <<<"$title"; then
  echo "commit-messages: PR title is not a Conventional Commit subject: '$title'" >&2
  status=1
fi

if [[ ${#positional[@]} -eq 2 ]]; then
  base="${positional[0]}"; head="${positional[1]}"
  while IFS= read -r sha; do
    [[ -z "$sha" ]] && continue
    subject="$(git log -1 --format=%s "$sha")"
    body="$(git log -1 --format=%B "$sha")"
    if ! grep -Eq "$conventional" <<<"$subject"; then
      echo "commit-messages: ${sha:0:12} subject is not Conventional: '$subject'" >&2
      status=1
    fi
    if ! grep -Eq "$signoff" <<<"$body"; then
      echo "commit-messages: ${sha:0:12} is missing a Signed-off-by trailer (git commit -s)" >&2
      status=1
    else
      author="$(git log -1 --format=%ae "$sha")"
      committer="$(git log -1 --format=%ce "$sha")"
      if ! grep -E "$signoff" <<<"$body" | grep -Fq -e "<$author>" -e "<$committer>"; then
        echo "commit-messages: ${sha:0:12} Signed-off-by email matches neither author <$author> nor committer <$committer>" >&2
        status=1
      fi
    fi
  done < <(git rev-list --no-merges "$base..$head")
elif [[ ${#positional[@]} -ne 0 ]]; then
  usage >&2
  exit 2
fi

[[ $status -eq 0 ]] && echo "commit-messages: ok."
exit $status
