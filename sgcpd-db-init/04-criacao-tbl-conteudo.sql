-- Conteúdo
CREATE TABLE IF NOT EXISTS conteudo (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(160) NOT NULL,
    corpo TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    id_autor BIGINT NOT NULL,
    id_categoria BIGINT,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    data_atualizacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_autor_conteudo
        FOREIGN KEY (id_autor) REFERENCES usuario(id),
    CONSTRAINT fk_categoria_conteudo
        FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);


CREATE INDEX IF NOT EXISTS idx_conteudo_status ON conteudo(status);
CREATE INDEX IF NOT EXISTS idx_conteudo_id_autor ON conteudo(id_autor);
CREATE INDEX IF NOT EXISTS idx_conteudo_id_categoria ON conteudo(id_categoria);