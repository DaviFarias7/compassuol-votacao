package com.compassuol.cooperativa_votacao.services;
import com.compassuol.cooperativa_votacao.dto.VotoRequestDTO;
import com.compassuol.cooperativa_votacao.model.Associado;
import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.model.Voto;
import com.compassuol.cooperativa_votacao.repository.AssociadoRepository;
import com.compassuol.cooperativa_votacao.repository.VotoRepository;
import com.compassuol.cooperativa_votacao.util.CpfValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotoService {
    private final VotoRepository votoRepository;
    private final PautaService pautaService;
    private final CpfValidator cpfValidator;
    private final AssociadoRepository associadoRepository;


    public Voto registrarVoto(VotoRequestDTO request) {
        Pauta pauta = pautaService.buscarPautaPorId(request.getPautaId());
        validarSessaoVotacao(pauta);
        validarCpf(request.getCpfAssociado());

        Associado associado = buscarOuCriarAssociado(
                request.getCpfAssociado(),
                request.getNomeAssociado()
        );
        validarVotoUnico(pauta, associado);

        Voto voto = Voto.builder()
                .pauta(pauta)
                .associado(associado)
                .voto(request.getVoto())
                .build();

        return votoRepository.save(voto);
    }


    private void validarSessaoVotacao(Pauta pauta) {
        if (pauta.getDataAbertura() == null) {
            throw new IllegalStateException("Sessão da pauta com id " + pauta.getId() + " não iniciada");
        }
        if (LocalDateTime.now().isAfter(pauta.getDataFechamento())) {
            throw new IllegalStateException("Sessão da pauta com id " + pauta.getId() + " está encerrada");
        }
    }

    private void validarCpf(String cpf) {
        if (!cpfValidator.isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
    }

    private void validarVotoUnico(Pauta pauta, Associado associado) {
        if (votoRepository.findByPautaAndAssociado(pauta, associado).isPresent()) {
            throw new IllegalStateException("Associado com CPF " + associado.getCpf() +
                    " já votou na pauta " + pauta.getId());
        }
    }

    private Associado buscarOuCriarAssociado(String cpf, String nome) {
        return associadoRepository.findByCpf(cpf)
                .orElseGet(() -> associadoRepository.save(
                        Associado.builder().cpf(cpf).nome(nome).build()));
    }

}

