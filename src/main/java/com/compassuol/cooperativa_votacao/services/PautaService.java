package com.compassuol.cooperativa_votacao.services;

import com.compassuol.cooperativa_votacao.dto.PautaRequest;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

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

    public Pauta abrirSessaoVotacao(Long pautaId, Integer minutos) {
        Pauta pauta = repository.findById(pautaId)
                .orElseThrow(() -> new NoSuchElementException("Pauta com id " + pautaId + " não encontrada"));

        if (pauta.getDataAbertura() != null) {
            throw new IllegalStateException("Sessão já está aberta para a pauta com id " + pautaId);
        }

        LocalDateTime agora = LocalDateTime.now();
        pauta.setDataAbertura(agora);
        pauta.setDataFechamento(agora.plusMinutes(minutos != null ? minutos : 1));

        return repository.save(pauta);
    }

    public Pauta buscarPautaPorId(Long pautaId) {
        return repository.findById(pautaId)
                .orElseThrow(() -> new NoSuchElementException("Pauta com id " + pautaId + " não encontrada"));
    }
}
