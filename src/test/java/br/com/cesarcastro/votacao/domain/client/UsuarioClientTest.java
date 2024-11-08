package br.com.cesarcastro.votacao.domain.client;

import br.com.cesarcastro.votacao.domain.model.clients.in.Usuario;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import br.com.cesarcastro.votacao.util.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioClientTest {
    @Mock
    Client client;
    ObjectMapper om;
    UsuarioClient usuarioClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        om = new ObjectMapper();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.findAndRegisterModules();
        usuarioClient = new UsuarioClient(client, om, "http://localhost:8080");
    }

    @Test
    void testBuscarUsuarioPorCpf() throws JsonProcessingException {
        String cpf = "12345678900";
        Usuario usuario = TestUtils.generateRandom(Usuario.class);
        List<Usuario> list = List.of(usuario);
        when(client.get(anyString(), any(HttpHeaders.class))).thenReturn(om.writeValueAsString(list));
        Usuario response = usuarioClient.buscarUsuarioPorCpf(cpf);
        assertEquals(usuario.getId(), response.getId());
        verify(client).get(anyString(), any(HttpHeaders.class));
    }

    @Test
    void testBuscarUsuarioPorCpfDeveLancarRecursoNaoEncontradoException() throws JsonProcessingException {
        String cpf = "12345678900";
        List<Usuario> list = List.of();
        when(client.get(anyString(), any(HttpHeaders.class))).thenReturn(om.writeValueAsString(list));
        assertThrows(RecursoNaoEncontradoException.class, () -> usuarioClient.buscarUsuarioPorCpf(cpf),
                "Usuário não encontrado");
        verify(client).get(anyString(), any(HttpHeaders.class));
    }

    @Test
    void testBuscarUsuarioPorCpfDeveLancarRunTimeExceptionFormatoResposta() throws JsonProcessingException {
        String cpf = "12345678900";
        when(client.get(anyString(), any(HttpHeaders.class))).thenReturn("invalid json");
        assertThrows(RuntimeException.class, () -> usuarioClient.buscarUsuarioPorCpf(cpf));
        verify(client).get(anyString(), any(HttpHeaders.class));
    }

}