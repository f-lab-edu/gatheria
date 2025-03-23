import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  vus: 1,               // 한 번에 하나씩만
  iterations: 200,      // 총 200명: student1~100, instructor1~100
};

export default function () {
  const id = __ITER + 1;

  const isStudent = id <= 100;
  const num = isStudent ? id : id - 100;

  const user = {
    email: `${isStudent ? 'student' : 'instructor'}${num}@gatheria.com`,
    password: 'Password123!',
    name: `${isStudent ? 'Student' : 'Instructor'} ${num}`,
    phone: isStudent
        ? `0101234${String(num).padStart(4, '0')}`
        : `0109999${String(num).padStart(4, '0')}`,
    ...(isStudent ? {} : {affiliation: '테스트기관'}),
  };

  const url = isStudent
      ? 'http://host.docker.internal:8080/api/member/student/register'
      : 'http://host.docker.internal:8080/api/member/instructor/register';

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(url, JSON.stringify(user), params);

  check(res, {
    [`${isStudent ? '학생' : '교수자'} 상태 200`]: (r) => r.status === 200,
    '응답 코드 < 400': (r) => r.status < 400,
  });

  sleep(0.5); // 부하 줄이기용!
}