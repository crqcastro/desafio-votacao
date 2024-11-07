package br.com.cesarcastro.votacao.domain.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record OpcaoResponse(Long id, String descricao, String nome, Long votos) {
}
