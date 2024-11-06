package br.com.cesarcastro.votacao.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "tb_opcao", schema = "votacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OpcaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "nome")
    private String nome;
    @Column(name = "votos")
    private Long votos;
    @ManyToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private PautaEntity pauta;
}
