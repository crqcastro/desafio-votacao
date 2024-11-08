package br.com.cesarcastro.votacao.domain.repositories;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<PautaEntity, Long>, JpaSpecificationExecutor<PautaEntity> {
    @Query("SELECT p FROM PautaEntity p WHERE p.pautaAberta = false AND p.dataHoraInicio >= :dataHoraAtual AND p.dataHoraInicio <= :dataHoraAtual")
    List<PautaEntity> findByPautaAbertaFalseAndDataHoraInicioBetween(@Param("dataHoraAtual") LocalDateTime dataHoraAtual);

    @Query("SELECT p FROM PautaEntity p WHERE p.pautaAberta = true AND p.dataHoraFim > :dataHoraAtual")
    List<PautaEntity> findByPautaAbertaTrueAndDataHoraFimBefore(@Param("dataHoraAtual") LocalDateTime dataHoraAtual);
}
