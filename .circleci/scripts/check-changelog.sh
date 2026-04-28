#!/usr/bin/env bash
set -uo pipefail

if ! git fetch origin main --quiet 2>/dev/null; then
  echo "WARNING: Could not fetch origin/main. Skipping CHANGELOG.md check."
  exit 0
fi

CHANGELOG_CHANGED=$(git diff origin/main...HEAD --name-only 2>/dev/null | grep -c "^CHANGELOG.md$")

if [ "$CHANGELOG_CHANGED" -eq 0 ]; then
  echo "WARNING: CHANGELOG.md has not been updated in this branch."
  echo "         Please update CHANGELOG.md before merging to main."
else
  echo "OK: CHANGELOG.md has been updated."
fi
