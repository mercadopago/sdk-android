#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=lib.sh
source "$SCRIPT_DIR/lib.sh"

BUILDSCR="buildSrc/src/main/kotlin/com/mercadopago/sdk/android"

module_version() {
  grep -oP '(?<=VERSION_NAME = ")[^"]+' "$BUILDSCR/${1}" 2>/dev/null
}

PUBLISHED_MODULES=$(bom_published_modules)

CHANGED_MODULES=""
for config in CoreSDKConfig.kt AnalyticsSDKConfig.kt MercadoPagoSDKConfig.kt \
              CoreMethodsSDKConfig.kt FoundationSDKConfig.kt ComponentsSDKConfig.kt \
              CheckoutSDKConfig.kt MPExtendedSDKConfig.kt BomConfig.kt; do
  module=$(config_to_module "$config")
  echo "$PUBLISHED_MODULES" | grep -qw "$module" || continue
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
    [ -n "$dep" ] && DEPENDENTS["$dep"]="${DEPENDENTS[$dep]:-} $MODULE"
  done
done

ERRORS=""
for changed in $CHANGED_MODULES; do
  for dependent in ${DEPENDENTS[$changed]:-}; do
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
