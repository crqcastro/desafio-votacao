package br.com.cesarcastro.votacao.mappers;

import br.com.cesarcastro.votacao.domain.model.entities.OpcaoEntity;
import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.requests.OpcaoRequest;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.OpcaoResponse;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    PautaMapper INSTANCE = Mappers.getMapper(PautaMapper.class);

    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "dataHoraInicio", target = "dataHoraInicio")
    @Mapping(source = "dataHoraFim", target = "dataHoraFim")
    @Mapping(target = "pautaAberta", constant = "false")
    @Mapping(target = "opcoes", ignore = true)
    PautaEntity toPautaEntity(PautaRequest source);

    default List<OpcaoEntity> toOpcaoEntityList(List<OpcaoRequest> list) {
        return list.parallelStream().map(this::toOpcaoEntity).toList();
    }

    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "nome", target = "nome")
    OpcaoEntity toOpcaoEntity(OpcaoRequest opcaoRequest);

    //TODO Implementar
    PautaResponse toPautaResponse(PautaEntity pautaEntity, List<OpcaoEntity> opcaoEntityList);

    default List<OpcaoResponse> toOpcaoResponseList(List<OpcaoEntity> list){
        return list.parallelStream().map(this::toOpcaoResponse).toList();
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "votos", target = "votos")
    OpcaoResponse toOpcaoResponse(OpcaoEntity source);
}
