package br.com.cesarcastro.votacao.domain.service.pauta;

import br.com.cesarcastro.votacao.domain.model.entities.OpcaoEntity;
import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import br.com.cesarcastro.votacao.mappers.PautaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper mapper;

    public PautaResponse cadastrarPauta(PautaRequest pautaRequest) {
        PautaEntity pautaEntity = mapper.toPautaEntity(pautaRequest);
        List<OpcaoEntity> listOpcoesEntity = mapper.toOpcaoEntityList(pautaRequest.getOpcoes());
        listOpcoesEntity.forEach(opcaoEntity -> opcaoEntity.setPauta(pautaEntity));
        pautaEntity.setOpcoes(listOpcoesEntity);
        pautaRepository.save(pautaEntity);
        return mapper.toPautaResponse(pautaEntity, listOpcoesEntity);
    }
}
