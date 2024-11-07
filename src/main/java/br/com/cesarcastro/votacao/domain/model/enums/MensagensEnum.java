package br.com.cesarcastro.votacao.domain.model.enums;

import lombok.Getter;

@Getter
public enum MensagensEnum {
    FEIGN_NOT_AUTHORIZED("Erro de autenticação"),
    FEIGN_FORBIDDEN( "Erro de autorização - usuário não autorizado"),
    FEIGN_NOT_FOUND("Recurso não encontrado"),
    FEIGN_INTERNAL_ERROR("Erro interno do servidor"),
    FEIGN_BAD_GATEWAY("Erro de gateway - serviço indisponível"),
    FEIGN_SERVICE_UNAVAILABLE("Erro de serviço indisponível");

     private final String mensagem;

    MensagensEnum(String mensagem) {
          this.mensagem = mensagem;
    }
}
