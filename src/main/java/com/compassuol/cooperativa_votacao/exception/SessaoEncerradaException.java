package com.compassuol.cooperativa_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessaoEncerradaException extends RuntimeException {
    public SessaoEncerradaException(Long pautaId) {
        super("Sessão de votação já encerrada para a pauta ID: " + pautaId);
    }
}
