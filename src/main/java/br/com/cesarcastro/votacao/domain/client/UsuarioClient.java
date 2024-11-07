package br.com.cesarcastro.votacao.domain.client;

import br.com.cesarcastro.votacao.domain.model.clients.in.Usuario;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UsuarioClient {
    private final Client client;
    private final ObjectMapper om;
    private final String urlBase;

    public static final String X_APPLICATION_ORIGIN = "x-application-origin";
    public static final String APPLICATION_NAME = "votacao";

    public UsuarioClient(Client client,
                         ObjectMapper om,
                         @Value("${USUARIO_API_URL}")
                         String urlBase) {
        this.client = client;
        this.om = om;
        this.urlBase = urlBase;
    }

    public Usuario buscarUsuarioPorCpf(String cpf) {
        try {
            List<Usuario> list = om.readValue(
                    client.get(urlBase.concat("/user").concat("?cpf=").concat(cpf), obterHeaders()),
                    new TypeReference<List<Usuario>>() {});
            if(list.isEmpty()) {
                throw new RecursoNaoEncontradoException("Usuário não encontrado");
            }
            return list.get(0);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders obterHeaders() {
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(X_APPLICATION_ORIGIN, APPLICATION_NAME);
        return headers;
    }
}
