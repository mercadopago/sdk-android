#!/usr/bin/env bash
set -euo pipefail

# Verifies that each library module protects, in its consumer-rules.pro, the classes that Gson and
# Retrofit reach only via reflection. consumer-rules.pro is what R8 applies in the integrator's
# release build; the module's own proguard-rules.pro is NOT propagated, so keeps placed only there
# do not protect integrators (root cause of the mp-extended device session ClassCastException).
#
# Required per module that has Gson DTOs (@SerializedName) or Retrofit services (retrofit2.http):
#   - "-keepattributes" covering *Annotation* (Gson reads @SerializedName) and Signature
#     (Retrofit/Gson resolve generic types such as Response<T>)
#   - a package-wide "-keep class|interface <pkg>.**" (or "<pkg>.*") for every such package
#
# Known gaps are listed in the baseline file and only reported; any gap not in the baseline fails.
# Baseline entries that are already fixed are reported so they can be removed.
#
# Usage: check-consumer-rules.sh [repo_root]   (BASELINE=<file> to override the baseline path)

ROOT="${1:-$(git rev-parse --show-toplevel)}"
BASELINE="${BASELINE:-$ROOT/.circleci/consumer-rules-baseline.txt}"
SKIP_MODULES="example showkase"

# Prints the distinct packages of Kotlin/Java sources under src/main that contain the given pattern.
packages_with() {
  local module_dir="$1" pattern="$2"
  { grep -rlE "$pattern" "$module_dir/src/main" --include=*.kt --include=*.java 2>/dev/null || true; } \
    | while read -r file; do
        sed -n 's/^package[[:space:]]\{1,\}\([A-Za-z0-9_.]*\).*/\1/p' "$file" | head -1
      done \
    | sort -u
}

# Prints the class patterns of keep rules, without comments (e.g. "com.foo.data.remote.**").
keep_patterns() {
  sed 's/#.*//' "$1" \
    | grep -E '^[[:space:]]*-keep[a-z]*' \
    | sed -E 's/^[[:space:]]*-keep[a-z]*(,[a-z]+)*[[:space:]]+(@[^[:space:]]+[[:space:]]+)?(class|interface|enum)[[:space:]]+([^[:space:]{]+).*/\4/' \
    | grep -E '\.\*\*?$' || true
}

# Returns 0 when a keep pattern covers the whole package.
package_covered() {
  local package="$1" patterns="$2" pattern prefix
  for pattern in $patterns; do
    case "$pattern" in
      *.\*\*)
        prefix="${pattern%.\*\*}"
        if [ "$package" = "$prefix" ] || [ "${package#"$prefix".}" != "$package" ]; then return 0; fi
        ;;
      *.\*)
        [ "$package" = "${pattern%.\*}" ] && return 0
        ;;
    esac
  done
  return 1
}

has_attribute() {
  sed 's/#.*//' "$1" | grep -E '^[[:space:]]*-keepattributes' | grep -qF -- "$2"
}

in_baseline() {
  [ -f "$BASELINE" ] && grep -qxF -- "$1" "$BASELINE"
}

FOUND=""
NEW_GAPS=""
KNOWN_GAPS=0

record_gap() {
  local gap="$1"
  FOUND="$FOUND$gap"$'\n'
  if in_baseline "$gap"; then
    KNOWN_GAPS=$((KNOWN_GAPS + 1))
  else
    NEW_GAPS="$NEW_GAPS  ✗ $gap"$'\n'
  fi
}

for rules in "$ROOT"/*/consumer-rules.pro; do
  [ -f "$rules" ] || continue
  module_dir="$(dirname "$rules")"
  module="$(basename "$module_dir")"
  case " $SKIP_MODULES " in *" $module "*) continue ;; esac
  [ -d "$module_dir/src/main" ] || continue

  packages=$( { packages_with "$module_dir" '@SerializedName'; packages_with "$module_dir" 'retrofit2\.http\.'; } | sort -u)
  if [ -z "$packages" ]; then
    echo "    $module: no Gson DTOs or Retrofit services, skipping"
    continue
  fi

  patterns=$(keep_patterns "$rules")
  for attribute in '*Annotation*' 'Signature'; do
    has_attribute "$rules" "$attribute" || record_gap "$module:keepattributes $attribute"
  done
  for package in $packages; do
    package_covered "$package" "$patterns" || record_gap "$module:keep $package"
  done
  echo "    $module: $(echo "$packages" | grep -c '.') reflected package(s) checked"
done

STALE=""
if [ -f "$BASELINE" ]; then
  while read -r entry; do
    case "$entry" in ''|\#*) continue ;; esac
    printf '%s' "$FOUND" | grep -qxF -- "$entry" || STALE="$STALE  - $entry"$'\n'
  done < "$BASELINE"
fi

if [ "$KNOWN_GAPS" -gt 0 ]; then
  echo "  ⚠️  $KNOWN_GAPS known gap(s) listed in $(basename "$BASELINE") — fix them and remove the entries."
fi
if [ -n "$STALE" ]; then
  printf "  ℹ️  Baseline entries already fixed, remove them from %s:\n%s" "$(basename "$BASELINE")" "$STALE"
fi

if [ -n "$NEW_GAPS" ]; then
  printf "CONSUMER RULES CHECK FAILED — classes reached by Gson/Retrofit reflection are not kept in consumer-rules.pro:\n%s" "$NEW_GAPS"
  echo "Add the keep rules to the module's consumer-rules.pro (proguard-rules.pro is not applied to integrators)."
  exit 1
fi

echo "Consumer rules check passed."
