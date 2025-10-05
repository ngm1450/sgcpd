
-- Tag (entidade ConteudoTag usa esta tabela)
CREATE TABLE tag (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(60) NOT NULL UNIQUE,
    id_categoria BIGINT NOT NULL,
    CONSTRAINT fk_tag_categoria FOREIGN KEY (id_categoria)
        REFERENCES categoria(id)
        ON DELETE CASCADE
);


-- Corrige o erro: coluna é 'nome', não 'name'
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_nome_tag'
    ) THEN
        ALTER TABLE tag
            ADD CONSTRAINT uk_nome_tag UNIQUE (nome);
    END IF;
END$$;

-- Bloco 1 - Pessoal, Saúde, Finanças, Casa, Lazer, Leituras, Produtividade
INSERT INTO tag (nome, id_categoria) VALUES
('anotacao', (SELECT id FROM categoria WHERE nome='Pessoal')),
('rascunho', (SELECT id FROM categoria WHERE nome='Pessoal')),
('planejamento', (SELECT id FROM categoria WHERE nome='Pessoal')),
('prioridade-alta', (SELECT id FROM categoria WHERE nome='Tarefas')),
('prioridade-media', (SELECT id FROM categoria WHERE nome='Tarefas')),
('prioridade-baixa', (SELECT id FROM categoria WHERE nome='Tarefas')),
('okrs', (SELECT id FROM categoria WHERE nome='Objetivos')),
('metas', (SELECT id FROM categoria WHERE nome='Objetivos')),
('habitos', (SELECT id FROM categoria WHERE nome='Saúde')),
('saude', (SELECT id FROM categoria WHERE nome='Saúde')),
('treino', (SELECT id FROM categoria WHERE nome='Saúde')),
('corrida', (SELECT id FROM categoria WHERE nome='Saúde')),
('ciclismo', (SELECT id FROM categoria WHERE nome='Saúde')),
('yoga', (SELECT id FROM categoria WHERE nome='Saúde')),
('nutricao', (SELECT id FROM categoria WHERE nome='Saúde')),
('financas', (SELECT id FROM categoria WHERE nome='Finanças')),
('orcamento', (SELECT id FROM categoria WHERE nome='Finanças')),
('investimentos', (SELECT id FROM categoria WHERE nome='Finanças')),
('renda-fixa', (SELECT id FROM categoria WHERE nome='Finanças')),
('acoes', (SELECT id FROM categoria WHERE nome='Finanças')),
('fundos-imobiliarios', (SELECT id FROM categoria WHERE nome='Finanças')),
('criptomoedas', (SELECT id FROM categoria WHERE nome='Finanças')),
('impostos', (SELECT id FROM categoria WHERE nome='Finanças')),
('contas', (SELECT id FROM categoria WHERE nome='Finanças')),
('compras', (SELECT id FROM categoria WHERE nome='Compras')),
('listas', (SELECT id FROM categoria WHERE nome='Compras')),
('mercado', (SELECT id FROM categoria WHERE nome='Compras')),
('receitas', (SELECT id FROM categoria WHERE nome='Culinária')),
('cardapio', (SELECT id FROM categoria WHERE nome='Culinária')),
('familia', (SELECT id FROM categoria WHERE nome='Família')),
('filhos', (SELECT id FROM categoria WHERE nome='Família')),
('pets', (SELECT id FROM categoria WHERE nome='Família')),
('casa', (SELECT id FROM categoria WHERE nome='Casa')),
('manutencao', (SELECT id FROM categoria WHERE nome='Casa')),
('reforma', (SELECT id FROM categoria WHERE nome='Casa')),
('mudanca', (SELECT id FROM categoria WHERE nome='Casa')),
('viagem', (SELECT id FROM categoria WHERE nome='Viagens')),
('roteiro', (SELECT id FROM categoria WHERE nome='Viagens')),
('hospedagem', (SELECT id FROM categoria WHERE nome='Viagens')),
('passagens', (SELECT id FROM categoria WHERE nome='Viagens')),
('documentos', (SELECT id FROM categoria WHERE nome='Documentos')),
('checklist', (SELECT id FROM categoria WHERE nome='Tarefas')),
('lazer', (SELECT id FROM categoria WHERE nome='Lazer')),
('filmes', (SELECT id FROM categoria WHERE nome='Lazer')),
('series', (SELECT id FROM categoria WHERE nome='Lazer')),
('jogos', (SELECT id FROM categoria WHERE nome='Lazer')),
('leituras', (SELECT id FROM categoria WHERE nome='Leituras')),
('resenha', (SELECT id FROM categoria WHERE nome='Leituras')),
('livros', (SELECT id FROM categoria WHERE nome='Leituras')),
('biblioteca', (SELECT id FROM categoria WHERE nome='Leituras')),
('produtividade', (SELECT id FROM categoria WHERE nome='Produtividade')),
('gtd', (SELECT id FROM categoria WHERE nome='Produtividade')),
('pomodoro', (SELECT id FROM categoria WHERE nome='Produtividade')),
('automatizacao', (SELECT id FROM categoria WHERE nome='Produtividade'))
ON CONFLICT (nome) DO NOTHING;


