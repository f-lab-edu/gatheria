import http from 'k6/http';
import {check, sleep} from 'k6';
import {
  randomIntBetween,
  randomItem,
  randomString
} from "https://jslib.k6.io/k6-utils/1.1.0/index.js";

// 테스트 구성 - Prometheus 출력 설정 포함
export let options = {
  vus: 10, duration: '30s',
  thresholds: {
    'http_req_duration': ['p(95)<2000'], // 95%의 요청이 2초 이내 완료
    'http_req_failed': ['rate<0.1'],     // 요청 실패율 10% 미만
  },
  // Prometheus 출력 설정
  ext: {
    loadimpact: {
      distribution: {local: {loadZone: 'local', percent: 100}},
    },
    prometheusRW: {
      url: "http://host.docker.internal:9090/api/v1/write",
    },
  },
};

// 이메일 도메인 배열
const emailDomains = [
  'gmail.com', 'naver.com', 'daum.net', 'hotmail.com', 'yahoo.com'
];

// 소속 기관 배열
const affiliations = [
  '삼성전자', 'LG전자', '현대자동차', 'SK텔레콤', 'NAVER', '카카오'
];

// 강사 회원가입 요청 생성 함수
function createInstructorRequest() {
  const firstName = randomString(5, 'abcdefghijklmnopqrstuvwxyz');
  const lastName = randomString(3, 'abcdefghijklmnopqrstuvwxyz');
  const emailName = `${firstName}.${lastName}${randomIntBetween(1, 999)}`;
  const domain = randomItem(emailDomains);

  return {
    email: `${emailName}@${domain}`,
    password: `Password${randomIntBetween(100, 999)}!`,
    name: `${lastName.charAt(0).toUpperCase() + lastName.slice(
        1)} ${firstName.charAt(0).toUpperCase() + firstName.slice(1)}`,
    phone: `010${randomIntBetween(10000000, 99999999)}`,
    affiliation: randomItem(affiliations)
  };
}

// 학생 회원가입 요청 생성 함수
function createStudentRequest() {
  const firstName = randomString(5, 'abcdefghijklmnopqrstuvwxyz');
  const lastName = randomString(3, 'abcdefghijklmnopqrstuvwxyz');
  const emailName = `${firstName}.${lastName}${randomIntBetween(1, 999)}`;
  const domain = randomItem(emailDomains);

  return {
    email: `${emailName}@${domain}`,
    password: `Password${randomIntBetween(100, 999)}!`,
    name: `${lastName.charAt(0).toUpperCase() + lastName.slice(
        1)} ${firstName.charAt(0).toUpperCase() + firstName.slice(1)}`,
    phone: `010${randomIntBetween(10000000, 99999999)}`
  };
}

// 메인 테스트 함수
export default function () {
  // HTTP 요청 공통 헤더
  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let payload, url, response;

  // 강사와 학생 회원가입을 50:50 비율로 실행
  if (Math.random() < 0.5) {
    // 강사 회원가입
    payload = JSON.stringify(createInstructorRequest());
    url = 'http://host.docker.internal:8080/api/member/instructor/register';
    response = http.post(url, payload, params);

    check(response, {
      'instructor 상태 200': (r) => r.status === 200,
      'instructor 응답 확인': (r) => r.status < 400
    });
  } else {
    // 학생 회원가입
    payload = JSON.stringify(createStudentRequest());
    url = 'http://host.docker.internal:8080/api/member/student/register';
    response = http.post(url, payload, params);

    check(response, {
      'student 상태 200': (r) => r.status === 200,
      'student 응답 확인': (r) => r.status < 400
    });
  }

  // 요청 간 약간의 대기 시간 추가
  sleep(randomIntBetween(1, 5) / 10);
}