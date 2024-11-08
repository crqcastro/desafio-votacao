package br.com.cesarcastro.votacao.domain.model.requests;

import br.com.cesarcastro.votacao.annotations.DateGreaterThan;
import br.com.cesarcastro.votacao.annotations.FutureDate;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DateGreaterThan(startDateField = "dataHoraInicio", endDateField = "dataHoraFim", message = "A dataHoraFim deve ser maior que dataHoraInicio")
@Schema(description = "Requisição para criação de uma nova pauta.", name = "Pauta")
public class PautaRequest {
    @Schema(description = "Nome da pauta.", requiredMode = REQUIRED)
    @NotNull(message = "O nome da pauta é obrigatório")
    private String nome;
    @Schema(description = "Descrição sobre a pauta.")
    private String descricao;
    @Schema(description = "Data e hora que a votação deve iniciar.", example = "2024-11-04T00:00:00", requiredMode = REQUIRED)
    @NotNull(message = "A data de início é obrigatória")
    @FutureDate
    private LocalDateTime dataHoraInicio;
    @Schema(description = "Data e hora que a votação deve finalizar.", example = "2024-11-04T23:59:59", requiredMode = REQUIRED)
    @NotNull(message = "A data de fim é obrigatória")
    @FutureDate
    private LocalDateTime dataHoraFim;
}
