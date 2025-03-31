#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PARENT_DIR="$(dirname "$SCRIPT_DIR")"
K6_SCRIPT="$PARENT_DIR/registerPattern2.js"

echo "🔄 K6 Register Script 실행: $K6_SCRIPT"
echo

if [ ! -f "$K6_SCRIPT" ]; then
  echo "❌ registerPattern2.js 스크립트를 찾을 수 없습니다."
  exit 1
fi

docker run --rm -i \
  -v "$PARENT_DIR:/scripts" \
  grafana/k6 run /scripts/registerPattern2.js