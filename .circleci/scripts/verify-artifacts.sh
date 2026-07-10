#!/usr/bin/env bash
set -euo pipefail

RELEASE_DIR="${1:-$HOME/artifacts/aars/release}"
DEBUG_DIR="${2:-$HOME/artifacts/aars/debug}"
ERRORS=""

# Extracts the sorted list of .class entries from an AAR's classes.jar.
list_classes() {
  local aar="$1"
  local temp_dir
  temp_dir=$(mktemp -d)
  unzip -q "$aar" classes.jar -d "$temp_dir" 2>/dev/null || true
  if [ -f "$temp_dir/classes.jar" ]; then
    unzip -l "$temp_dir/classes.jar" 2>/dev/null | grep '\.class$' | awk '{print $NF}' | sort
  fi
  rm -rf "$temp_dir"
}

for release_aar in "$RELEASE_DIR"/*.aar; do
  [ -f "$release_aar" ] || continue
  module=$(basename "$release_aar" -release.aar)
  debug_aar="$DEBUG_DIR/${module}-debug.aar"

  if [ ! -f "$debug_aar" ]; then
    echo "  ⚠️  $module: no debug AAR found to compare, skipping"
    continue
  fi

  release_classes=$(list_classes "$release_aar")
  debug_classes=$(list_classes "$debug_aar")

  release_count=$(echo "$release_classes" | grep -c '.' || echo "0")
  debug_count=$(echo "$debug_classes" | grep -c '.' || echo "0")

  # Classes present in release but missing from debug indicate a build problem.
  # Classes present in debug but absent from release are expected (e.g. Showkase
  # KSP-generated previews, Compose tooling) — these must NOT trigger a failure.
  missing=$(comm -23 <(echo "$release_classes") <(echo "$debug_classes") | wc -l | tr -d ' ')

  ratio=$(( debug_count > 0 ? release_count * 100 / debug_count : 0 ))
  echo "    $module: release=$release_count debug=$debug_count ratio=${ratio}% missing_from_debug=$missing"

  if [ "$missing" -gt 0 ]; then
    ERRORS="$ERRORS\n  ✗ '$module': $missing release class(es) not found in debug build — possible build misconfiguration"
  fi
done

if [ -n "$ERRORS" ]; then
  printf "ARTIFACT VERIFICATION FAILED:%b\n" "$ERRORS"
  exit 1
fi

echo "All artifacts verified successfully."
