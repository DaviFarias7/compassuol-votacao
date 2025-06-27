package com.compassuol.cooperativa_votacao.controller;

import com.compassuol.cooperativa_votacao.dto.VotoRequest;
import com.compassuol.cooperativa_votacao.model.Voto;
import com.compassuol.cooperativa_votacao.services.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
public class VotoController {
    private final VotoService votoService;

    @PostMapping
    public ResponseEntity<Voto> registrarVoto(@Valid @RequestBody VotoRequest request) {
        Voto voto = votoService.registrarVoto(request);
        return ResponseEntity.ok(voto);
    }
}
