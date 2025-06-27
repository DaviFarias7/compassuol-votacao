package com.compassuol.cooperativa_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotoRequest {
    @NotNull(message = "ID da pauta é obrigatório")
    private Long pautaId;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpfAssociado;

    @NotNull(message = "Voto é obrigatório (true=Sim, false=Não)")
    private Boolean voto;
}
