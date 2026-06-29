#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

ORG_GRADLE_PROJECT_sdkAndroidUsername=$(echo "$ORG_GRADLE_PROJECT_sdkAndroidUsername" | base64 --decode)
ORG_GRADLE_PROJECT_sdkAndroidPassword=$(echo "$ORG_GRADLE_PROJECT_sdkAndroidPassword" | base64 --decode)
export ORG_GRADLE_PROJECT_sdkAndroidUsername
export ORG_GRADLE_PROJECT_sdkAndroidPassword

readarray -t MODULES <<< "$(bom_published_modules | tr ' ' '\n' | grep -v '^$')"

any_published=0
for MODULE in "${MODULES[@]}"; do
  VERSION=$(./gradlew -q ":$MODULE:properties" | grep "^version:" | awk '{print $2}')
  if artifact_exists "$MODULE" "$VERSION"; then
    echo "Module $MODULE: version $VERSION already exists, skipping publish for this module."
  else
    echo "Publishing $MODULE:$VERSION ..."
    ./gradlew ":$MODULE:publish" || {
      echo "Publish failed for $MODULE"
      exit 1
    }
    any_published=1
  fi
done

if [[ $any_published -eq 0 ]]; then
  echo "No modules needed to be published. Skipping release creation."
  circleci-agent step halt
else
  echo "export SHOULD_CREATE_RELEASE=true" >> "$BASH_ENV"
fi
