package br.com.cesarcastro.votacao.domain.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record PautaResponse(Long id,
                            String descricao,
                            String nome,
                            LocalDateTime dataHoraInicio,
                            LocalDateTime dataHoraFim,
                            Boolean pautaAberta,
                            Long totalVotosSim,
                            Long totalVotosNao) {
}
