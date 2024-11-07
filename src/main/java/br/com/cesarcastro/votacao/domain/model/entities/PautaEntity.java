package br.com.cesarcastro.votacao.domain.model.entities;

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

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@Table(name = "tb_pauta")
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
    @Column(name = "totalVotosSim")
    private Long totalVotosSim = 0L;
    @Column(name = "totalVotosNao")
    private Long totalVotosNao = 0L;
    @OneToMany(mappedBy = "pauta", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    private List<VotoEntity> votos;

}
