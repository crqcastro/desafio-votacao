package br.com.cesarcastro.votacao.domain.service.pauta;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import br.com.cesarcastro.votacao.mappers.PautaMapper;
import br.com.cesarcastro.votacao.support.exceptions.BusinessException;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

    private final PautaRepository pautaRepository;
    private final PautaMapper mapper;

    public PautaResponse cadastrarPauta(PautaRequest pautaRequest) {
        validatePautaRequest(pautaRequest);
        PautaEntity pautaEntity = mapper.toPautaEntity(pautaRequest);
        pautaRepository.save(pautaEntity);
        return mapper.toPautaResponse(pautaEntity);
    }

    private void validatePautaRequest(PautaRequest pauta) {
        if (pauta.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data de início da pauta não pode ser menor que a data atual");
        }
        if (pauta.getDataHoraFim().isBefore(pauta.getDataHoraInicio())) {
            throw new BusinessException("Data de fim da pauta não pode ser menor que a data de início");
        }
    }

    public PautaResponse consultarPautaPorId(Long id) {
        return pautaRepository.findById(id)
                .map(mapper::toPautaResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));
    }
}
