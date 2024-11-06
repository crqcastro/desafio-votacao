package br.com.cesarcastro.votacao.controller.rest;

import br.com.cesarcastro.votacao.controller.api.IPautaController;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.service.pauta.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PautaController implements IPautaController {
    private final PautaService service;
    @Override
    public ResponseEntity<PautaResponse> cadastrarPauta(PautaRequest request) {
        PautaResponse pautaResponse = service.cadastrarPauta(request);
        return ResponseEntity.created(URI.create("/v1/pauta/" + pautaResponse.id())).body(pautaResponse);
    }
}
