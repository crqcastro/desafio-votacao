package br.com.cesarcastro.votacao.domain.repositories.especifications;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PautaSpecification {
    public static Specification<PautaEntity> nomeContains(String nome) {
        return (root, query, criteriaBuilder) ->
                nome == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }
    public static Specification<PautaEntity> idEqual(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<PautaEntity> dataInicial(LocalDateTime dataInicial) {
        return (root, query, criteriaBuilder) ->
                dataInicial == null ? null : criteriaBuilder.equal(root.get("dataHoraInicio"), dataInicial);
    }

    public static Specification<PautaEntity> dataFim(LocalDateTime dataFim) {
        return (root, query, criteriaBuilder) ->
                dataFim == null ? null : criteriaBuilder.equal(root.get("dataHoraFim"), dataFim);
    }

    public static Specification<PautaEntity> pautaAberta(Boolean pautaAberta) {
        return (root, query, criteriaBuilder) ->
                pautaAberta == null ? null : criteriaBuilder.equal(root.get("pautaAberta"), pautaAberta);
    }

    public static Specification<PautaEntity> diaInicio(LocalDate dataInicio) {
        return (root, query, criteriaBuilder) ->
                dataInicio == null ? null : criteriaBuilder.greaterThan(root.get("dataHoraInicio"), dataInicio.atStartOfDay());
    }

    public static Specification<PautaEntity> diaFim(LocalDate dataFim) {
        return (root, query, criteriaBuilder) ->
                dataFim == null ? null : criteriaBuilder.lessThan(root.get("dataHoraFim"), dataFim.atStartOfDay().plusDays(1));
    }


}
