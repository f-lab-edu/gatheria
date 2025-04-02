import http from 'k6/http';
import {check, sleep} from 'k6';

function randomIntBetween(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}

export let options = {
  scenarios: {
    deadlock_scenario: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 100,
      maxVUs: 100,
      stages: [
        {target: 10, duration: '5s'},
        {target: 55, duration: '5s'},
        {target: 100, duration: '5s'},
        {target: 100, duration: '10s'},
        {target: 0, duration: '5s'}
      ],
    }
  },
  thresholds: {
    http_req_failed: ['rate<0.3'],
    http_req_duration: ['p(95)<3000'],
  },
};

const SESSION_ID = 2;

export default function () {
  const id = __VU; // VU 1~100 → student1~student100 1:1 매핑

  const student = {
    email: `student${id}@gatheria.com`,
    password: 'Password123!',
  };

  // 로그인
  const loginRes = http.post(
      'http://host.docker.internal:8080/api/auth/student/login',
      JSON.stringify(student),
      {headers: {'Content-Type': 'application/json'}}
  );

  check(loginRes, {
    '로그인 성공': (res) => res.status === 200,
  });

  if (loginRes.status !== 200) {
    console.error(
        `로그인 실패: 학생${id}, 상태 ${loginRes.status}, 응답: ${loginRes.body}`);
    return;
  }

  const token = loginRes.json('accessToken');

  // 그룹별 요청 타이밍 조정
  if (id <= 20) {
    sleep(randomIntBetween(0, 200) / 1000);
  } else if (id <= 50) {
    const targetTime = new Date().getTime() + 100;
    const now = new Date().getTime();
    if (targetTime > now) {
      sleep((targetTime - now) / 1000);
    }
  } else {
    sleep(randomIntBetween(300, 500) / 1000);
  }

  // 세션 등록 요청
  const startTime = new Date().getTime();
  const joinRes = http.post(
      `http://host.docker.internal:8080/api/mentoring/${SESSION_ID}/join`,
      null,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        tags: {name: '멘토링_세션_등록'},
      }
  );
  const responseTime = new Date().getTime() - startTime;

  const hasDeadlock =
      joinRes.body.includes('Deadlock') ||
      joinRes.body.includes('transaction') ||
      (joinRes.status === 500 && joinRes.body.includes('try restarting'));

  if (hasDeadlock) {
    console.error(
        `[⚠️ 데드락 감지] 학생${id}, 응답시간: ${responseTime}ms, 응답: ${joinRes.body.substring(
            0, 200)}`
    );
  } else if (joinRes.status === 500) {
    console.error(
        `[❌ 서버 오류] 학생${id}, 응답시간: ${responseTime}ms, 응답: ${joinRes.body.substring(
            0, 100)}`
    );
  } else if (joinRes.status === 200) {
    console.log(`[✅ 등록 성공] 학생${id}, 응답시간: ${responseTime}ms`);
  } else if (joinRes.status === 409) {
    console.log(
        `[⚙️ 등록 불가] 학생${id}, 응답시간: ${responseTime}ms, 사유: ${joinRes.body.substring(
            0, 50)}`
    );
  } else {
    console.log(
        `[❓ 기타 응답] 학생${id}, 상태: ${joinRes.status}, 응답시간: ${responseTime}ms`
    );
  }

  check(joinRes, {
    '세션 등록 처리됨': (res) => res.status === 200 || res.status === 409,
    '데드락 없음': (res) => !hasDeadlock,
  });

  // 성공한 경우 상태 확인
  if (joinRes.status === 200) {
    sleep(randomIntBetween(100, 300) / 1000);

    const checkRes = http.get(
        `http://host.docker.internal:8080/api/mentoring/sessions/${SESSION_ID}/status`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
    );

    check(checkRes, {
      '등록 상태 확인 성공': (res) => res.status === 200,
    });
  }
}