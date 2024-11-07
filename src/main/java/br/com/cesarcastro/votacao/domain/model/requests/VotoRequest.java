package br.com.cesarcastro.votacao.domain.model.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VotoRequest {
    @Schema(description = "Id da pauta.", requiredMode = REQUIRED)
    @NotNull(message = "O ID da pauta é obrigatório")
    private Long id;
    @Schema(description = "CPF do usuario votante.", requiredMode = REQUIRED)
    @NotNull(message = "O CPF é obrigatório")
    private String cpf;
    @Schema(description = "Voto selecionado para a pauta.", requiredMode = REQUIRED)
    @NotNull(message = "O VOTO da pauta é obrigatório")
    private Boolean voto;
}