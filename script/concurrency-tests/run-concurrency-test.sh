#!/bin/bash
source ./secrets.sh

SCRIPT_NAME=${1:-concurrency-test.js}
SUMMARY_PATH="./script/summary.json"
SESSION_ID=${2:-1}  # 기본 세션 ID는 1

echo "🚀 동시성 테스트 시작: $SCRIPT_NAME"

SCRIPT=$SCRIPT_NAME docker compose -f docker-compose.k6.yml up --abort-on-container-exit
RESULT=$?

# 기본 메시지
if [ ! -f "$SUMMARY_PATH" ]; then
  MSG="❌ *K6 테스트 실패!* summary.json이 생성되지 않았습니다.\n스크립트: \`$SCRIPT_NAME\`"
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

  MSG="$ICON *동시성 테스트 완료*\n📄 \`$SCRIPT_NAME\`\n📊 총 요청: *$REQS*\n❌ 실패율: *$(awk "BEGIN {printf \"%.2f\", $FAILED * 100}")%*\n⏱️ 평균 응답: *$(awk "BEGIN {printf \"%.2f\", $DURATION}")s*, P95: *$(awk "BEGIN {printf \"%.2f\", $P95}")s*"
fi

# DB 쿼리 실행 (신청된 순서)
PARTICIPANTS=$(mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" -N -e "
SELECT m.email, sp.registration_time
FROM session_participants sp
JOIN students s ON sp.student_id = s.id
JOIN members m ON s.member_id = m.id
WHERE sp.session_id = ${SESSION_ID} AND sp.status = 'REGISTERED'
ORDER BY sp.registration_time
LIMIT 10;
")

# 포맷 예쁘게 만들기
if [ -n "$PARTICIPANTS" ]; then
  MSG="$MSG\n\n📋 *신청 순서 (상위 10명)*:"
  while IFS=$'\t' read -r email time; do
    MSG="$MSG\n- $email \`$time\`"
  done <<< "$PARTICIPANTS"
fi

# 슬랙 전송
curl -X POST -H 'Content-type: application/json' \
--data "{\"text\":\"$MSG\"}" "$SLACK_WEBHOOK_URL"

echo "📬 슬랙 메시지 전송 완료!"