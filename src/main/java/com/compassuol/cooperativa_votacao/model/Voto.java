package com.compassuol.cooperativa_votacao.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_voto")
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false)
    @ToString.Exclude
    private Pauta pauta;

    @Column(nullable = false, length = 11)
    private String cpfAssociado;

    @Column(nullable = false)
    private boolean voto;
}
