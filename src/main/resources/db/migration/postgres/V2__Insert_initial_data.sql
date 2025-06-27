-- V2__Insert_initial_data.sql para PostgreSQL
INSERT INTO tb_associado (cpf, nome) VALUES ('12345678901', 'Fulano de Tal');

INSERT INTO tb_pauta (titulo, descricao, data_abertura, data_fechamento, sessao_encerrada)
VALUES (
    'Pauta Exemplo',
    'Descrição da pauta de teste',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '5 MINUTE',
    false
);