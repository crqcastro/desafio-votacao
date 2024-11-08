package br.com.cesarcastro.votacao.controller.rest;

import br.com.cesarcastro.votacao.domain.model.filtros.PautaFiltro;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.requests.VotoRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.service.pauta.PautaService;
import br.com.cesarcastro.votacao.support.exceptions.BusinessException;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PautaControllerTest {

    private static final String URL = "/v1/pauta";

    @MockBean
    private PautaService pautaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUpAll() {
        Dotenv dotenv = Dotenv.configure()
                .filename("votacao-test.env")
                .load();
        System.setProperty("APP_ENDPOINT_BASE", dotenv.get("APP_ENDPOINT_BASE"));
        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("SPRING_JPA_DATABASE_PLATFORM", dotenv.get("SPRING_JPA_DATABASE_PLATFORM"));
        System.setProperty("SPRING_JPA_HIBERNATE_DDL_AUTO", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO"));
        System.setProperty("USUARIO_API_URL", dotenv.get("USUARIO_API_URL"));
    }

    @Test
    void testDeveCadastrarPautaComSucesso() throws Exception {
        LocalDateTime dataHoraInicio = LocalDateTime.now().plusDays(1);
        LocalDateTime dataHoraFim = LocalDateTime.now().plusDays(2);

        PautaRequest pautaRequest = new PautaRequest();
        pautaRequest.setDescricao("Pauta Teste");
        pautaRequest.setNome("Nome da Pauta");
        pautaRequest.setDataHoraInicio(dataHoraInicio);
        pautaRequest.setDataHoraFim(dataHoraFim);

        PautaResponse pautaResponse = new PautaResponse(1L,
                "Nome da Pauta",
                "Pauta Teste",
                null,
                null,
                null,
                null,
                null);

        given(pautaService.cadastrarPauta(any())).willReturn(pautaResponse);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pautaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Pauta Teste"))
                .andExpect(jsonPath("$.nome").value("Nome da Pauta"));
    }

    @Test
    void testDeveReceberBadRequestAoCadastrarPautaComDataInferiorAAtual() throws Exception {
        LocalDateTime dataHoraInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime dataHoraFim = LocalDateTime.now().plusDays(2);

        PautaRequest pautaRequest = new PautaRequest();
        pautaRequest.setDescricao("Pauta Teste");
        pautaRequest.setNome("Nome da Pauta");
        pautaRequest.setDataHoraInicio(dataHoraInicio);
        pautaRequest.setDataHoraFim(dataHoraFim);

        given(pautaService.cadastrarPauta(any())).willCallRealMethod();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pautaRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados invalidos."));
    }

    @Test
    void testDeveReceberBadRequestAoCadastrarPautaComDataFinalMenorQueInicial() throws Exception {
        LocalDateTime dataHoraInicio = LocalDateTime.now().plusDays(1);
        LocalDateTime dataHoraFim = LocalDateTime.now();

        PautaRequest pautaRequest = new PautaRequest();
        pautaRequest.setDescricao("Pauta Teste");
        pautaRequest.setNome("Nome da Pauta");
        pautaRequest.setDataHoraInicio(dataHoraInicio);
        pautaRequest.setDataHoraFim(dataHoraFim);

        given(pautaService.cadastrarPauta(any())).willCallRealMethod();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pautaRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados invalidos."));
    }

    @Test
    void testDeveConsultarPautaPorIdComSucesso() throws Exception {

        PautaResponse pautaMock = new PautaResponse(1L,
                "Pauta Teste",
                "Nome da Pauta",
                null,
                null,
                null,
                null,
                null);

        given(pautaService.consultarPautaPorId(1L)).willReturn(pautaMock);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pautaMock.id()))
                .andExpect(jsonPath("$.descricao").value(pautaMock.descricao()))
                .andExpect(jsonPath("$.nome").value(pautaMock.nome()));
    }

    @Test
    void testDeveConsultarPautaPorIdRecebendoNotFound() throws Exception {

        given(pautaService.consultarPautaPorId(1L)).willThrow(new RecursoNaoEncontradoException("Pauta não encontrada"));

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
    }

    @Test
    void testDeveListarComSucesso() throws Exception {
        Page<PautaResponse> expected = Page.empty();

        PautaFiltro filtros = PautaFiltro.builder().build();
        given(pautaService.listar(filtros)).willReturn(expected);

        mockMvc.perform(get(URL + "?paginaAtual=0&itensPorPagina=30&orderBy=id&direction=desc"))
                .andExpect(status().isOk());
    }

    @Test
    void deveAbrirSessaoComSucesso() throws Exception {
        PautaResponse pautaResponse = new PautaResponse(1L,
                "Nome da Pauta",
                "Pauta Teste",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                true,
                0L,
                0L);

        given(pautaService.abrirSessao(1L, 1)).willReturn(pautaResponse);

        mockMvc.perform(patch(URL + "/1/abrir-sessao?minutos=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Pauta Teste"))
                .andExpect(jsonPath("$.nome").value("Nome da Pauta"))
                .andExpect(jsonPath("$.pautaAberta").value(TRUE));
    }

    @Test
    void deveAbrirSessaoNotFoundParaPautaNaoEncontrada() throws Exception {

        given(pautaService.abrirSessao(1L, 1)).willThrow(new RecursoNaoEncontradoException("Pauta não encontrada"));

        mockMvc.perform(patch(URL + "/1/abrir-sessao?minutos=1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
    }

    @Test
    void deveAbrirSessaoBadRequestParaPautaJaEncerrada() throws Exception {
        given(pautaService.abrirSessao(1L, 1)).willThrow(new BusinessException("Pauta já encerrada"));

        mockMvc.perform(patch(URL + "/1/abrir-sessao?minutos=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Pauta já encerrada"));
    }

    @Test
    void deveAbrirSessaoBadRequestParaPautaJaAberta() throws Exception {

        given(pautaService.abrirSessao(1L, 1)).willThrow(new BusinessException("Pauta já aberta"));

        mockMvc.perform(patch(URL + "/1/abrir-sessao?minutos=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Pauta já aberta"));
    }

    @Test
    void deveEncerrarSessaoComSucesso() throws Exception {
        PautaResponse pautaResponse = new PautaResponse(1L,
                "Nome da Pauta",
                "Pauta Teste",
                LocalDateTime.now().minusMinutes(2),
                LocalDateTime.now().plusMinutes(2),
                false,
                0L,
                0L);

        given(pautaService.encerrarSessao(1L)).willReturn(pautaResponse);

        mockMvc.perform(patch(URL + "/1/encerrar-sessao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Pauta Teste"))
                .andExpect(jsonPath("$.nome").value("Nome da Pauta"))
                .andExpect(jsonPath("$.pautaAberta").value(FALSE));
    }

    @Test
    void deveEncerrarSessaoNotFoundParaPautaNaoEncontrada() throws Exception {

        given(pautaService.encerrarSessao(1L)).willThrow(new RecursoNaoEncontradoException("Pauta não encontrada"));

        mockMvc.perform(patch(URL + "/1/encerrar-sessao"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
    }

    @Test
    void deveEncerrarSessaoBadRequestParaPautaJaEncerrada() throws Exception {

        given(pautaService.encerrarSessao(1L)).willThrow(new BusinessException("Pauta já encerrada"));

        mockMvc.perform(patch(URL + "/1/encerrar-sessao"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Pauta já encerrada"));
    }

    @Test
    void deveVotarComSucessoEmPautaAberta() throws Exception {

        doNothing().when(pautaService).votar(any(VotoRequest.class));

        mockMvc.perform(patch(URL + "/votar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VotoRequest(1L, "99296756049", TRUE))))
                .andExpect(status().isOk());

    }

    @Test
    void deveVotarComErroVotoDuplicadoEmPautaAberta() throws Exception {

        Mockito.doThrow(new BusinessException("Voto já computado")).when(pautaService).votar(any(VotoRequest.class));

        mockMvc.perform(patch(URL + "/votar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VotoRequest(1L, "99296756049", TRUE))))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deveVotarComErroVotoUsuauarioNaoEncontrado() throws Exception {

        Mockito.doThrow(new RecursoNaoEncontradoException("Usuario nao encontrado")).when(pautaService).votar(any(VotoRequest.class));

        mockMvc.perform(patch(URL + "/votar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VotoRequest(1L, "99296756049", TRUE))))
                .andExpect(status().isNotFound());

    }

    @Test
    void deveVotarComErroPautaNaoEncontrada() throws Exception {

        Mockito.doThrow(new RecursoNaoEncontradoException("Pauta Não Encontrada")).when(pautaService).votar(any(VotoRequest.class));

        mockMvc.perform(patch(URL + "/votar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new VotoRequest(1L, "99296756049", TRUE))))
                .andExpect(status().isNotFound());

    }
}
