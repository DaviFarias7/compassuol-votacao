package com.compassuol.cooperativa_votacao.controller;

import com.compassuol.cooperativa_votacao.dto.PautaRequest;
import com.compassuol.cooperativa_votacao.dto.ResultadoVotacaoResponse;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.services.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaController {
    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody PautaRequest request) {
        Pauta pauta = pautaService.criarPauta(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pauta.getId())
                .toUri();

        return ResponseEntity.created(location).body(pauta);
    }

    @PostMapping("/{id}/sessao")
    public ResponseEntity<Pauta> abrirSessao(
            @PathVariable Long id,
            @RequestParam(required = false) Integer minutos) {
        Pauta pauta = pautaService.abrirSessaoVotacao(id, minutos);
        return ResponseEntity.ok(pauta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pauta> buscarPauta(@PathVariable Long id) {
        Pauta pauta = pautaService.buscarPautaPorId(id);
        return ResponseEntity.ok(pauta);
    }

    @GetMapping("/{id}/resultado")
    public ResponseEntity<ResultadoVotacaoResponse> resultadoVotacao(@PathVariable Long id) {
        ResultadoVotacaoResponse resultado = pautaService.obterResultadoVotacao(id);
        return ResponseEntity.ok(resultado);
    }

}
