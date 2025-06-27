package com.compassuol.cooperativa_votacao.services;

import com.compassuol.cooperativa_votacao.dto.ResultadoVotacaoResponseDTO;
import com.compassuol.cooperativa_votacao.exception.PautaNaoEncontradaException;
import com.compassuol.cooperativa_votacao.exception.SessaoJaAbertaException;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.repository.PautaRepository;
import com.compassuol.cooperativa_votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private PautaService pautaService;

    private Pauta pauta;

    @BeforeEach
    void setup() {
        pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta Teste")
                .descricao("Descrição da pauta teste")
                .sessaoEncerrada(false)
                .build();
    }

    @Test
    void abrirSessaoVotacao_quandoSessaoNaoAberta_deveAbrirSessaoComSucesso() throws SessaoJaAbertaException {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(pautaRepository.save(pauta)).thenReturn(pauta);

        Pauta resultado = pautaService.abrirSessaoVotacao(1L, 10);

        assertNotNull(resultado.getDataAbertura());
        assertNotNull(resultado.getDataFechamento());
        assertEquals(pauta.getId(), resultado.getId());
        assertFalse(resultado.isSessaoEncerrada());
    }

    @Test
    void abrirSessaoVotacao_quandoSessaoJaAberta_deveLancarExcecao() {
        pauta.setDataAbertura(LocalDateTime.now().minusMinutes(10));
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        SessaoJaAbertaException ex = assertThrows(SessaoJaAbertaException.class, () -> {
            pautaService.abrirSessaoVotacao(1L, 10);
        });

        assertEquals("Sessão de votação já está aberta para a pauta ID: 1", ex.getMessage());
    }

    @Test
    void abrirSessaoVotacao_quandoPautaNaoExiste_deveLancarExcecao() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        PautaNaoEncontradaException ex = assertThrows(PautaNaoEncontradaException.class, () -> {
            pautaService.abrirSessaoVotacao(99L, 10);
        });

        assertEquals("Pauta não encontrada com ID: 99", ex.getMessage());
    }

    @Test
    void buscarPautaPorId_quandoPautaExiste_deveRetornarPauta() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        Pauta resultado = pautaService.buscarPautaPorId(1L);

        assertNotNull(resultado);
        assertEquals(pauta.getId(), resultado.getId());
    }

    @Test
    void buscarPautaPorId_quandoPautaNaoExiste_deveLancarExcecao() {
        when(pautaRepository.findById(100L)).thenReturn(Optional.empty());

        PautaNaoEncontradaException ex = assertThrows(PautaNaoEncontradaException.class, () -> {
            pautaService.buscarPautaPorId(100L);
        });

        assertEquals("Pauta não encontrada com ID: 100", ex.getMessage());
    }

    @Test
    void obterResultadoVotacao_quandoSessaoNaoEncerrada_deveLancarExcecao() {
        pauta.setDataFechamento(null); // sessão não encerrada
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            pautaService.obterResultadoVotacao(pauta.getId());
        });

        assertEquals("Sessão de votação ainda não foi encerrada.", ex.getMessage());
    }

    @Test
    void obterResultadoVotacao_quandoEmpate_deveRetornarEmpate() {
        pauta.setDataFechamento(LocalDateTime.now().minusMinutes(1));
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        when(votoRepository.countByPautaAndVoto(pauta, true)).thenReturn(5L);
        when(votoRepository.countByPautaAndVoto(pauta, false)).thenReturn(5L);

        ResultadoVotacaoResponseDTO resultado = pautaService.obterResultadoVotacao(pauta.getId());

        assertEquals("EMPATE", resultado.getResultado());
    }

    @Test
    void obterResultadoVotacao_quandoAprovada_deveRetornarAprovada() {
        pauta.setDataFechamento(LocalDateTime.now().minusMinutes(1));
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        when(votoRepository.countByPautaAndVoto(pauta, true)).thenReturn(10L);
        when(votoRepository.countByPautaAndVoto(pauta, false)).thenReturn(5L);

        ResultadoVotacaoResponseDTO resultado = pautaService.obterResultadoVotacao(pauta.getId());

        assertEquals("APROVADA", resultado.getResultado());
    }

    @Test
    void obterResultadoVotacao_quandoRejeitada_deveRetornarRejeitada() {
        pauta.setDataFechamento(LocalDateTime.now().minusMinutes(1));
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        when(votoRepository.countByPautaAndVoto(pauta, true)).thenReturn(3L);
        when(votoRepository.countByPautaAndVoto(pauta, false)).thenReturn(7L);

        ResultadoVotacaoResponseDTO resultado = pautaService.obterResultadoVotacao(pauta.getId());

        assertEquals("REJEITADA", resultado.getResultado());
    }
}

