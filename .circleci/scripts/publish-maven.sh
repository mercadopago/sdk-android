#!/usr/bin/env bash
set -euo pipefail

ORG_GRADLE_PROJECT_sdkAndroidUsername=$(echo "$ORG_GRADLE_PROJECT_sdkAndroidUsername" | base64 --decode)
ORG_GRADLE_PROJECT_sdkAndroidPassword=$(echo "$ORG_GRADLE_PROJECT_sdkAndroidPassword" | base64 --decode)
export ORG_GRADLE_PROJECT_sdkAndroidUsername
export ORG_GRADLE_PROJECT_sdkAndroidPassword

MAVEN_BASE="https://artifacts.mercadolibre.com/service/rest/repository/browse/android-releases/com/mercadopago/android/sdk"
MODULES=("core" "analytics" "sdk-android" "core-methods" "sdk-android-bom" "mp-extended")

any_published=0
for MODULE in "${MODULES[@]}"; do
  VERSION=$(./gradlew -q ":$MODULE:properties" | grep "^version:" | awk '{print $2}')
  ARTIFACT_URL="$MAVEN_BASE/$MODULE/$VERSION/"
  if curl -sfI "$ARTIFACT_URL" > /dev/null; then
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
