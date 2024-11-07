package br.com.cesarcastro.votacao.domain.model.clients.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {
    private Integer id;
    private String name;
    private String cpf;
}
