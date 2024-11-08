package br.com.cesarcastro.votacao.domain.model.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record PautaResponse(
        @Schema(description = "Id da pauta.", required = true)
        Long id,
        @Schema(description = "Nome da pauta.", required = true)
        String nome,
        @Schema(description = "Descrição da pauta.")
        String descricao,
        @Schema(description = "Data e hora que a votação deve iniciar.", example = "2024-11-04T00:00:00", required = true)
        LocalDateTime dataHoraInicio,
        @Schema(description = "Data e hora que a votação deve encerrar.", example = "2024-11-04T00:00:00", required = true)
        LocalDateTime dataHoraFim,
        @Schema(description = "Indica se a pauta está aberta para votação.", required = true)
        Boolean pautaAberta,
        @Schema(description = "Quantidade de votos sim", required = true)
        Long totalVotosSim,
        @Schema(description = "Quantidade de votos não.", required = true)
        Long totalVotosNao) {
}
