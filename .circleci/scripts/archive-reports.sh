#!/usr/bin/env bash
set -euo pipefail

mkdir -p ~/circleci-artifacts/reports
for module in */; do
  [ -d "${module}build/reports/detekt" ] && cp -r "${module}build/reports/detekt" ~/circleci-artifacts/reports/${module%/}-detekt || true
  [ -d "${module}build/reports/ktlint" ] && cp -r "${module}build/reports/ktlint" ~/circleci-artifacts/reports/${module%/}-ktlint || true
  [ -d "${module}build/reports/kover"  ] && cp -r "${module}build/reports/kover"  ~/circleci-artifacts/reports/${module%/}-kover  || true
done
