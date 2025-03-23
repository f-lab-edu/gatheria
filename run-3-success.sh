#!/bin/bash

SCRIPT_NAME="mentoring-join-3-success.js"
SUMMARY_PATH="./script/summary.json"

source ./secrets.sh

echo "🚀 정원 3 테스트 시작: $SCRIPT_NAME"

# 실행
SCRIPT=$SCRIPT_NAME docker compose -f docker-compose.k6.yml up --abort-on-container-exit
RESULT=$?

# 기본 메시지
if [ ! -f "$SUMMARY_PATH" ]; then
  MSG="❌ *정원 3 테스트 실패!* summary.json이 생성되지 않았습니다.\n스크립트: \`$SCRIPT_NAME\`"
else
  REQS=$(jq '.metrics.http_reqs.count' $SUMMARY_PATH)
  FAILED=$(jq '.metrics.http_req_failed.value' $SUMMARY_PATH)
  DURATION=$(jq '.metrics.http_req_duration.avg' $SUMMARY_PATH)
  P95=$(jq '.metrics.http_req_duration["p(95)"]' $SUMMARY_PATH)

  if [ "$FAILED" == "0" ]; then
    ICON="✅"
  else
    ICON="⚠️"
  fi

  MSG="$ICON *정원 3 테스트 완료*\n📄 \`$SCRIPT_NAME\`\n📊 총 요청: *$REQS*\n❌ 실패율: *$(awk "BEGIN {printf \"%.2f\", $FAILED * 100}")%*\n⏱️ 평균 응답: *$(awk "BEGIN {printf \"%.2f\", $DURATION}")s*, P95: *$(awk "BEGIN {printf \"%.2f\", $P95}")s*"
fi

# Slack 전송
curl -X POST -H 'Content-type: application/json' \
--data "{\"text\":\"$MSG\"}" "$SLACK_WEBHOOK_URL"

echo "📬 슬랙 메시지 전송 완료!"