package br.com.cesarcastro.votacao.support.exceptions;

import br.com.cesarcastro.votacao.domain.model.enums.MensagensEnum;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;
import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {
    private static final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = getResponseBody(response);

        return switch (response.status()) {
            case 401 -> new AuthenticationException(MensagensEnum.FEIGN_NOT_AUTHORIZED.getMensagem());
            case 404 -> new RecursoNaoEncontradoException(MensagensEnum.FEIGN_NOT_FOUND.getMensagem());
            case 502 -> new BadGatewayException(MensagensEnum.FEIGN_BAD_GATEWAY.getMensagem());
            case 503 -> new ServiceUnavailableException(MensagensEnum.FEIGN_SERVICE_UNAVAILABLE.getMensagem());
            default -> errorDecoder.decode(methodKey, response);
        };
    }

    private String getResponseBody(Response response) {
        if (response.body() == null) {
            return "";
        }

        try (var inputStream = response.body().asInputStream()) {
            return new String(Util.toByteArray(inputStream), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Erro ao processar a requisição", e);
            throw new RuntimeException("Erro ao processar a requisição");
        }
    }
}