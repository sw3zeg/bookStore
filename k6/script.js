import http from 'k6/http';
import { check } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';

export const options = {
  stages: [
    { duration: '5s', target: 5 },
    { duration: '10s', target: 5 },
    { duration: '5s', target: 0 },
  ],
};

export default function () {
  const res = http.get('http://192.168.1.77:8081/api/books');

  check(res, {
    'status is 200': (r) => r.status === 200,
  });
}
