package com.friodiag.friodiag.exception;

import com.friodiag.friodiag.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(
            MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos", detalhes);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException ex) {
        return resposta(HttpStatus.BAD_REQUEST,
                "JSON inválido ou corpo da requisição ausente", List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarParametroInvalido(
            MethodArgumentTypeMismatchException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Parâmetro inválido",
                List.of("O parâmetro '" + ex.getName() + "' possui um formato inválido"));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErroResponse> tratarOrdenacaoInvalida(
            PropertyReferenceException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Parâmetro inválido",
                List.of("Não é possível ordenar por '" + ex.getPropertyName() + "'"));
    }

    @ExceptionHandler(OrdemNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> tratarOrdemNaoEncontrada(
            OrdemNaoEncontradaException ex) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> tratarRotaInexistente(
            NoResourceFoundException ex) {
        return resposta(HttpStatus.NOT_FOUND, "Recurso não encontrado", List.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> tratarMetodoNaoSuportado(
            HttpRequestMethodNotSupportedException ex) {
        return resposta(HttpStatus.METHOD_NOT_ALLOWED,
                "Método " + ex.getMethod() + " não suportado", List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInesperado(Exception ex) {
        log.error("Erro inesperado ao processar requisição", ex);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor", List.of());
    }

    private ResponseEntity<ErroResponse> resposta(
            HttpStatus status, String mensagem, List<String> detalhes) {
        ErroResponse corpo = new ErroResponse(
                status.value(), mensagem, detalhes, OffsetDateTime.now());
        return ResponseEntity.status(status).body(corpo);
    }
}
