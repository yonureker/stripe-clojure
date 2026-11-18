#!/usr/bin/env bash
# Starts stripe-mock with the OpenAPI spec this library is pinned to.
# The spec and fixtures are downloaded once from github.com/stripe/openapi.
#
# Usage: scripts/stripe-mock.sh
# Env:   STRIPE_MOCK_PORT  HTTP port (default 12111); HTTPS uses the next port
#        OPENAPI_TAG       stripe/openapi tag to serve (default below)
set -euo pipefail

# v2600 is the last stripe/openapi tag for API version 2026-10-28.endive.
OPENAPI_TAG="${OPENAPI_TAG:-v2600}"
PORT="${STRIPE_MOCK_PORT:-12111}"
SPEC_DIR="$(cd "$(dirname "$0")/.." && pwd)/.stripe-spec/${OPENAPI_TAG}"
BASE_URL="https://raw.githubusercontent.com/stripe/openapi/${OPENAPI_TAG}/openapi"

mkdir -p "$SPEC_DIR"
for file in spec3.sdk.json fixtures3.json; do
  if [ ! -s "$SPEC_DIR/$file" ]; then
    echo "Downloading $file ($OPENAPI_TAG)"
    curl -fsSL --retry 3 "$BASE_URL/$file" -o "$SPEC_DIR/$file.tmp"
    mv "$SPEC_DIR/$file.tmp" "$SPEC_DIR/$file"
  fi
done

exec stripe-mock \
  -http-port "$PORT" \
  -https-port "$((PORT + 1))" \
  -spec "$SPEC_DIR/spec3.sdk.json" \
  -fixtures "$SPEC_DIR/fixtures3.json"
