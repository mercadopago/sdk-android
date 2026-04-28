#!/usr/bin/env bash

git fetch origin main --quiet 2>/dev/null || true

CHANGELOG_CHANGED=$(git diff origin/main...HEAD --name-only 2>/dev/null | grep -c "^CHANGELOG.md$")

if [ "$CHANGELOG_CHANGED" -eq 0 ]; then
  echo "⚠️  WARNING: CHANGELOG.md não foi atualizado nessa branch."
  echo "   Por favor, atualize o CHANGELOG.md antes de fazer merge para main."
else
  echo "✅ CHANGELOG.md foi atualizado."
fi
