package br.com.cesarcastro.votacao.domain.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public interface Client {
    String get(String url, HttpHeaders httpHeaders);
    String post(String url, String body, HttpHeaders httpHeaders);
    String patch(String url, String body, HttpHeaders httpHeaders);
    String put(String url, String body, HttpHeaders httpHeaders);
    String delete(String url, String body, HttpHeaders httpHeaders);
    HttpStatus getStatusCode();
}
