package br.com.cesarcastro.votacao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class RequestLoggingFilterConfig implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Request URI: {}", exchange.getRequest().getURI());
        log.info("Request Method: {}", exchange.getRequest().getMethod());
        log.info("Request Headers: {}", exchange.getRequest().getHeaders());

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    log.info("Response Status Code: {}", exchange.getResponse().getStatusCode());
                })
                .doOnError(throwable -> {
                    log.error("Error: ", throwable);
                });
    }
}