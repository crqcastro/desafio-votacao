package br.com.cesarcastro.votacao.domain.service.pauta;

import br.com.cesarcastro.votacao.domain.client.UsuarioClient;
import br.com.cesarcastro.votacao.domain.model.clients.in.StatusEnum;
import br.com.cesarcastro.votacao.domain.model.clients.in.Usuario;
import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.filtros.PautaFiltro;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.requests.VotoRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.repositories.PautaRepository;
import br.com.cesarcastro.votacao.domain.repositories.VotoRepository;
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

import static br.com.cesarcastro.votacao.domain.model.clients.in.StatusEnum.ABLE_TO_VOTE;
import static br.com.cesarcastro.votacao.domain.model.clients.in.StatusEnum.UNABLE_TO_VOTE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PautaServiceTest {
    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private VotoRepository votoRepository;
    private PautaMapper mapper;
    private PautaService pautaService;
    @Mock
    private UsuarioClient usuarioClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mapper = PautaMapper.INSTANCE;
        this.pautaService = new PautaService(pautaRepository, votoRepository, mapper, usuarioClient);
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
        pautaRequest.setDataHoraFim(LocalDateTime.now().minusHours(1));
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

    @Test
    public void deveAbrirSessaoComSucesso() {
        PautaEntity pautaEntity = TestUtils.generateRandom(PautaEntity.class);
        pautaEntity.setDataHoraInicio(LocalDateTime.now().plusDays(1));
        pautaEntity.setDataHoraFim(LocalDateTime.now().plusDays(2));
        pautaEntity.setPautaAberta(false);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));
        when(pautaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        PautaResponse pautaResponse = this.pautaService.abrirSessao(1L, 1);
        verify(pautaRepository).save(any());
        verify(pautaRepository).findById(1L);
        assertEquals(true, pautaResponse.pautaAberta());
    }

    @Test
    public void deveAbrirSessaoRecursoNaoEncontrado() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> this.pautaService.abrirSessao(1L, 1),
                "Pauta não encontrada");
        verify(pautaRepository).findById(1L);
    }

    @Test
    public void deveAbrirSessaoBusinessExceptionPautaEncerrada() {
        PautaEntity pautaEntity = TestUtils.generateRandom(PautaEntity.class);
        pautaEntity.setDataHoraInicio(LocalDateTime.now().minusMinutes(2));
        pautaEntity.setDataHoraFim(LocalDateTime.now().minusMinutes(1));
        pautaEntity.setPautaAberta(false);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));
        assertThrows(BusinessException.class, () -> this.pautaService.abrirSessao(1L, 1),
                "Pauta já encerrada");
        verify(pautaRepository).findById(1L);

    }

    @Test
    public void deveAbrirSessaoBusinessExceptionPautaJaAberta() {
        PautaEntity pautaEntity = TestUtils.generateRandom(PautaEntity.class);
        pautaEntity.setDataHoraInicio(LocalDateTime.now().minusMinutes(2));
        pautaEntity.setDataHoraFim(LocalDateTime.now().plusMinutes(1));
        pautaEntity.setPautaAberta(true);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));
        assertThrows(BusinessException.class, () -> this.pautaService.abrirSessao(1L, 1),
                "Pauta já aberta");
        verify(pautaRepository).findById(1L);

    }

    @Test
    public void deveVotarComSucesso(){
        Usuario usuario = TestUtils.generateRandom(Usuario.class);
        usuario.setHabilitado(ABLE_TO_VOTE);
        when(usuarioClient.buscarUsuarioPorCpf(anyString())).thenReturn(usuario);
        PautaEntity entity = TestUtils.generateRandom(PautaEntity.class);
        entity.setPautaAberta(TRUE);
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(pautaRepository.save(any(PautaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        this.pautaService.votar(TestUtils.generateRandom(VotoRequest.class));
        verify(usuarioClient).buscarUsuarioPorCpf(anyString());
        verify(pautaRepository).findById(anyLong());
        verify(pautaRepository).save(any(PautaEntity.class));
    }

    @Test
    public void deveVotarSemSucessoUsuarioInexistente(){
        when(usuarioClient.buscarUsuarioPorCpf(anyString())).thenThrow(new RecursoNaoEncontradoException("Recurso não encontrado"));
        assertThrows(RecursoNaoEncontradoException.class, () -> this.pautaService.votar(TestUtils.generateRandom(VotoRequest.class)),
                "Recurso não encontrado");
        verify(usuarioClient).buscarUsuarioPorCpf(anyString());
    }

    @Test
    public void deveVotarSemSucessoPautaNaoEncontrada(){
        Usuario usuario = TestUtils.generateRandom(Usuario.class);
        usuario.setHabilitado(ABLE_TO_VOTE);
        when(usuarioClient.buscarUsuarioPorCpf(anyString())).thenReturn(usuario);
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> this.pautaService.votar(TestUtils.generateRandom(VotoRequest.class)),
                "Pauta não encontrada");
        verify(usuarioClient).buscarUsuarioPorCpf(anyString());
        verify(pautaRepository).findById(anyLong());
    }
    @Test
    public void deveVotarSemSucessoVotoInvalido(){
        Usuario usuario = TestUtils.generateRandom(Usuario.class);
        usuario.setHabilitado(ABLE_TO_VOTE);
        when(usuarioClient.buscarUsuarioPorCpf(anyString())).thenReturn(usuario);
        PautaEntity entity = TestUtils.generateRandom(PautaEntity.class);
        entity.setPautaAberta(TRUE);
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(votoRepository.existsByCpfAndPautaId(anyString(), anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () -> this.pautaService.votar(TestUtils.generateRandom(VotoRequest.class)),
                "Usuário já votou nesta pauta");
        verify(usuarioClient).buscarUsuarioPorCpf(anyString());
        verify(pautaRepository).findById(anyLong());
        verify(votoRepository).existsByCpfAndPautaId(anyString(), anyLong());
    }
    @Test
    public void deveVotarSemSucessoVotoInvalidoUsuarioNaoHabilitado(){
        Usuario usuario = TestUtils.generateRandom(Usuario.class);
        usuario.setHabilitado(UNABLE_TO_VOTE);
        when(usuarioClient.buscarUsuarioPorCpf(anyString())).thenReturn(usuario);
        PautaEntity entity = TestUtils.generateRandom(PautaEntity.class);
        entity.setPautaAberta(TRUE);
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        assertThrows(BusinessException.class, () -> this.pautaService.votar(TestUtils.generateRandom(VotoRequest.class)),
                "Usuário não habilitado para votar");
        verify(usuarioClient).buscarUsuarioPorCpf(anyString());
        verify(pautaRepository).findById(anyLong());
    }

    @Test
    public void deveVotarSemSucessoPautaEncerrada(){
        Usuario usuario = TestUtils.generateRandom(Usuario.class);
        usuario.setHabilitado(ABLE_TO_VOTE);
        when(usuarioClient.buscarUsuarioPorCpf(anyString())).thenReturn(usuario);
        PautaEntity entity = TestUtils.generateRandom(PautaEntity.class);
        entity.setPautaAberta(FALSE);
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        assertThrows(BusinessException.class, () -> this.pautaService.votar(TestUtils.generateRandom(VotoRequest.class)),
                "Pauta não está aberta para votação");
        verify(usuarioClient).buscarUsuarioPorCpf(anyString());
        verify(pautaRepository).findById(anyLong());
    }

    @Test
    public void deveEncerrarSessaoRecursoNaoEncontrado() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> this.pautaService.encerrarSessao(1L),
                "Pauta não encontrada");
        verify(pautaRepository).findById(1L);
    }

    @Test
    public void deveEncerrarSessaoComSucesso() {
        PautaEntity pautaEntity = TestUtils.generateRandom(PautaEntity.class);
        pautaEntity.setDataHoraInicio(LocalDateTime.now().plusDays(1));
        pautaEntity.setDataHoraFim(LocalDateTime.now().plusDays(2));
        pautaEntity.setPautaAberta(true);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));
        when(pautaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        PautaResponse pautaResponse = this.pautaService.encerrarSessao(1L);
        verify(pautaRepository).save(any());
        verify(pautaRepository).findById(1L);
        assertEquals(false, pautaResponse.pautaAberta());
    }
    @Test
    public void deveEncerrarSessaoSemSucessoPautaEncerradaDataHoraFim() {
        PautaEntity pautaEntity = TestUtils.generateRandom(PautaEntity.class);
        pautaEntity.setDataHoraInicio(LocalDateTime.now().minusDays(1));
        pautaEntity.setDataHoraFim(LocalDateTime.now().minusMinutes(1));
        pautaEntity.setPautaAberta(true);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));
        assertThrows(BusinessException.class, () -> this.pautaService.encerrarSessao(1L),
                "Pauta já encerrada");
        verify(pautaRepository).findById(1L);
    }

    @Test
    public void deveEncerrarSessaoSemSucessoPautaStatus() {
        PautaEntity pautaEntity = TestUtils.generateRandom(PautaEntity.class);
        pautaEntity.setDataHoraInicio(LocalDateTime.now().minusDays(1));
        pautaEntity.setDataHoraFim(LocalDateTime.now().plusMinutes(1));
        pautaEntity.setPautaAberta(false);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pautaEntity));
        assertThrows(BusinessException.class, () -> this.pautaService.encerrarSessao(1L),
                "Pauta já encerrada");
        verify(pautaRepository).findById(1L);
    }


}