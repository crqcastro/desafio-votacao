package br.com.cesarcastro.votacao.domain.repositories;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends CrudRepository<PautaEntity, Long> {
}
