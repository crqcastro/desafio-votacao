package br.com.cesarcastro.votacao.controller.rest;

import br.com.cesarcastro.votacao.controller.api.IPautaController;
import br.com.cesarcastro.votacao.domain.model.filtros.PautaFiltro;
import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import br.com.cesarcastro.votacao.domain.service.pauta.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class PautaController implements IPautaController {
    private final PautaService service;

    @Override
    public ResponseEntity<PautaResponse> cadastrarPauta(PautaRequest request) {
        PautaResponse pautaResponse = service.cadastrarPauta(request);
        return ResponseEntity.created(URI.create("/v1/pauta/" + pautaResponse.id())).body(pautaResponse);
    }

    @Override
    public ResponseEntity<PautaResponse> consultarPautaPorId(Long id) {
        return ResponseEntity.ok(service.consultarPautaPorId(id));
    }

    @Override
    public Page<PautaResponse> listar(LocalDateTime dataInicial,
                                      LocalDateTime dataFinal,
                                      LocalDate dataInicio,
                                      LocalDate dataFim,
                                      Boolean pautaAberta,
                                      Long id,
                                      String nome,
                                      Integer paginaAtual,
                                      Integer itensPorPagina,
                                      String orderBy,
                                      String direction) {
        PageRequest pageRequest = PageRequest.of(paginaAtual, itensPorPagina,
                Sort.by(direction.equalsIgnoreCase("ASC") ? ASC : DESC, orderBy));

        PautaFiltro filtro = PautaFiltro.builder()
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .pautaAberta(pautaAberta)
                .id(id)
                .nome(nome)
                .pageRequest(pageRequest)
                .build();

        return service.listar(filtro);
    }
}
