package br.com.cesarcastro.votacao.domain.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "tb_pauta", schema = "votacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PautaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "nome")
    private String nome;
    @Column(name = "dataInicio")
    private LocalDateTime dataHoraInicio;
    @Column(name = "dataFim")
    private LocalDateTime dataHoraFim;
    @Column(name = "ativa")
    private Boolean pautaAberta;
    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcaoEntity> opcoes;

}
