import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  vus: 100,
  iterations: 100,
};

export default function () {
  const id = __VU;
  const student = {
    email: `student${id}@gatheria.com`,
    password: 'Password123!'
  };

  // 1. 로그인
  const loginRes = http.post(
      'http://host.docker.internal:8080/api/auth/student/login',
      JSON.stringify(student),
      {headers: {'Content-Type': 'application/json'}}
  );

  check(loginRes, {
    '✅ 로그인 성공': (res) => res.status === 200,
  });

  const token = loginRes.json('accessToken');

  // 2. 요청 간 분산 (전원 sleep 적용)
  const delay = id <= 40 ? id * 0.2 : 8 + (id - 40) * 0.1;
  sleep(delay);  // ex. id=41 → 8.1s, id=50 → 9s

  // 3. 멘토링 신청
  const joinRes = http.post(
      'http://host.docker.internal:8080/api/mentoring/3/join',
      null,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      }
  );

  console.log(
      `[student${id}] 신청 응답 코드: ${joinRes.status}, 메시지: ${joinRes.body}`);

  check(joinRes, {
    '✅ 신청 성공 or 정원 초과': (res) => res.status === 200 || res.status === 409,
  });
}