-- Bloco 2 – Técnico Geral
INSERT INTO tag (nome, id_categoria) VALUES
('tecnologia', (SELECT id FROM categoria WHERE nome='Tecnologia')),
('programacao', (SELECT id FROM categoria WHERE nome='Programação')),
('algoritmos', (SELECT id FROM categoria WHERE nome='Programação')),
('estrutura-de-dados', (SELECT id FROM categoria WHERE nome='Programação')),
('poo', (SELECT id FROM categoria WHERE nome='Programação')),
('solid', (SELECT id FROM categoria WHERE nome='Programação')),
('arquitetura', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('clean-architecture', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('ddd', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('tdd', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('cqrs', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('event-sourcing', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('documentacao', (SELECT id FROM categoria WHERE nome='Documentos')),
('api', (SELECT id FROM categoria WHERE nome='Backend')),
('rest', (SELECT id FROM categoria WHERE nome='Backend')),
('graphql', (SELECT id FROM categoria WHERE nome='Backend')),
('websocket', (SELECT id FROM categoria WHERE nome='Backend')),
('mensageria', (SELECT id FROM categoria WHERE nome='Backend')),
('microservicos', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('monolito', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('escalabilidade', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('disponibilidade', (SELECT id FROM categoria WHERE nome='Arquitetura')),
('observabilidade', (SELECT id FROM categoria WHERE nome='DevOps')),
('monitoracao', (SELECT id FROM categoria WHERE nome='DevOps')),
('logging', (SELECT id FROM categoria WHERE nome='DevOps')),
('tracing', (SELECT id FROM categoria WHERE nome='DevOps')),
('metrics', (SELECT id FROM categoria WHERE nome='DevOps')),
('slo', (SELECT id FROM categoria WHERE nome='DevOps')),
('sla', (SELECT id FROM categoria WHERE nome='DevOps')),
('sli', (SELECT id FROM categoria WHERE nome='DevOps')),
('seguranca', (SELECT id FROM categoria WHERE nome='Segurança')),
('oauth2', (SELECT id FROM categoria WHERE nome='Segurança')),
('oidc', (SELECT id FROM categoria WHERE nome='Segurança')),
('jwt', (SELECT id FROM categoria WHERE nome='Segurança')),
('rbac', (SELECT id FROM categoria WHERE nome='Segurança')),
('abac', (SELECT id FROM categoria WHERE nome='Segurança')),
('rate-limit', (SELECT id FROM categoria WHERE nome='Segurança')),
('teste', (SELECT id FROM categoria WHERE nome='Projetos')),
('unitario', (SELECT id FROM categoria WHERE nome='Projetos')),
('integracao', (SELECT id FROM categoria WHERE nome='Projetos')),
('contrato', (SELECT id FROM categoria WHERE nome='Projetos')),
('e2e', (SELECT id FROM categoria WHERE nome='Projetos')),
('testcontainers', (SELECT id FROM categoria WHERE nome='Projetos')),
('performance', (SELECT id FROM categoria WHERE nome='Projetos')),
('carga', (SELECT id FROM categoria WHERE nome='Projetos')),
('stress', (SELECT id FROM categoria WHERE nome='Projetos')),
('profiling', (SELECT id FROM categoria WHERE nome='Projetos')),
('benchmark', (SELECT id FROM categoria WHERE nome='Projetos'))
ON CONFLICT (nome) DO NOTHING;