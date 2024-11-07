package br.com.cesarcastro.votacao.support.exceptions;

public class BadGatewayException extends RuntimeException {
    public BadGatewayException(String message) {
        super(message);
    }
}
