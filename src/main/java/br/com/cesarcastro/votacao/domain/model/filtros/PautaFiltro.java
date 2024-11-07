package br.com.cesarcastro.votacao.domain.model.filtros;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PautaFiltro {
    private Long id;
    private String nome;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean pautaAberta;
    private PageRequest pageRequest;
}
