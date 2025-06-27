package com.compassuol.cooperativa_votacao.controller;

import com.compassuol.cooperativa_votacao.dto.VotoRequestDTO;
import com.compassuol.cooperativa_votacao.model.Voto;
import com.compassuol.cooperativa_votacao.services.VotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotoController.class)
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotoService votoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarVoto_deveRetornarOk() throws Exception {
        VotoRequestDTO request = VotoRequestDTO.builder()
                .pautaId(1L)
                .cpfAssociado("12345678901")
                .nomeAssociado("João")
                .voto(true)
                .build();

        Voto voto = Voto.builder().id(1L).build();

        Mockito.when(votoService.registrarVoto(any(VotoRequestDTO.class))).thenReturn(voto);

        mockMvc.perform(post("/api/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
