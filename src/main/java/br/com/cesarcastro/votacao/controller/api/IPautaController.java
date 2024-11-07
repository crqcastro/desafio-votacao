package br.com.cesarcastro.votacao.controller.api;

import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Pauta", description = "Rotas para Pautas.")
@CrossOrigin
@RequestMapping("/v1/pauta")
@Transactional
@Validated
public interface IPautaController {

    @Operation(summary = "Endpoint para cadastrar uma nova pauta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta cadastrada com sucesso.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição com formato inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<PautaResponse> cadastrarPauta(@RequestBody @Valid PautaRequest request);

    @Operation(summary = "Endpoint para Consultar uma pauta pelo Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta obtida com sucesso.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/{id}")
    ResponseEntity<PautaResponse> consultarPautaPorId(@PathVariable(name = "id")
                                                      @Parameter(description = "O identificador da pauta.", required = true)
                                                      @Min(value = 1, message = "deve ser maior que zero.")
                                                      Long id);
}
