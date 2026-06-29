#!/usr/bin/env bash

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
  echo "$1" | sed 's/[A-Z]/-&/g' | tr '[:upper:]' '[:lower:]'
}

artifact_exists() {
  curl -sfI "$MAVEN_BASE/$1/$2/" > /dev/null 2>&1
}

# Returns space-separated list of published modules derived from BOM constraints,
# with sdk-android-bom appended last.
bom_published_modules() {
  local root
  root="$(git rev-parse --show-toplevel)"
  local modules=""
  for ref in $(grep -oP '(?<=api\(projects\.)[a-zA-Z]+' "$root/sdk-android-bom/build.gradle.kts" | awk '!seen[$0]++'); do
    local mod
    mod=$(project_ref_to_module "$ref")
    [ -n "$mod" ] && modules="$modules $mod"
  done
  echo "$modules sdk-android-bom"
}
