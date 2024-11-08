package br.com.cesarcastro.votacao.domain.service.pauta;

import br.com.cesarcastro.votacao.domain.client.UsuarioClient;
import br.com.cesarcastro.votacao.domain.model.clients.in.Usuario;
import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.entities.VotoEntity;
import br.com.cesarcastro.votacao.domain.model.filtros.PautaFiltro;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.requests.VotoRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import br.com.cesarcastro.votacao.domain.repositories.VotoRepository;
import br.com.cesarcastro.votacao.domain.repositories.especifications.PautaSpecification;
import br.com.cesarcastro.votacao.mappers.PautaMapper;
import br.com.cesarcastro.votacao.support.exceptions.BusinessException;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;
    private final PautaMapper mapper;
    private final UsuarioClient usuarioClient;

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

    public Page<PautaResponse> listar(PautaFiltro filtro) {

        Pageable pageable = filtro.getPageRequest();

        Specification<PautaEntity> specification = Specification
                .where(PautaSpecification.idEqual(filtro.getId()))
                .and(PautaSpecification.nomeContains(filtro.getNome()))
                .and(PautaSpecification.dataInicial(filtro.getDataInicial()))
                .and(PautaSpecification.dataFim(filtro.getDataFinal()))
                .and(PautaSpecification.diaInicio(filtro.getDataInicio()))
                .and(PautaSpecification.diaFim(filtro.getDataFim()))
                .and(PautaSpecification.pautaAberta(filtro.getPautaAberta()));

        return pautaRepository.findAll(specification, pageable)
                .map(mapper::toPautaResponse);
    }

    public PautaResponse abrirSessao(Long id, Integer minutos) {
        PautaEntity pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

        if ((pauta.getDataHoraInicio().isBefore(LocalDateTime.now())
                && pauta.getDataHoraFim().isAfter(LocalDateTime.now()))
                || pauta.getPautaAberta()) {
            throw new BusinessException("Pauta já aberta");
        }

        if (pauta.getDataHoraFim().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Pauta já encerrada");
        }


        pauta.setDataHoraInicio(LocalDateTime.now());
        pauta.setDataHoraFim(pauta.getDataHoraInicio().plusMinutes(minutos));
        pauta.setPautaAberta(TRUE);
        pautaRepository.save(pauta);

        return mapper.toPautaResponse(pauta);
    }

    public PautaResponse encerrarSessao(Long id) {
        PautaEntity pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

        if (pauta.getDataHoraFim().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Pauta já encerrada");
        }

        if (!pauta.getPautaAberta()) {
            throw new BusinessException("Pauta já encerrada");
        }

        pauta.setDataHoraFim(LocalDateTime.now());

        pauta.setPautaAberta(false);
        pautaRepository.save(pauta);

        return mapper.toPautaResponse(pauta);
    }

    public void votar(VotoRequest request) {
        Usuario usuario = usuarioClient.buscarUsuarioPorCpf(request.getCpf());

        PautaEntity pauta = pautaRepository.findById(request.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

        validarPautaParaVotacao(pauta);

        validarVoto(request.getId(), usuario);

        VotoEntity voto = VotoEntity.builder()
                .cpf(request.getCpf())
                .voto(request.getVoto())
                .pauta(pauta)
                .build();

        pauta.getVotos().add(voto);

        if (voto.getVoto()) {
            Long votosAtuais = pauta.getTotalVotosSim();
            pauta.setTotalVotosSim(Objects.isNull(votosAtuais) ? 0 : votosAtuais + 1);
        } else {
            Long votosAtuais = pauta.getTotalVotosNao();
            pauta.setTotalVotosNao(Objects.isNull(votosAtuais) ? 0 : votosAtuais + 1);
        }

        pautaRepository.save(pauta);

        log.info("Voto registrado com sucesso! CPF: {}, Voto: {}, Pauta: {}", request.getCpf(), request.getVoto(), request.getId());
    }

    private void validarVoto(Long id, Usuario usuario) {
        if(!usuario.getHabilitado().isHabilitado())
            throw new BusinessException("Usuário não habilitado para votar");
        if(votoRepository.existsByCpfAndPautaId(usuario.getCpf(), id))
            throw new BusinessException("Usuário já votou nesta pauta");
    }

    private void validarPautaParaVotacao(PautaEntity pauta) {
        if (!pauta.getPautaAberta())
            throw new BusinessException("Pauta não está aberta para votação");
    }
}
