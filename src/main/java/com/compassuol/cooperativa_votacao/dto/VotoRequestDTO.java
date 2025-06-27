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
public class VotoRequestDTO {
    @NotNull(message = "ID da pauta é obrigatório")
    private Long pautaId;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpfAssociado;

    @NotBlank(message = "Nome do associado é obrigatório")
    private String nomeAssociado;

    @NotNull(message = "Voto é obrigatório (true=Sim, false=Não)")
    private Boolean voto;
}
