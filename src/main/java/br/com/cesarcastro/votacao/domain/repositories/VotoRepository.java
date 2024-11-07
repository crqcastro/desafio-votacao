package br.com.cesarcastro.votacao.domain.repositories;

import br.com.cesarcastro.votacao.domain.model.entities.VotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<VotoEntity, Long> {
    Boolean existsByCpfAndPautaId(String cpf, Long pautaId);
}
