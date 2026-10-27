package com.friodiag.friodiag.exception;

import com.friodiag.friodiag.dto.ErroResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInesperado(Exception ex) {
        log.error("Erro inesperado ao processar requisição", ex);

        ErroResponse resposta = new ErroResponse(
                500,
                "Erro interno do servidor",
                List.of(),
                OffsetDateTime.now()
        );

        return ResponseEntity.internalServerError().body(resposta);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(
            MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        ErroResponse resposta = new ErroResponse(
                400,
                "Dados inválidos",
                detalhes,
                OffsetDateTime.now()
        );

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> tratarStatus(
            ResponseStatusException ex) {

        ErroResponse resposta = new ErroResponse(
                ex.getStatusCode().value(),
                ex.getReason(),
                List.of(),
                OffsetDateTime.now()
        );

        return ResponseEntity.status(ex.getStatusCode()).body(resposta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException ex) {

        ErroResponse resposta = new ErroResponse(
                400,
                "JSON inválido ou corpo da requisição ausente",
                List.of(),
                OffsetDateTime.now()
        );

        return ResponseEntity.badRequest().body(resposta);
    }



}
