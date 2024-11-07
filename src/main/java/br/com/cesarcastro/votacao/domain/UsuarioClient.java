package br.com.cesarcastro.votacao.domain;

import br.com.cesarcastro.votacao.domain.model.clients.in.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UsuarioClient",
        url = "${USUARIO-API-URL}",
        configuration = FeignClientProperties.FeignClientConfiguration.class)
@Component
public interface UsuarioClient {
    @GetMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Usuario> getUsuarioByCPF(@RequestParam("cpf") String cpf);
}
