package com.compassuol.cooperativa_votacao.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotoResponseDTO {
    private String cpfAssociado;
    private String nomeAssociado;
    private boolean voto;
}
