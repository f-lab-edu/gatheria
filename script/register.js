import http from 'k6/http';
import {check, sleep} from 'k6';
import {
  randomIntBetween,
  randomItem,
  randomString
} from "https://jslib.k6.io/k6-utils/1.1.0/index.js";

export let options = {
  vus: 10, duration: '30s',
  thresholds: {
    'http_req_duration': ['p(95)<2000'],
    'http_req_failed': ['rate<0.1'],
  },

  ext: {
    loadimpact: {
      distribution: {local: {loadZone: 'local', percent: 100}},
    },
    prometheusRW: {
      url: "http://host.docker.internal:9090/api/v1/write",
    },
  },
};

const emailDomains = [
  'gmail.com', 'naver.com', 'daum.net', 'hotmail.com', 'yahoo.com'
];

const affiliations = [
  '삼성전자', 'LG전자', '현대자동차', 'SK텔레콤', 'NAVER', '카카오'
];

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

export default function () {

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let payload, url, response;

  if (Math.random() < 0.5) {
    payload = JSON.stringify(createInstructorRequest());
    url = 'http://host.docker.internal:8080/api/member/instructor/register';
    response = http.post(url, payload, params);

    check(response, {
      'instructor 상태 200': (r) => r.status === 200,
      'instructor 응답 확인': (r) => r.status < 400
    });
  } else {
    payload = JSON.stringify(createStudentRequest());
    url = 'http://host.docker.internal:8080/api/member/student/register';
    response = http.post(url, payload, params);

    check(response, {
      'student 상태 200': (r) => r.status === 200,
      'student 응답 확인': (r) => r.status < 400
    });
  }

  sleep(randomIntBetween(1, 5) / 10);
}