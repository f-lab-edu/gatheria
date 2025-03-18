#!/bin/bash

# k6 테스트 실행 스크립트

# 스크립트 파일 경로 확인
if [ ! -d "./scripts/k6" ]; then
  mkdir -p ./scripts/k6
fi

# 프로메테우스 메트릭 노출 모드로 k6 실행
docker compose exec k6 k6 run --out=prometheus /scripts/script.js

# 테스트 결과 확인을 위한 안내
echo "테스트가 실행 중입니다."
echo "Grafana 대시보드: http://localhost:3001"
echo "Prometheus: http://localhost:9090"