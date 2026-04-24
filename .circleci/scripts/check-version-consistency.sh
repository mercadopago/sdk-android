#!/usr/bin/env bash
set -euo pipefail

BUILDSCR="buildSrc/src/main/kotlin/com/mercadopago/sdk/android"
MAVEN_BASE="https://artifacts.mercadolibre.com/service/rest/repository/browse/android-releases/com/mercadopago/android/sdk"

config_to_module() {
  case "$1" in
    CoreSDKConfig.kt)        echo "core" ;;
    AnalyticsSDKConfig.kt)   echo "analytics" ;;
    MercadoPagoSDKConfig.kt) echo "sdk-android" ;;
    CoreMethodsSDKConfig.kt) echo "core-methods" ;;
    FoundationSDKConfig.kt)  echo "foundation" ;;
    ComponentsSDKConfig.kt)  echo "components" ;;
    CheckoutSDKConfig.kt)    echo "checkout" ;;
    MPExtendedSDKConfig.kt)  echo "mp-extended" ;;
    BomConfig.kt)            echo "sdk-android-bom" ;;
  esac
}

project_ref_to_module() {
  case "$1" in
    core)        echo "core" ;;
    analytics)   echo "analytics" ;;
    sdkAndroid)  echo "sdk-android" ;;
    coreMethods) echo "core-methods" ;;
    foundation)  echo "foundation" ;;
    components)  echo "components" ;;
    checkout)    echo "checkout" ;;
    mpExtended)  echo "mp-extended" ;;
  esac
}

module_version() {
  grep -oP '(?<=VERSION_NAME = ")[^"]+' "$BUILDSCR/${1}" 2>/dev/null
}

artifact_exists() {
  curl -sfI "$MAVEN_BASE/$1/$2/" > /dev/null 2>&1
}

CHANGED_MODULES=""
for config in CoreSDKConfig.kt AnalyticsSDKConfig.kt MercadoPagoSDKConfig.kt \
              CoreMethodsSDKConfig.kt FoundationSDKConfig.kt ComponentsSDKConfig.kt \
              CheckoutSDKConfig.kt MPExtendedSDKConfig.kt BomConfig.kt; do
  module=$(config_to_module "$config")
  version=$(module_version "$config")
  if git diff HEAD~1 HEAD -- "$BUILDSCR/$config" 2>/dev/null | grep -q '^\+.*VERSION_NAME'; then
    echo "  → $module: VERSION_NAME bumped in diff"
    CHANGED_MODULES="$CHANGED_MODULES $module"
  elif [ -n "$version" ] && ! artifact_exists "$module" "$version"; then
    echo "  → $module: version $version not found in Maven, adding to check"
    CHANGED_MODULES="$CHANGED_MODULES $module"
  fi
done

if [ -z "$(echo $CHANGED_MODULES | tr -d ' ')" ]; then
  echo "No version changes detected and all modules already published, skipping consistency check."
  exit 0
fi

echo "Modules with version changes: $CHANGED_MODULES"

declare -A DEPENDENTS
for kts in */build.gradle.kts; do
  MODULE="${kts%%/*}"
  [[ "$MODULE" == "example" || "$MODULE" == "showkase" ]] && continue
  for ref in $(grep -oP '(?<=projects\.)[a-zA-Z]+' "$kts" 2>/dev/null); do
    dep=$(project_ref_to_module "$ref")
    [ -n "$dep" ] && DEPENDENTS["$dep"]="${DEPENDENTS[$dep]} $MODULE"
  done
done

ERRORS=""
for changed in $CHANGED_MODULES; do
  for dependent in ${DEPENDENTS[$changed]}; do
    if ! echo "$CHANGED_MODULES" | grep -qw "$dependent"; then
      ERRORS="$ERRORS\n  ✗ '$dependent' depends on '$changed' but VERSION_NAME was not bumped"
    fi
  done
done

if [ -n "$ERRORS" ]; then
  printf "VERSION CONSISTENCY ERROR:%b\n" "$ERRORS"
  exit 1
fi

echo "Version consistency check passed."
