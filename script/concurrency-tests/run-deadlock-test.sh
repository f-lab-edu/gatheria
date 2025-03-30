#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/secrets.sh"
# 색상 정의
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 명령줄 인자 처리
SCRIPT_NAME=${1:-deadlock-test.js}
SESSION_ID=${2:-2}

echo -e "${GREEN}===== 멘토링 세션 등록 데드락 테스트 =====${NC}"
echo "테스트 스크립트: ${BLUE}$SCRIPT_NAME${NC}"
echo "대상 세션 ID: ${BLUE}$SESSION_ID${NC}"
echo

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PARENT_DIR="$(dirname "$SCRIPT_DIR")"
K6_SCRIPT="$PARENT_DIR/$SCRIPT_NAME"
RESULT_DIR="$SCRIPT_DIR/results"
SUMMARY_PATH="$PARENT_DIR/summary.json"

mkdir -p "$RESULT_DIR"

if [ ! -f "$K6_SCRIPT" ]; then
    echo -e "${RED}오류: $SCRIPT_NAME 파일을 찾을 수 없습니다.${NC}"
    exit 1
fi

DOCKER_COMPOSE_FILE="$PARENT_DIR/../docker-compose.k6.yml"
if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
    DOCKER_COMPOSE_FILE="$PARENT_DIR/docker-compose.k6.yml"
    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        echo -e "${RED}오류: docker-compose.k6.yml 파일을 찾을 수 없습니다.${NC}"
        exit 1
    fi
fi

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
RESULT_FILE="$RESULT_DIR/deadlock_test_result_$TIMESTAMP.json"

# 데드락 수 측정 - BEFORE
echo -e "\n${GREEN}===== 테스트 전 데드락 수 측정 =====${NC}"
DEADLOCK_BEFORE=$(curl -s localhost:9104/metrics | grep '^mysql_info_schema_innodb_metrics_lock_lock_deadlocks_total' | awk '{print $2}')
echo "이전 데드락 수: $DEADLOCK_BEFORE"

# Docker Compose로 K6 실행
echo -e "${YELLOW}테스트 시작 중...${NC}"
echo "테스트 결과는 $RESULT_FILE 에 저장됩니다."
echo

export SESSION_ID=$SESSION_ID
export SCRIPT=$SCRIPT_NAME

START_TIME=$(date +%s)
echo -e "${BLUE}Docker Compose로 K6 실행 중...${NC}"
docker compose -f "$DOCKER_COMPOSE_FILE" up --abort-on-container-exit
END_TIME=$(date +%s)
RESULT=$?

# 데드락 수 측정 - AFTER
echo -e "\n${GREEN}===== 테스트 후 데드락 수 측정 =====${NC}"
NOW=$(date +"%Y-%m-%d %H:%M:%S")
echo "측정 시각: $NOW"
DEADLOCK_AFTER=$(curl -s localhost:9104/metrics | grep '^mysql_info_schema_innodb_metrics_lock_lock_deadlocks_total' | awk '{print $2}')
echo "현재 데드락 수: $DEADLOCK_AFTER"

# 비교
if [[ -n "$DEADLOCK_BEFORE" && -n "$DEADLOCK_AFTER" ]]; then
DEADLOCK_DIFF=$((DEADLOCK_AFTER - DEADLOCK_BEFORE))
    echo -e "${YELLOW}이번 테스트에서 발생한 데드락 수: $DEADLOCK_DIFF 건${NC}"
else
    echo -e "${RED}데드락 정보를 가져오지 못했습니다.${NC}"
fi

# 테스트 시간 기반 초당 요청 수 계산
if [[ -f "$SUMMARY_PATH" && -x "$(command -v jq)" ]]; then
    REQS=$(jq '.metrics.http_reqs.count // 0' "$SUMMARY_PATH")
    DURATION_MS=$(jq '.metrics.http_req_duration.avg // 0' "$SUMMARY_PATH")
    P95=$(jq '.metrics.http_req_duration["p(95)"] // 0' "$SUMMARY_PATH")
    FAILED=$(jq '.metrics.http_req_failed.value // 0' "$SUMMARY_PATH")
    DURATION_SEC=$((END_TIME - START_TIME))
    RPS=$(awk "BEGIN {printf \"%.2f\", $REQS / $DURATION_SEC}")
    echo -e "${BLUE}테스트 시간: $DURATION_SEC 초${NC}"
    echo -e "${BLUE}초당 요청 수 (RPS): $RPS${NC}"
    echo -e "${BLUE}평균 응답시간:${NC} ${DURATION_MS}ms"
    echo -e "${BLUE}P95 응답시간:${NC} ${P95}ms"
fi

