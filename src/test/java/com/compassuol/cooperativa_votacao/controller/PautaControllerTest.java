package com.compassuol.cooperativa_votacao.controller;

import com.compassuol.cooperativa_votacao.dto.PautaRequestDTO;
import com.compassuol.cooperativa_votacao.dto.ResultadoVotacaoResponseDTO;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.services.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PautaController.class)
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService pautaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarPauta_deveRetornarCreated() throws Exception {
        Pauta pauta = Pauta.builder().id(1L).titulo("Teste").descricao("Desc").build();

        Mockito.when(pautaService.criarPauta(any(PautaRequestDTO.class))).thenReturn(pauta);

        PautaRequestDTO request = new PautaRequestDTO();
        request.setTitulo("Teste");
        request.setDescricao("Desc");

        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/pautas/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPauta_deveRetornarPautaResponseDTO() throws Exception {
        Pauta pauta = Pauta.builder()
                .id(1L)
                .titulo("Teste")
                .descricao("Descrição")
                .dataAbertura(LocalDateTime.now())
                .dataFechamento(LocalDateTime.now().plusMinutes(5))
                .sessaoEncerrada(false)
                .build();

        Mockito.when(pautaService.buscarPautaPorId(1L)).thenReturn(pauta);

        mockMvc.perform(get("/api/v1/pautas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Teste"))
                .andExpect(jsonPath("$.sessaoEncerrada").value(false));
    }

    @Test
    void abrirSessao_deveRetornarPauta() throws Exception {
        Pauta pauta = Pauta.builder().id(1L).titulo("Teste").build();
        Mockito.when(pautaService.abrirSessaoVotacao(eq(1L), eq(5))).thenReturn(pauta);

        mockMvc.perform(post("/api/v1/pautas/1/sessao")
                        .param("minutos", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void resultadoVotacao_deveRetornarResultado() throws Exception {
        ResultadoVotacaoResponseDTO resultado = ResultadoVotacaoResponseDTO.builder()
                .pautaId(1L)
                .tituloPauta("Teste")
                .totalVotos(10)
                .votosSim(6)
                .votosNao(4)
                .resultado("APROVADA")
                .build();

        Mockito.when(pautaService.obterResultadoVotacao(1L)).thenReturn(resultado);

        mockMvc.perform(get("/api/v1/pautas/1/resultado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pautaId").value(1))
                .andExpect(jsonPath("$.resultado").value("APROVADA"));
    }
}
