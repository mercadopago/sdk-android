#!/usr/bin/env bash
set -euo pipefail

STAGED=$(git diff --cached --name-only | grep '^ux-reference/deliveries/' || true)
[ -z "$STAGED" ] && exit 0

git fetch origin main --quiet 2>/dev/null || true

BLOCKED=""
while IFS= read -r file; do
  if git ls-tree -r origin/main --name-only | grep -qF "$file"; then
    BLOCKED="$BLOCKED\n  Attempted to modify: $file"
  fi
done <<< "$STAGED"

if [ -n "$BLOCKED" ]; then
  echo ""
  echo "[ux-reference] BLOCKED: Modifications to existing UX deliveries are not allowed."
  echo -e "$BLOCKED"
  echo ""
  echo "  UX deliveries are immutable after merge to main."
  echo "  To update a screen, create a new dated delivery directory instead."
  echo ""
  exit 1
fi
