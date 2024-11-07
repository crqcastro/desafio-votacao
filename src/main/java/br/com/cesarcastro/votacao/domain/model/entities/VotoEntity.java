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

import static jakarta.persistence.FetchType.EAGER;

@Table(name = "tb_voto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "pauta_id", nullable = false)
    @ManyToOne(fetch = EAGER)
    private PautaEntity pauta;
    @Column(name = "cpf")
    private String cpf;
    @Column(name = "voto")
    private Boolean voto;
}