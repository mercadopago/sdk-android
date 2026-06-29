#!/usr/bin/env bash
set -euo pipefail

git config --global user.email "dev@mercadopago.com"
git config --global user.name "CI Build"
npx gh-pages --dotfiles --message "[skip ci] Updates" --dist build/dokka/htmlMultiModule
