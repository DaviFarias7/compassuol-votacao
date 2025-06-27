package com.compassuol.cooperativa_votacao.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PautaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private boolean sessaoEncerrada;
    private List<VotoResponseDTO> votos;
}
