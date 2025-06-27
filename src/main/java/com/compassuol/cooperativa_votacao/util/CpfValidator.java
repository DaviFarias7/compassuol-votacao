package com.compassuol.cooperativa_votacao.util;

import com.compassuol.cooperativa_votacao.dto.CpfValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CpfValidator {
    @Value("${cpf.validator.url}")
    private String cpfValidatorUrl;

    private final RestTemplate restTemplate;

    public CpfValidator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isValid(String cpf) {
        try {
            String url = cpfValidatorUrl + cpf;
            CpfValidationResponse response = restTemplate.getForObject(url, CpfValidationResponse.class);

            if (response == null || response.getStatus() == null) {
                return false;
            }

            switch (response.getStatus()) {
                case ABLE_TO_VOTE:
                    return true;
                case UNABLE_TO_VOTE:
                    return false;
                default:
                    throw new IllegalStateException("Status inesperado: " + response.getStatus());
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw e;
        }
    }
}
