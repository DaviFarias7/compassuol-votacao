-- V1__Create_tables.sql
-- Criação da tabela de associados
CREATE TABLE tb_associado (
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL
);

-- Criação da tabela de pautas
CREATE TABLE tb_pauta (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    data_abertura TIMESTAMP,
    data_fechamento TIMESTAMP,
    sessao_encerrada BOOLEAN NOT NULL DEFAULT FALSE
);

-- Criação da tabela de votos
CREATE TABLE tb_voto (
    id SERIAL PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    voto BOOLEAN NOT NULL,
    CONSTRAINT fk_voto_pauta FOREIGN KEY (pauta_id) REFERENCES tb_pauta(id),
    CONSTRAINT fk_voto_associado FOREIGN KEY (associado_id) REFERENCES tb_associado(id)
);
