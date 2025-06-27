package com.compassuol.cooperativa_votacao.util;

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
            String response = restTemplate.getForObject(url, String.class);
            return "ABLE_TO_VOTE".equals(response);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw e;
        }
    }
}
