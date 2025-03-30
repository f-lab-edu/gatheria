import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
  vus: 1,
  iterations: 3, // student1 ~ student3만 테스트
};

const students = [
  {email: 'student1@gatheria.com', password: 'Password123!'},
  {email: 'student2@gatheria.com', password: 'Password123!'},
  {email: 'student3@gatheria.com', password: 'Password123!'}
];

export default function () {
  const student = students[__ITER];

  // 1. 로그인
  const loginRes = http.post(
      'http://host.docker.internal:8080/api/auth/student/login',
      JSON.stringify(student),
      {
        headers: {'Content-Type': 'application/json'}
      }
  );

  check(loginRes, {
    '로그인 성공': (res) => res.status === 200,
  });

  const token = loginRes.json('accessToken');

  // 2. 멘토링 세션 신청
  const joinRes = http.post(
      'http://host.docker.internal:8080/api/mentoring/1/join',
      null,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
  );

  check(joinRes, {
    '신청 성공 or 정원 초과': (res) => res.status === 200 || res.status === 409,
  });

  console.log(
      `[${student.email}] 신청 응답 코드: ${joinRes.status}, 메시지: ${joinRes.body}`
  );

  sleep(1);
}