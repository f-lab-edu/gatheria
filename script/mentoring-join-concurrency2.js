import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  vus: 100,         // 100명의 가상 유저
  iterations: 100,  // 총 100번 요청
};

// 5초 후를 기준으로 40~55번만 일제히 시작
const START_AT = new Date().getTime() + 5000;

function sleepUntil(targetTimeMs) {
  const now = new Date().getTime();
  if (targetTimeMs > now) {
    sleep((targetTimeMs - now) / 1000);
  }
}

export default function () {
  const id = __VU;
  const student = {
    email: `student${id}@gatheria.com`,
    password: 'Password123!',
  };

  // 1. 로그인
  const loginRes = http.post(
      'http://host.docker.internal:8080/api/auth/student/login',
      JSON.stringify(student),
      {headers: {'Content-Type': 'application/json'}}
  );

  check(loginRes, {
    'check: 로그인 성공': (res) => res.status === 200,
  });

  const token = loginRes.json('accessToken');

  // 2. 분산 전략
  if (id >= 40 && id <= 55) {
    // 40~55번은 동시에 START_AT 기준으로 일괄 실행
    sleepUntil(START_AT);
  } else {
    // 나머지는 개별적으로 간격 주기
    sleep(id * 0.15); // ex: id=10 → 1.5초 대기
  }

  // 3. 멘토링 신청
  const joinRes = http.post(
      'http://host.docker.internal:8080/api/mentoring/2/join',
      null,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      }
  );

  console.log(
      `[student${id}] 신청 응답 코드: ${joinRes.status}, 메시지: ${joinRes.body}`
  );

  check(joinRes, {
    'check: 신청 성공 or 정원 초과': (res) =>
        res.status === 200 || res.status === 409,
  });
}