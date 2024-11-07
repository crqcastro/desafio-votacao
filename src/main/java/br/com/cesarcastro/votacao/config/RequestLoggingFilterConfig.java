package br.com.cesarcastro.votacao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


@Configuration
@Slf4j
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {

        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

        filter.setIncludeClientInfo(true);
        filter.setIncludeHeaders(false);
        filter.setIncludePayload(true);
        filter.setIncludeQueryString(true);

        filter.setMaxPayloadLength(10000);
        filter.setAfterMessagePrefix("REQUEST DATA: ");

        return filter;
    }
}