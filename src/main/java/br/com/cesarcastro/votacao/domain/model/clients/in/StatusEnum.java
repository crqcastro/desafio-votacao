package br.com.cesarcastro.votacao.domain.model.clients.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    UNABLE_TO_VOTE(FALSE), ABLE_TO_VOTE(TRUE);
    boolean habilitado;
}
