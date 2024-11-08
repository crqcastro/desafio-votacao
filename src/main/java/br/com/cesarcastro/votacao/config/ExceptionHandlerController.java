package br.com.cesarcastro.votacao.config;

import br.com.cesarcastro.votacao.support.exceptions.BusinessException;
import br.com.cesarcastro.votacao.support.exceptions.InternalErrorException;
import br.com.cesarcastro.votacao.support.exceptions.RecursoNaoEncontradoException;
import br.com.cesarcastro.votacao.support.exceptions.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    /* ############################################ 4XX CLIENT EXCEPTION ############################################ */

    @ExceptionHandler(value = {AssertionError.class, IllegalArgumentException.class, BusinessException.class, MissingRequestValueException.class})
    public ResponseEntity<ErrorDto> handleBadRequest(Exception e, HttpServletRequest request) {
        log.error("Erro de validacao de regra de negocio.", e);
        ErrorDto err = gerarError(request, BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(value = {InternalErrorException.class})
    public ResponseEntity<ErrorDto> handleInternalErrorException(Exception e, HttpServletRequest request) {
        log.error("Erro de Interno do servidor.", e);
        ErrorDto err = gerarError(request, INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(value = {RecursoNaoEncontradoException.class})
    public ResponseEntity<ErrorDto> handleNotFound(Exception e, HttpServletRequest request) {
        log.error("Erro de recurso nao enconterado.", e);
        ErrorDto err = gerarError(request, NOT_FOUND, e.getMessage());
        return ResponseEntity.status(NOT_FOUND).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> tratarErroArgumentoInvalido(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Dados invalidos.", e);
        ErrorDto err = gerarError(request, BAD_REQUEST, "Dados invalidos.");

        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            String errorKey = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            err.addViolation(errorKey, errorMessage);
        }
        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        fieldErrorList.forEach(f -> err.addViolation(f.getField(), f.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }


    /* ############################################ 5XX SERVER EXCEPTION ############################################ */

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto> handleException(Exception e, HttpServletRequest request) {
        log.error("Erro de infraestrutura do servico.", e);
        ErrorDto err = gerarError(request, INTERNAL_SERVER_ERROR, "Erro interno do servidor. Entre em contato com o suporte.");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(APPLICATION_JSON).body(err);
    }

    private ErrorDto gerarError(HttpServletRequest request, HttpStatus httpStatus, String message) {
        return ErrorDto.builder()
                .timeStamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .path(request.getServletPath())
                .violations(new TreeSet<>())
                .build();
    }
}