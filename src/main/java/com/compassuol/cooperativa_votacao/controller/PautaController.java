package com.compassuol.cooperativa_votacao.controller;

import com.compassuol.cooperativa_votacao.dto.PautaRequestDTO;
import com.compassuol.cooperativa_votacao.dto.PautaResponseDTO;
import com.compassuol.cooperativa_votacao.dto.ResultadoVotacaoResponseDTO;
import com.compassuol.cooperativa_votacao.dto.VotoResponseDTO;
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
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody PautaRequestDTO request) {
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
    public ResponseEntity<PautaResponseDTO> buscarPauta(@PathVariable Long id) {
        Pauta pauta = pautaService.buscarPautaPorId(id);

        PautaResponseDTO response = PautaResponseDTO.builder()
                .id(pauta.getId())
                .titulo(pauta.getTitulo())
                .descricao(pauta.getDescricao())
                .dataAbertura(pauta.getDataAbertura())
                .dataFechamento(pauta.getDataFechamento())
                .sessaoEncerrada(pauta.isSessaoEncerrada())
                .votos(
                        pauta.getVotos().stream().map(voto -> VotoResponseDTO.builder()
                                .cpfAssociado(voto.getAssociado().getCpf())
                                .nomeAssociado(voto.getAssociado().getNome())
                                .voto(voto.isVoto())
                                .build()
                        ).toList()
                )
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/resultado")
    public ResponseEntity<ResultadoVotacaoResponseDTO> resultadoVotacao(@PathVariable Long id) {
        ResultadoVotacaoResponseDTO resultado = pautaService.obterResultadoVotacao(id);
        return ResponseEntity.ok(resultado);
    }

}
