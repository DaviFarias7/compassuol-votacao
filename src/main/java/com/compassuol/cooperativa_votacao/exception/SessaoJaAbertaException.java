package com.compassuol.cooperativa_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessaoJaAbertaException extends RuntimeException {
    public SessaoJaAbertaException(Long pautaId) {
        super("Sessão de votação já está aberta para a pauta ID: " + pautaId);
    }
}
