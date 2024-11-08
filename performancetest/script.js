import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 50 },  // Sobe para 50 usuários em 30 segundos
    { duration: '1m', target: 100 },  // Mantém 100 usuários por 1 minuto
    { duration: '30s', target: 0 },   // Reduz para 0 usuários em 30 segundos
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% das requisições devem ser abaixo de 500ms
    http_req_failed: ['rate<0.01'],   // Taxa de falhas menor que 1%
  },
};

export default function () {
  // IMPOERTANTE: A Pauta consultada deve estar cadastrada previamente na base!
  let res = http.get('http://localhost:8083/v1/pauta/1/contabilizar');
  check(res, {
    'status é 200': (r) => r.status === 200,
    'tempo de resposta é menor que 500ms': (r) => r.timings.duration < 500,
  });
  sleep(1);
}
