#!/bin/bash

source ./secrets.sh

# 스크립트 인자로 받은 값이 있으면 그걸, 없으면 기본값 사용
SCRIPT_NAME="${1:-mentoring-join-concurrency.js}"
SUMMARY_PATH="./script/summary.json"

echo "🚀 동시성 60명 테스트 시작: $SCRIPT_NAME"

# 실행
SCRIPT=$SCRIPT_NAME docker compose -f docker-compose.k6.yml up --abort-on-container-exit
RESULT=$?

# 메시지 포맷
if [ ! -f "$SUMMARY_PATH" ]; then
  MSG="❌ *동시성 테스트 실패!* summary.json이 생성되지 않았습니다.\n스크립트: \`$SCRIPT_NAME\`"
else
  REQS=$(jq '.metrics.http_reqs.count // "N/A"' $SUMMARY_PATH)
  FAILED=$(jq '.metrics.http_req_failed.value // 1' $SUMMARY_PATH)
  DURATION=$(jq '.metrics.http_req_duration.avg // 0' $SUMMARY_PATH)
  P95=$(jq '.metrics.http_req_duration["p(95)"] // 0' $SUMMARY_PATH)
  SUCCESS=$(jq -r '.metrics["checks{check: 신청 성공 or 정원 초과}"].passes // "N/A"' $SUMMARY_PATH)
  FAIL_COUNT=$(jq -r '.metrics["checks{check: 신청 성공 or 정원 초과}"].fails // "N/A"' $SUMMARY_PATH)

  if [ "$FAILED" == "0" ] && [ "$FAIL_COUNT" == "0" ]; then
    ICON="✅"
  else
    ICON="⚠️"
  fi

  MSG="$ICON *동시성 60명 멘토링 신청 테스트 완료*\n📄 \`$SCRIPT_NAME\`\n📊 총 요청: *$REQS*\n✅ 성공: *$SUCCESS*, ❌ 실패: *$FAIL_COUNT*\n💥 실패율: *$(awk "BEGIN {printf \"%.2f\", $FAILED * 100}")%*\n⏱️ 평균 응답: *$(awk "BEGIN {printf \"%.2f\", $DURATION}")s*, P95: *$(awk "BEGIN {printf \"%.2f\", $P95}")s*"
fi

# Slack 전송
curl -X POST -H 'Content-type: application/json' \
--data "{\"text\":\"$MSG\"}" "$SLACK_WEBHOOK_URL"

echo "📬 슬랙 메시지 전송 완료!"