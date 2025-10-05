CREATE TABLE IF NOT EXISTS conteudo_x_tag (
    id_conteudo BIGINT NOT NULL,
    id_tag BIGINT NOT NULL,
    CONSTRAINT fk_ct_content FOREIGN KEY (id_conteudo) REFERENCES conteudo(id) ON DELETE CASCADE,
    CONSTRAINT fk_ct_tag     FOREIGN KEY (id_tag)     REFERENCES tag(id) ON DELETE CASCADE,
    CONSTRAINT pk_conteudo_x_tag PRIMARY KEY (id_conteudo, id_tag)
);

CREATE INDEX IF NOT EXISTS idx_conteudo_x_tag_tag ON conteudo_x_tag(id_tag);