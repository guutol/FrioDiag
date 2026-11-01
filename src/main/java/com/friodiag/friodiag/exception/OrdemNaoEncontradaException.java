package com.friodiag.friodiag.exception;

import java.util.UUID;

public class OrdemNaoEncontradaException extends RuntimeException {

    public OrdemNaoEncontradaException(UUID id) {
        super("Ordem de serviço não encontrada: " + id);
    }
}
