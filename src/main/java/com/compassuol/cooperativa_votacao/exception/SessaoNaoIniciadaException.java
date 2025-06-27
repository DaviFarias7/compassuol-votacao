package com.compassuol.cooperativa_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessaoNaoIniciadaException extends RuntimeException {
    public SessaoNaoIniciadaException(Long pautaId) {
        super("Sessão de votação não iniciada para a pauta ID: " + pautaId);
    }
}
