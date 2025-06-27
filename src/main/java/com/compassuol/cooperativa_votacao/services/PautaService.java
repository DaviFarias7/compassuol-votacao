package com.compassuol.cooperativa_votacao.services;

import com.compassuol.cooperativa_votacao.dto.PautaRequestDTO;
import com.compassuol.cooperativa_votacao.dto.ResultadoVotacaoResponseDTO;
import com.compassuol.cooperativa_votacao.exception.PautaNaoEncontradaException;
import com.compassuol.cooperativa_votacao.exception.SessaoJaAbertaException;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.repository.PautaRepository;
import com.compassuol.cooperativa_votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PautaService {
    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;

    public Pauta criarPauta(PautaRequestDTO request) {
        Pauta pauta = Pauta.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .sessaoEncerrada(false)
                .build();

        return pautaRepository.save(pauta);
    }

    public Pauta abrirSessaoVotacao(Long pautaId, Integer minutos) throws SessaoJaAbertaException {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(pautaId));

        if (pauta.getDataAbertura() != null) {
            throw new SessaoJaAbertaException(pautaId);
        }

        LocalDateTime agora = LocalDateTime.now();
        pauta.setDataAbertura(agora);
        pauta.setDataFechamento(agora.plusMinutes(minutos != null ? minutos : 1));

        return pautaRepository.save(pauta);
    }

    public Pauta buscarPautaPorId(Long pautaId) {
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(pautaId));
    }

    public ResultadoVotacaoResponseDTO obterResultadoVotacao(Long pautaId) {
        Pauta pauta = buscarPautaPorId(pautaId);
        if (pauta.getDataFechamento() == null || LocalDateTime.now().isBefore(pauta.getDataFechamento())) {
            throw new IllegalStateException("Sessão de votação ainda não foi encerrada.");
        }

        long votosSim = votoRepository.countByPautaAndVoto(pauta, true);
        long votosNao = votoRepository.countByPautaAndVoto(pauta, false);

        String resultado = votosSim > votosNao ? "APROVADA" : "REJEITADA";
        if (votosSim == votosNao) resultado = "EMPATE";

        return ResultadoVotacaoResponseDTO.builder()
                .pautaId(pauta.getId())
                .tituloPauta(pauta.getTitulo())
                .totalVotos(votosSim + votosNao)
                .votosSim(votosSim)
                .votosNao(votosNao)
                .resultado(resultado)
                .build();
    }

}

