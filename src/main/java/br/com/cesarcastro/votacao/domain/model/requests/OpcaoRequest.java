package br.com.cesarcastro.votacao.domain.model.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Opcao", description = "Requisição para criação de uma nova opção em uma pauta.")
public class OpcaoRequest {
    @Schema(description = "Nome da opção.", requiredMode = REQUIRED)
    @NotNull(message = "O nome da opção é obrigatório")
    private String nome;
    @Schema(description = "Descrição sobre a opção.")
    private String descricao;
}
