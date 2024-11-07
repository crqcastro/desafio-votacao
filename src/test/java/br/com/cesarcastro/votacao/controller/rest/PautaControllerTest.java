package br.com.cesarcastro.votacao.controller.rest;

import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PautaControllerTest {

    private static final String URL = "/v1/pauta";

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

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pautaRequest)))
                .andExpect(status().isCreated()) // Verifica se a resposta foi OK
                .andExpect(jsonPath("$.id").value(1)) // Verifica o ID retornado
                .andExpect(jsonPath("$.descricao").value("Pauta Teste")) // Verifica a descrição
                .andExpect(jsonPath("$.nome").value("Nome da Pauta")); // Verifica o nome
    }
}
