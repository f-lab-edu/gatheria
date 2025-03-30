import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  vus: 100,
  iterations: 100,
};

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

  // 로그인
  const loginRes = http.post(
      'http://host.docker.internal:8080/api/auth/student/login',
      JSON.stringify(student),
      {headers: {'Content-Type': 'application/json'}}
  );

  check(loginRes, {
    'check: 로그인 성공': (res) => res.status === 200,
  });

  const token = loginRes.json('accessToken');

  // 병렬성 집중 구간 조절
  if (id < 40) {
    sleep(id * 0.2); // 순차적으로 느리게
  } else if (id <= 55) {
    sleepUntil(START_AT); // 동시성 집중
  } else {
    sleep(10 + (id - 55) * 0.2); // 늦게 요청 보내기
  }

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
      `[student${id}] 신청 응답 코드: ${joinRes.status}, 메시지: ${joinRes.body}`);

  check(joinRes, {
    'check: 신청 성공 or 정원 초과': (res) =>
        res.status === 200 || res.status === 409,
  });
}