# DB 참가자 조회 및 세션 정보
DB_HOST=${DB_HOST:-"127.0.0.1"}
DB_PORT=${DB_PORT:-"3306"}
DB_USER=${DB_USER:-"root"}
DB_PASSWORD=${DB_PASSWORD:-"1234"}
DB_NAME=${DB_NAME:-"gatheria"}

MYSQL_CMD="mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASSWORD --protocol=TCP"

echo -e "\n${GREEN}===== 세션 참가자 분석 =====${NC}"
echo -e "${BLUE}세션 $SESSION_ID 참가자 등록 순서 (상위 10명):${NC}"
PARTICIPANTS=$($MYSQL_CMD -e "
SELECT m.email, sp.request_at, sp.registered_at, sp.status
FROM $DB_NAME.session_participants sp
JOIN $DB_NAME.students s ON sp.student_id = s.id
JOIN $DB_NAME.members m ON s.member_id = m.id
WHERE sp.session_id = ${SESSION_ID}
ORDER BY sp.request_at
LIMIT 10;" 2>/dev/null)

if [ -n "$PARTICIPANTS" ]; then
    echo -e "$PARTICIPANTS"
else
    echo "참가자 정보를 찾을 수 없습니다."
fi

echo -e "\n${BLUE}세션 $SESSION_ID 정보:${NC}"
SESSION_INFO=$($MYSQL_CMD -e "
SELECT title, max_participants, current_participants, status
FROM $DB_NAME.mentoring_sessions
WHERE id = ${SESSION_ID};" 2>/dev/null)

if [ -n "$SESSION_INFO" ]; then
    echo -e "$SESSION_INFO"
else
    echo "세션 정보를 찾을 수 없습니다."
fi

# InnoDB 상태 저장
INNODB_LOG="$RESULT_DIR/innodb_status_$TIMESTAMP.log"
echo -e "\n${GREEN}===== InnoDB 데드락 상태 저장 =====${NC}"
$MYSQL_CMD -e "SHOW ENGINE INNODB STATUS\\G" > "$INNODB_LOG" 2>/dev/null

if [ -s "$INNODB_LOG" ]; then
    echo "MySQL InnoDB 상태가 성공적으로 저장되었습니다: $INNODB_LOG"
else
    echo -e "${YELLOW}경고: InnoDB 상태를 저장하지 못했습니다.${NC}"
fi

# 결과 파일 복사
if [ -f "$SUMMARY_PATH" ]; then
    cp "$SUMMARY_PATH" "$RESULT_FILE"
    echo "요약 결과를 $RESULT_FILE 에 저장 완료."
else
    echo -e "${RED}요약 summary.json 파일이 존재하지 않습니다. 결과 저장 실패${NC}"
fi

echo -e "\n${GREEN}===== Slack 시도 =====${NC}"
echo -e "${BLUE}Slack 전송 조건 검사 중...${NC}"
echo "REQS=$REQS, RPS=$RPS, DURATION_MS=$DURATION_MS, P95=$P95"

if command -v curl &>/dev/null && [ -n "$SLACK_WEBHOOK_URL" ]; then
  echo -e "${GREEN}Slack 전송 조건 통과! 메시지 전송 시작${NC}"
MSG="K6 데드락 테스트 결과\\n"
MSG+="스크립트: \`$SCRIPT_NAME\`\\n"
MSG+="세션 ID: $SESSION_ID\\n"
MSG+="전체 요청: $REQS\\n"
MSG+="RPS: $RPS\\n"
MSG+="응답 시간: 평균 ${DURATION_MS}ms / P95 ${P95}ms\\n"
MSG+="데드락: ${DEADLOCK_DIFF}건 발생 (이전: ${DEADLOCK_BEFORE}, 누적: ${DEADLOCK_AFTER})\\n"
MSG+="결과: $( [ "$RESULT" -eq 0 ] && echo "✅ 성공" || echo "❌ 실패" )"

  payload=$(jq -n --arg text "$MSG" '{text: $text}')
  RESPONSE=$(curl -s -w "%{http_code}" -o /dev/null -X POST -H 'Content-type: application/json' \
             --data "$payload" "$SLACK_WEBHOOK_URL")

  if [ "$RESPONSE" == "200" ]; then
    echo -e "${GREEN}Slack 알림 전송 완료!${NC}"
  else
    echo -e "${RED}Slack 알림 전송 실패! 응답 코드: $RESPONSE${NC}"
  fi
else
  echo -e "${RED}Slack 전송 조건 불충족. curl or webhook URL 확인 필요${NC}"
fi

echo
echo -e "${GREEN}테스트가 완료되었습니다.${NC}"
echo "전체 결과는 다음 명령어로 확인할 수 있습니다:"
echo "cat $RESULT_FILE | jq"

