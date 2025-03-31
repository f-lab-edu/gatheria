import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  vus: 1,               // 락 줄이기 위해 순차 실행
  iterations: 537,     // student464 ~ student1000
};

export default function () {
  const id = __ITER + 1;       // 1 ~ 537
  const num = id + 463;        // 464 ~ 1000

  const user = {
    email: `student${num}@gatheria.com`,
    password: 'Password123!',
    name: `Student ${num}`,
    phone: `0101234${String(num).padStart(4, '0')}`,
  };

  const url = 'http://host.docker.internal:8080/api/member/student/register';

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(url, JSON.stringify(user), params);

  check(res, {
    [`학생 ${num} 상태 200`]: (r) => r.status === 200,
    '응답 코드 < 400': (r) => r.status < 400,
  });

  sleep(0.5); // 부하 줄이기 위해 느리게 진행
}