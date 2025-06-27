package com.compassuol.cooperativa_votacao.dto;

import com.compassuol.cooperativa_votacao.enums.CpfStatus;
import lombok.Data;

@Data
public class CpfValidationResponseDTO {
    private CpfStatus status;
}
