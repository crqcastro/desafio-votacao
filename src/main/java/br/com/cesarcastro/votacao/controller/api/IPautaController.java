package br.com.cesarcastro.votacao.controller.api;

import br.com.cesarcastro.votacao.domain.model.requests.PautaRequest;
import br.com.cesarcastro.votacao.domain.model.requests.VotoRequest;
import br.com.cesarcastro.votacao.domain.model.responses.PautaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
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


    @Operation(summary = "Endpoint para Consultar listar pautas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pautas obtidas com sucesso.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PautaResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Page<PautaResponse> listar(
            @Parameter(description = "A data/hora exata de inicio da pauta para pesquisa no formato ISO-DATE_TIME.")
            @RequestParam(name = "dataHoraInicio", required = false)
            @DateTimeFormat(iso = DATE_TIME)
            LocalDateTime dataInicial,

            @Parameter(description = "A data/hora exata do fim da pauta para pesquisa no formato ISO-DATE_TIME.")
            @RequestParam(name = "dataHoraFim", required = false)
            @DateTimeFormat(iso = DATE_TIME)
            LocalDateTime dataFinal,

            @Parameter(description = "A data do inicio da pauta para pesquisa no formato ISO-DATE.")
            @RequestParam(name = "dataInicio", required = false)
            @DateTimeFormat(iso = DATE)
            LocalDate dataInicio,

            @Parameter(description = "A data do fim da pauta para pesquisa no formato ISO-DATE.")
            @RequestParam(name = "dataFim", required = false)
            @DateTimeFormat(iso = DATE)
            LocalDate dataFim,

            @Parameter(description = "Pautas abertas (true/false).")
            @RequestParam(name = "pautaAberta", required = false)
            Boolean pautaAberta,

            @Parameter(description = "Id da pauta a ser pesquisada.")
            @Min(1)
            @RequestParam(name = "id", required = false)
            Long id,

            @Parameter(description = "Nome da pauta.")
            @Size(min = 8, max = 32)
            @RequestParam(name = "nome", required = false)
            String nome,

            @Parameter(description = "Página a ser retornada pela consulta.")
            @Min(value = 0)
            @RequestParam(name = "paginaAtual", defaultValue = "0", required = false)
            Integer paginaAtual,

            @Parameter(description = "Quantidade máxima de registros retornados em cada página.")
            @Min(value = 1)
            @Max(value = 100)
            @RequestParam(name = "itensPorPagina", defaultValue = "5", required = false)
            Integer itensPorPagina,

            @Parameter(description = "Campo a ser ordenado")
            @RequestParam(name = "orderBy", defaultValue = "id", required = false)
            String orderBy,

            @Parameter(description = "Direção da ordenação (asc/desc)")
            @RequestParam(name = "direction", defaultValue = "asc", required = false)
            String direction
    );

    @Operation(summary = "Endpoint para abrir sessao de pauta manualmente, sem considerar a data de inicio e fim. [ATENÇÃO!] Esse método altera as datas de inicio e fim cadastradas originalmente!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pautas obtidas com sucesso.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @PatchMapping(produces = APPLICATION_JSON_VALUE, path = "/{id}/abrir-sessao")
    ResponseEntity<PautaResponse> abrirSessao(@PathVariable("id")
                                              @Parameter(description = "Id da pauta a ser aberta")
                                              Long id,
                                              @Parameter(description = "Duração, em minutos, da pauta.")
                                              @Min(value = 1)
                                              @RequestParam(value = "minutos", defaultValue = "1", required = false)
                                              Integer minutos);

    @Operation(summary = "Endpoint para abrir sessao de pauta manualmente, sem considerar a data de inicio e fim. [ATENÇÃO!] Esse método altera as datas de inicio e fim cadastradas originalmente!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pautas obtidas com sucesso.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @PatchMapping(produces = APPLICATION_JSON_VALUE, path = "/{id}/encerrar-sessao")
    ResponseEntity<PautaResponse> encerrarSessao(@PathVariable("id")
                                                 @Parameter(description = "Id da pauta a ser encerrada")
                                                 Long id);

    @Operation(summary = "Endpoint para escolha em pauta aberta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto computado com sucesso.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição errada.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @PatchMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> votar(@RequestBody @Valid VotoRequest request);
}
