package br.com.cesarcastro.votacao.domain.service.pauta;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.filtros.PautaFiltro;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import br.com.cesarcastro.votacao.mappers.PautaMapper;
import br.com.cesarcastro.votacao.support.exceptions.BusinessException;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import br.com.cesarcastro.votacao.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PautaServiceTest {
    @Mock
    private PautaRepository pautaRepository;
    private PautaMapper mapper;
    private PautaService pautaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mapper = PautaMapper.INSTANCE;
        this.pautaService = new PautaService(pautaRepository, mapper);
    }

    @Test
    public void testCadastrarPauta() {
        LocalDateTime dataHoraInicio = LocalDateTime.now().plusDays(1);
        LocalDateTime dataHoraFim = LocalDateTime.now().plusDays(2);
        PautaRequest pautaRequest = TestUtils.generateRandom(PautaRequest.class);
        pautaRequest.setDataHoraInicio(dataHoraInicio);
        pautaRequest.setDataHoraFim(dataHoraFim);
        PautaEntity pautaEntity = this.mapper.toPautaEntity(pautaRequest);
        pautaEntity.setId(1L);
        when(pautaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        PautaResponse pautaResponse = this.pautaService.cadastrarPauta(pautaRequest);

        verify(pautaRepository).save(any());
    }

    @Test
    public void deveLancarBusinessExceptionQuandoDataHoraInicioForMenorQueDataAtual() {
        PautaRequest pautaRequest = TestUtils.generateRandom(PautaRequest.class);
        pautaRequest.setDataHoraInicio(LocalDateTime.now().minusDays(1));
        assertThrows(BusinessException.class, () -> this.pautaService.cadastrarPauta(pautaRequest)
                , "Data de início da pauta não pode ser menor que a data atual");
    }

    @Test
    public void deveLancarBusinessExceptionQuandoDataHoraInicioForMaiorQueAFInal() {
        PautaRequest pautaRequest = TestUtils.generateRandom(PautaRequest.class);
        pautaRequest.setDataHoraInicio(LocalDateTime.now());
        pautaRequest.setDataHoraFim(LocalDateTime.now().plusDays(1));
        assertThrows(BusinessException.class, () -> this.pautaService.cadastrarPauta(pautaRequest)
                , "Data de fim da pauta não pode ser menor que a data de início");
    }

    @Test
    public void deveConsultarPautaPorId() {
        PautaEntity pautaEntity = TestUtils.generateRandom(PautaEntity.class);
        pautaEntity.setId(1L);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));
        PautaResponse pautaResponse = this.pautaService.consultarPautaPorId(1L);
        verify(pautaRepository).findById(1L);
        assertEquals(1L, pautaResponse.id());
    }

    @Test
    public void deveConsultarPautaPorIdLancandoRecursoNaoEncontrado() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> this.pautaService.consultarPautaPorId(1L));
        verify(pautaRepository).findById(1L);
    }

    @Test
    public void deveListarPautas() {
        when(pautaRepository.findAll(any(Specification.class), (Pageable) any(Pageable.class))).thenReturn(Page.empty());
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<PautaResponse> listar = pautaService.listar(PautaFiltro.builder().pageRequest(pageRequest).build());
        verify(pautaRepository).findAll(any(Specification.class), (Pageable) any(Pageable.class));
        assertEquals(0, listar.getTotalElements());
    }
}