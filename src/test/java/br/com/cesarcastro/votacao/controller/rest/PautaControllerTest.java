package br.com.cesarcastro.votacao.controller.rest;

import br.com.cesarcastro.votacao.domain.model.entities.PautaEntity;
import br.com.cesarcastro.votacao.domain.model.filtros.PautaFiltro;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.service.pauta.PautaService;
import br.com.cesarcastro.votacao.mappers.PautaMapper;
import br.com.cesarcastro.votacao.mappers.PautaMapperImpl;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PautaControllerTest {

    private static final String URL = "/v1/pauta";

    @MockBean
    private PautaService pautaService;

    @MockBean
    private PautaMapper pautaMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUpAll() {
        Dotenv dotenv = Dotenv.configure()
                .filename("votacao-test.env")
                .load();
        System.setProperty("APP_ENDPOINT_BASE", dotenv.get("APP_ENDPOINT_BASE"));
        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        System.setProperty("SPRING_JPA_DATABASE_PLATFORM", dotenv.get("SPRING_JPA_DATABASE_PLATFORM"));
        System.setProperty("SPRING_JPA_HIBERNATE_DDL_AUTO", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO"));
    }

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testDeveCadastrarPautaComSucesso() throws Exception {
        LocalDateTime dataHoraInicio = LocalDateTime.now().plusDays(1);
        LocalDateTime dataHoraFim = LocalDateTime.now().plusDays(2);

        PautaRequest pautaRequest = new PautaRequest();
        pautaRequest.setDescricao("Pauta Teste");
        pautaRequest.setNome("Nome da Pauta");
        pautaRequest.setDataHoraInicio(dataHoraInicio);
        pautaRequest.setDataHoraFim(dataHoraFim);

        PautaResponse pautaResponse = new PautaResponse(1L,
                "Pauta Teste",
                "Nome da Pauta",
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
    public void testDeveReceberBadRequestAoCadastrarPautaComDataInferiorAAtual() throws Exception {
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
                .andExpect(jsonPath("$.message").value("Data de início da pauta não pode ser menor que a data atual"));
    }

    @Test
    public void testDeveReceberBadRequestAoCadastrarPautaComDataFinalMenorQueInicial() throws Exception {
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
                .andExpect(jsonPath("$.message").value("Data de fim da pauta não pode ser menor que a data de início"));
    }

    @Test
    public void testDeveConsultarPautaPorIdComSucesso() throws Exception {

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
    public void testDeveConsultarPautaPorIdRecebendoNotFound() throws Exception {

        given(pautaService.consultarPautaPorId(1L)).willThrow(new RecursoNaoEncontradoException("Pauta não encontrada"));

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada"));
    }

    @Test
    public void testDeveListarComSucesso() throws Exception {
        Page<PautaResponse> expected = Page.empty();

        PautaFiltro filtros = PautaFiltro.builder().build();
        given(pautaService.listar(filtros)).willReturn(expected);

        mockMvc.perform(get(URL + "?paginaAtual=0&itensPorPagina=30&orderBy=id&direction=desc"))
                .andExpect(status().isOk());
    }

}
