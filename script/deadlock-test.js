import http from 'k6/http';
import {check} from 'k6';

export let options = {
  scenarios: {
    wave1: {
      executor: 'per-vu-iterations',
      vus: 40,
      iterations: 1,
      startTime: '0s',
      maxDuration: '10s',
    },
    wave2: {
      executor: 'per-vu-iterations',
      vus: 40,
      iterations: 1,
      startTime: '1s',
      maxDuration: '10s',
    },
    wave3: {
      executor: 'per-vu-iterations',
      vus: 40,
      iterations: 1,
      startTime: '2s',
      maxDuration: '10s',
    },
    wave4: {
      executor: 'per-vu-iterations',
      vus: 40,
      iterations: 1,
      startTime: '3s',
      maxDuration: '10s',
    },
    wave5: {
      executor: 'per-vu-iterations',
      vus: 40,
      iterations: 1,
      startTime: '4s',
      maxDuration: '10s',
    },
    wave6: {
      executor: 'per-vu-iterations',
      vus: 40,
      iterations: 1,
      startTime: '5s',
      maxDuration: '10s',
    },
  },
  // 기본 요약 통계를 표시하기 위한 설정
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)'],
};

// 응답 시간 데이터를 수집할 배열
let responseTimes = [];
let waveStartTimes = {};
let waveCompleteCounts = {
  wave1: 0,
  wave2: 0,
  wave3: 0,
  wave4: 0,
  wave5: 0,
  wave6: 0
};

export default function () {
  const id = __VU;
  const wave = __ENV.SCENARIO;

  // 웨이브 시작 시간 기록 (첫 요청시)
  if (!waveStartTimes[wave]) {
    waveStartTimes[wave] = new Date().getTime();
  }

  const student = {
    email: `student${id}@gatheria.com`,
    password: 'Password123!',
  };

  const start = new Date().getTime();
  const res = http.post(
      'http://host.docker.internal:8080/api/auth/student/login',
      JSON.stringify(student),
      {
        headers: {'Content-Type': 'application/json'},
      }
  );
  const duration = new Date().getTime() - start;

  // 응답 시간 데이터 수집
  responseTimes.push({
    vu: id,
    wave: wave,
    time: duration,
    status: res.status
  });

  const success = check(res, {
    '로그인 성공': (r) => r.status === 200,
  });

  // 웨이브별 완료 카운트 증가
  waveCompleteCounts[wave]++;

  // 개별 VU 로그는 출력하지 않음 - 요약 데이터만 수집
}

export function handleSummary(data) {
  // 표준 요약 통계 출력
  console.log("\n--- 테스트 완료 ---");

  // 웨이브별 완료 카운트 및 평균 응답 시간 출력
  let waveStats = {};

  for (const wave in waveCompleteCounts) {
    const waveResponses = responseTimes.filter(r => r.wave === wave);
    const avgTime = waveResponses.reduce((sum, r) => sum + r.time, 0)
        / (waveResponses.length || 1);
    const successCount = waveResponses.filter(r => r.status === 200).length;

    waveStats[wave] = {
      total: waveCompleteCounts[wave],
      success: successCount,
      avgTime: Math.round(avgTime)
    };

    console.log(`${wave}: 완료 ${waveCompleteCounts[wave]}개, 성공률 ${Math.round(
        successCount / waveCompleteCounts[wave] * 100)}%, 평균응답시간 ${Math.round(
        avgTime)}ms`);
  }

  // CSV 파일로 데이터 출력 (그래프 생성용)
  let csvContent = "wave,vus,success,failure,avg_time\n";

  for (const wave in waveStats) {
    const stats = waveStats[wave];
    csvContent += `${wave},${options.scenarios[wave].vus},${stats.success},${stats.total
    - stats.success},${stats.avgTime}\n`;
  }

  return {
    "stdout": data.stdout,
    "./test-results.csv": csvContent
  };
}