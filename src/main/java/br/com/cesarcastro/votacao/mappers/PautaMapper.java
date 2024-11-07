package br.com.cesarcastro.votacao.mappers;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    PautaMapper INSTANCE = Mappers.getMapper(PautaMapper.class);

    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "dataHoraInicio", target = "dataHoraInicio")
    @Mapping(source = "dataHoraFim", target = "dataHoraFim")
    @Mapping(target = "pautaAberta", constant = "false")
    PautaEntity toPautaEntity(PautaRequest source);

    @Qualifier("longZero")
    default Long longZero(){
        return 0L;
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "descricao", target = "descricao")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "dataHoraInicio", target = "dataHoraInicio")
    @Mapping(source = "dataHoraFim", target = "dataHoraFim")
    @Mapping(source = "pautaAberta", target = "pautaAberta")
    @Mapping(source = "totalVotosSim", target = "totalVotosSim")
    @Mapping(source = "totalVotosNao", target = "totalVotosNao")
    PautaResponse toPautaResponse(PautaEntity pautaEntity);
}
