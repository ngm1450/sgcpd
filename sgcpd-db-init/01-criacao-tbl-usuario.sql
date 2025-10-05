CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(60) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    data_atualizacao TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_email_usuario'
    ) THEN
        ALTER TABLE usuario
            ADD CONSTRAINT uk_email_usuario UNIQUE (email);
    END IF;
END$$;

INSERT INTO usuario (nome, email, senha, ativo, data_criacao, data_atualizacao)
VALUES
('Arthur Luís Enrico Barros', 'arthur_barros@scuderiagwr.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Calebe Pietro Bernardes', 'calebe_pietro_bernardes@contjulioroberto.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Mariane Elza Heloisa Nascimento', 'mariane-nascimento97@bemarius.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Filipe Bernardo Mendes', 'filipe_mendes@zaniniengenharia.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Regina Sabrina Melissa Fernandes', 'reginasabrinafernandes@geopx.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Luís Filipe Silva', 'luis_silva@msn.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Daiane Débora Araújo', 'daiane-araujo71@bessa.net.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Gustavo Augusto Vinicius Pereira', 'gustavo-pereira88@limao.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('André Mateus José Melo', 'andre_mateus_melo@performa.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now()),
('Emanuel Manuel Drumond', 'emanuel_manuel_drumond@vnews.com.br', '$2a$10$qzwJr6VJI4ocbvWfSoaw4.9SgafMQBpCGBSDAhOGQmFr0aDw998qC', true, now(), now());