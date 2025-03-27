#!/bin/bash
SCRIPT_NAME=${1:-register.js}

# 비밀 환경 변수 로딩
source ./secrets.sh

SUMMARY_PATH="./script/summary.json"


echo "🚀 K6 테스트 시작: $SCRIPT_NAME"
SCRIPT=$SCRIPT_NAME docker compose -f docker-compose.k6.yml up --abort-on-container-exit
RESULT=$?

# summary 파일이 없으면 실패 처리
if [ ! -f "$SUMMARY_PATH" ]; then
  MSG="❌ *K6 테스트 실패!* summary.json이 생성되지 않았습니다.\n스크립트: \`$SCRIPT_NAME\`"
else
  # JSON 파싱 (경로 수정!)
  REQS=$(jq '.metrics.http_reqs.count' $SUMMARY_PATH)
  FAILED=$(jq '.metrics.http_req_failed.value' $SUMMARY_PATH)
  DURATION=$(jq '.metrics.http_req_duration.avg' $SUMMARY_PATH)
  P95=$(jq '.metrics.http_req_duration["p(95)"]' $SUMMARY_PATH)

  # 메시지 구성
  if [ "$FAILED" == "0" ]; then
    ICON="✅"
  else
    ICON="⚠️"
  fi

  MSG="$ICON *K6 테스트 완료*\n📄 \`$SCRIPT_NAME\`\n📊 총 요청: *$REQS*\n❌ 실패율: *$(awk "BEGIN {printf \"%.2f\", $FAILED * 100}")%*\n⏱️ 평균 응답: *$(awk "BEGIN {printf \"%.2f\", $DURATION}")s*, P95: *$(awk "BEGIN {printf \"%.2f\", $P95}")s*"
fi

# 슬랙 전송
curl -X POST -H 'Content-type: application/json' \
--data "{\"text\":\"$MSG\"}" "$SLACK_WEBHOOK_URL"

echo "📬 슬랙 메시지 전송 완료!"