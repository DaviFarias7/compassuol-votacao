package com.compassuol.cooperativa_votacao.dto;

import lombok.*;

import com.compassuol.cooperativa_votacao.enums.CpfStatus;
import lombok.Data;

@Data
public class CpfValidationResponse {
    private CpfStatus status;
}
