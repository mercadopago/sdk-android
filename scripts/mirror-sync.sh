#!/bin/bash
# mirror-sync.sh - Mirrors branches from fury repo to public repo

set -euo pipefail

DEST_REPO="git@github.com:${DESTINATION_REPOSITORY_USERNAME}/${DESTINATION_REPOSITORY_NAME}.git"

# Strip refs/heads/ prefix if present
BRANCH_NAME="${BRANCH_NAME#refs/heads/}"

echo "📌 Event: ${EVENT_NAME} | Branch: ${BRANCH_NAME}"

# Configure git
git config --global user.name "${GIT_USER_NAME}"
git config --global user.email "${GIT_USER_EMAIL}"

# Handle branch deletion
if [ "${EVENT_NAME}" = "delete" ]; then
  echo "🗑️ Deleting branch ${BRANCH_NAME} from mirror..."
  git push "${DEST_REPO}" --delete "${BRANCH_NAME}" 2>/dev/null || echo "⚠️ Branch ${BRANCH_NAME} not found in mirror"
  echo "✅ Done"
  exit 0
fi

# Clone destination repository
CLONE_DIR=$(mktemp -d)
DEST_DIR="${CLONE_DIR}/repo"

echo "📋 Cloning destination repository..."
git clone "${DEST_REPO}" "${DEST_DIR}"

# Check if branch exists in destination
cd "${DEST_DIR}"
if git ls-remote --exit-code origin "${BRANCH_NAME}" >/dev/null 2>&1; then
  echo "📌 Branch ${BRANCH_NAME} exists, checking out..."
  git checkout "${BRANCH_NAME}"
else
  echo "📌 Creating new branch ${BRANCH_NAME}..."
  git checkout -b "${BRANCH_NAME}"
fi

# Save .git directory
mv "${DEST_DIR}/.git" "${CLONE_DIR}/.git-backup"

# Remove all content from destination
rm -rf "${DEST_DIR}"
mkdir -p "${DEST_DIR}"

# Restore .git directory
mv "${CLONE_DIR}/.git-backup" "${DEST_DIR}/.git"

# Copy source files (exclude .git and .github only)
echo "📦 Syncing files..."
cd "${GITHUB_WORKSPACE}"
rsync -a \
  --exclude='.git' \
  --exclude='.github' \
  ./ "${DEST_DIR}/"

# Commit and push
cd "${DEST_DIR}"
git add -A

if git diff --cached --quiet; then
  echo "ℹ️ No changes to sync"
  exit 0
fi

git commit -m "Sync ${BRANCH_NAME} from fury_openplatform-sdk-android"

# Protected branches (like main) require PRs, push to a sync/ branch instead
PUSH_BRANCH="${BRANCH_NAME}"
if [ "${BRANCH_NAME}" = "main" ] || [ "${BRANCH_NAME}" = "master" ]; then
  PUSH_BRANCH="sync/main-$(date +%Y%m%d-%H%M%S)"
  git checkout -b "${PUSH_BRANCH}"
  echo "📤 Pushing ${PUSH_BRANCH} to mirror (main is protected)..."
else
  echo "📤 Pushing ${BRANCH_NAME} to mirror..."
fi
git push origin "${PUSH_BRANCH}"

# Cleanup
rm -rf "${CLONE_DIR}"

echo "✅ Branch ${BRANCH_NAME} synced to ${DESTINATION_REPOSITORY_USERNAME}/${DESTINATION_REPOSITORY_NAME}"
