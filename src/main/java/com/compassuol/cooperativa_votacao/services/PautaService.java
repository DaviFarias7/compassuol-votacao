package com.compassuol.cooperativa_votacao.services;

import com.compassuol.cooperativa_votacao.dto.PautaRequest;
import com.compassuol.cooperativa_votacao.exception.PautaNaoEncontradaException;
import com.compassuol.cooperativa_votacao.exception.SessaoJaAbertaException;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PautaService {
    private final PautaRepository repository;

    public Pauta criarPauta(PautaRequest request) {
        Pauta pauta = Pauta.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .sessaoEncerrada(false)
                .build();

        return repository.save(pauta);
    }

    public Pauta abrirSessaoVotacao(Long pautaId, Integer minutos) throws SessaoJaAbertaException {
        Pauta pauta = repository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(pautaId));

        if (pauta.getDataAbertura() != null) {
            throw new SessaoJaAbertaException(pautaId);
        }

        LocalDateTime agora = LocalDateTime.now();
        pauta.setDataAbertura(agora);
        pauta.setDataFechamento(agora.plusMinutes(minutos != null ? minutos : 1));

        return repository.save(pauta);
    }

    public Pauta buscarPautaPorId(Long pautaId) {
        return repository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(pautaId));
    }
}

