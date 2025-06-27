package com.compassuol.cooperativa_votacao.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoVotacaoResponseDTO {
    private Long pautaId;
    private String tituloPauta;
    private long totalVotos;
    private long votosSim;
    private long votosNao;
    private String resultado;
}
