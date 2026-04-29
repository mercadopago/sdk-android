#!/usr/bin/env bash
set -euo pipefail

RELEASE_DIR="${1:-$HOME/artifacts/aars/release}"
DEBUG_DIR="${2:-$HOME/artifacts/aars/debug}"
THRESHOLD=50
ERRORS=""

count_classes() {
  local aar="$1"
  local temp_dir
  temp_dir=$(mktemp -d)
  unzip -q "$aar" classes.jar -d "$temp_dir" 2>/dev/null || true
  local count=0
  if [ -f "$temp_dir/classes.jar" ]; then
    count=$(unzip -l "$temp_dir/classes.jar" 2>/dev/null | grep -c '\.class$' || echo "0")
  fi
  rm -rf "$temp_dir"
  echo "$count"
}

for release_aar in "$RELEASE_DIR"/*.aar; do
  [ -f "$release_aar" ] || continue
  module=$(basename "$release_aar" -release.aar)
  debug_aar="$DEBUG_DIR/${module}-debug.aar"

  if [ ! -f "$debug_aar" ]; then
    echo "  ⚠️  $module: no debug AAR found to compare, skipping"
    continue
  fi

  release_count=$(count_classes "$release_aar")
  debug_count=$(count_classes "$debug_aar")

  if [ "$debug_count" -eq 0 ]; then
    echo "  ⚠️  $module: debug AAR has 0 classes, skipping ratio check"
    continue
  fi

  ratio=$(( release_count * 100 / debug_count ))
  echo "  $module: release=$release_count debug=$debug_count ratio=${ratio}%"

  if [ "$ratio" -lt "$THRESHOLD" ]; then
    ERRORS="$ERRORS\n  ✗ '$module' release has only ${ratio}% of debug classes (${release_count}/${debug_count}) — possible ProGuard misconfiguration"
  fi
done

if [ -n "$ERRORS" ]; then
  printf "ARTIFACT VERIFICATION FAILED:%b\n" "$ERRORS"
  exit 1
fi

echo "All artifacts verified successfully."
