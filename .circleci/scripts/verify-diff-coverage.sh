#!/usr/bin/env bash
set -euo pipefail

# python3-pip not shipped in cimg/openjdk:17.0.13 (~15s overhead, no cache available in this image)
sudo apt-get update -qq && sudo apt-get install -y python3-pip --no-install-recommends -qq
pip3 install "diff-cover==10.2.0" --quiet

if ! git fetch origin main 2>&1; then
  echo "Warning: git fetch origin main failed — diff-cover may compare against a stale branch"
fi

REPORTS=$(find . -path "*/build/reports/kover*" -name "*.xml" | tr '\n' ' ')
if [ -z "$(echo $REPORTS | tr -d ' ')" ]; then
  echo "No Kover XML reports found, skipping diff coverage check."
  exit 0
fi

echo "Coverage reports found: $REPORTS"
diff-cover $REPORTS --compare-branch=origin/main --diff-range-notation=.. --fail-under=80
