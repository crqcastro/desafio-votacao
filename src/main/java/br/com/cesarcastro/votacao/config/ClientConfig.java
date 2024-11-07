package br.com.cesarcastro.votacao.config;

import br.com.cesarcastro.votacao.domain.client.Client;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class ClientConfig {

    private static final int KEEP_ALIVE_DURATION = 60;
    private static final int MAX_IDLE_CONNECTIONS = 50;
    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;

    @Bean
    public Client client(RestTemplate restTemplate) {
        return new ClientImpl(restTemplate);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        OkHttpClient client = initClientBuilder().build();
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(client));
        return restTemplate;
    }

    private OkHttpClient.Builder initClientBuilder() {
        ConnectionPool okHttpConnectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, SECONDS);

        return new OkHttpClient.Builder()
                .connectionPool(okHttpConnectionPool)
                .connectTimeout(CONNECT_TIMEOUT, SECONDS)
                .readTimeout(READ_TIMEOUT, SECONDS)
                .writeTimeout(WRITE_TIMEOUT, SECONDS);
    }

    private static class ClientImpl implements Client {

        private final RestTemplate restTemplate;

        private HttpStatus status;

        public ClientImpl(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @Override
        public String get(String url, HttpHeaders httpHeaders) {
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            return this.buildRequisition(url, entity, HttpMethod.GET);
        }

        @Override
        public String post(String url, String body, HttpHeaders httpHeaders) {
            HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
            return this.buildRequisition(url, entity, HttpMethod.POST);
        }

        @Override
        public String patch(String url, String body, HttpHeaders httpHeaders) {
            HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
            return this.buildRequisition(url, entity, HttpMethod.PATCH);
        }

        @Override
        public String put(String url, String body, HttpHeaders httpHeaders) {
            HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
            return this.buildRequisition(url, entity, HttpMethod.PUT);
        }

        @Override
        public String delete(String url, String body, HttpHeaders httpHeaders) {
            HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
            return this.buildRequisition(url, entity, HttpMethod.DELETE);
        }

        @Override
        public HttpStatus getStatusCode() {
            return status;
        }

        private String buildRequisition(String url, HttpEntity<?> entity, HttpMethod httpMethod) {
            try {

                ResponseEntity<String> responseEntity = restTemplate
                        .exchange(url, httpMethod, entity, String.class);

                status = HttpStatus.valueOf(responseEntity.getStatusCode().value());

                return responseEntity.getBody();

            } catch (HttpClientErrorException | HttpServerErrorException e) {
                this.status = HttpStatus.valueOf(e.getStatusCode().value());
                if(e.getStatusCode().value()==404){
                    throw new RecursoNaoEncontradoException("Recurso não encontrado");
                }
                throw e;
            } catch (Exception e) {
                this.status = HttpStatus.BAD_REQUEST;
                throw new RestClientException(e.getMessage());
            }
        }
    }
}
