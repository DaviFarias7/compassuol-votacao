-- V1__Create_tables.sql para H2
CREATE TABLE tb_associado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE tb_pauta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    data_abertura TIMESTAMP,
    data_fechamento TIMESTAMP,
    sessao_encerrada BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE tb_voto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    voto BOOLEAN NOT NULL,
    CONSTRAINT fk_voto_pauta FOREIGN KEY (pauta_id) REFERENCES tb_pauta(id),
    CONSTRAINT fk_voto_associado FOREIGN KEY (associado_id) REFERENCES tb_associado(id)
);