package com.compassuol.cooperativa_votacao.services;

import com.compassuol.cooperativa_votacao.dto.VotoRequestDTO;
import com.compassuol.cooperativa_votacao.model.Associado;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.model.Voto;
import com.compassuol.cooperativa_votacao.repository.AssociadoRepository;
import com.compassuol.cooperativa_votacao.repository.VotoRepository;
import com.compassuol.cooperativa_votacao.util.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaService pautaService;

    @Mock
    private CpfValidator cpfValidator;

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private VotoService votoService;

    private Pauta pauta;
    private Associado associado;

    @BeforeEach
    void setup() {
        pauta = Pauta.builder()
                .id(1L)
                .dataAbertura(LocalDateTime.now().minusMinutes(5))
                .dataFechamento(LocalDateTime.now().plusMinutes(5))
                .sessaoEncerrada(false)
                .build();

        associado = Associado.builder()
                .id(1L)
                .cpf("12345678901")
                .nome("João")
                .build();
    }

    @Test
    void registrarVoto_comSucesso() {
        VotoRequestDTO request = VotoRequestDTO.builder()
                .pautaId(pauta.getId())
                .cpfAssociado(associado.getCpf())
                .nomeAssociado(associado.getNome())
                .voto(true)
                .build();

        when(pautaService.buscarPautaPorId(pauta.getId())).thenReturn(pauta);
        when(cpfValidator.isValid(associado.getCpf())).thenReturn(true);
        when(associadoRepository.findByCpf(associado.getCpf())).thenReturn(Optional.of(associado));
        when(votoRepository.findByPautaAndAssociado(pauta, associado)).thenReturn(Optional.empty());
        when(votoRepository.save(any(Voto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Voto voto = votoService.registrarVoto(request);

        assertNotNull(voto);
        assertEquals(pauta, voto.getPauta());
        assertEquals(associado, voto.getAssociado());
        assertTrue(voto.isVoto());

        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void registrarVoto_sessaoNaoIniciada_throwsException() {
        pauta.setDataAbertura(null);

        when(pautaService.buscarPautaPorId(pauta.getId())).thenReturn(pauta);

        VotoRequestDTO request = VotoRequestDTO.builder()
                .pautaId(pauta.getId())
                .cpfAssociado(associado.getCpf())
                .nomeAssociado(associado.getNome())
                .voto(true)
                .build();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            votoService.registrarVoto(request);
        });

        assertEquals("Sessão da pauta com id 1 não iniciada", exception.getMessage());
    }

    @Test
    void registrarVoto_sessaoEncerrada_throwsException() {
        pauta.setDataFechamento(LocalDateTime.now().minusMinutes(1));

        when(pautaService.buscarPautaPorId(pauta.getId())).thenReturn(pauta);

        VotoRequestDTO request = VotoRequestDTO.builder()
                .pautaId(pauta.getId())
                .cpfAssociado(associado.getCpf())
                .nomeAssociado(associado.getNome())
                .voto(true)
                .build();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            votoService.registrarVoto(request);
        });

        assertTrue(exception.getMessage().contains("está encerrada"));
    }

    @Test
    void registrarVoto_cpfInvalido_throwsException() {
        when(pautaService.buscarPautaPorId(pauta.getId())).thenReturn(pauta);
        when(cpfValidator.isValid(associado.getCpf())).thenReturn(false);

        VotoRequestDTO request = VotoRequestDTO.builder()
                .pautaId(pauta.getId())
                .cpfAssociado(associado.getCpf())
                .nomeAssociado(associado.getNome())
                .voto(true)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoService.registrarVoto(request);
        });

        assertEquals("CPF inválido: " + associado.getCpf(), exception.getMessage());
    }

    @Test
    void registrarVoto_jaVotou_throwsException() {
        when(pautaService.buscarPautaPorId(pauta.getId())).thenReturn(pauta);
        when(cpfValidator.isValid(associado.getCpf())).thenReturn(true);
        when(associadoRepository.findByCpf(associado.getCpf())).thenReturn(Optional.of(associado));
        when(votoRepository.findByPautaAndAssociado(pauta, associado)).thenReturn(Optional.of(new Voto()));

        VotoRequestDTO request = VotoRequestDTO.builder()
                .pautaId(pauta.getId())
                .cpfAssociado(associado.getCpf())
                .nomeAssociado(associado.getNome())
                .voto(true)
                .build();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            votoService.registrarVoto(request);
        });

        assertTrue(exception.getMessage().contains("já votou na pauta"));
    }
}
