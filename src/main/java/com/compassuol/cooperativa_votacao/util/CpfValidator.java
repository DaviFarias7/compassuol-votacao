package com.compassuol.cooperativa_votacao.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
        // Validação desativada temporariamente para desenvolvimento
        return true;

        /*
        try {
            String url = cpfValidatorUrl + cpf;
            CpfValidationResponseDTO response = restTemplate.getForObject(url, CpfValidationResponseDTO.class);

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
        */
    }
}
