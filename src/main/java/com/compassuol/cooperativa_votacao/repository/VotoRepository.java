package com.compassuol.cooperativa_votacao.repository;

import com.compassuol.cooperativa_votacao.model.Pauta;
import com.compassuol.cooperativa_votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findByPautaAndCpfAssociado(Pauta pauta, String cpfAssociado);
    long countByPautaAndVoto(Pauta pauta, boolean voto);
}
