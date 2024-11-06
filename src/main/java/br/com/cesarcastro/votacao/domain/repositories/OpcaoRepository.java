package br.com.cesarcastro.votacao.domain.repositories;

import br.com.cesarcastro.votacao.domain.model.entities.OpcaoEntity;
import org.springframework.data.repository.CrudRepository;

public interface OpcaoRepository extends CrudRepository<OpcaoEntity, Long> {
}
