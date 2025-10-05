CREATE TABLE arquivo (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(80) NOT NULL,
    tamanho BIGINT NOT NULL,
    dados BYTEA NOT NULL,
    id_conteudo BIGINT,
    CONSTRAINT uk_nome_arquivo UNIQUE (nome),
    CONSTRAINT fk_arquivo_conteudo
    FOREIGN KEY (id_conteudo)
        REFERENCES conteudo (id)
        ON DELETE CASCADE
);
