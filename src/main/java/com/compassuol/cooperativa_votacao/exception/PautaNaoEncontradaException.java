package com.compassuol.cooperativa_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PautaNaoEncontradaException extends RuntimeException {
    public PautaNaoEncontradaException(Long id) {
        super("Pauta não encontrada com ID: " + id);
    }
}
