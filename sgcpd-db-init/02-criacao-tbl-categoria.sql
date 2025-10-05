-- Categoria
CREATE TABLE IF NOT EXISTS categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(80) NOT NULL,
    descricao VARCHAR(240)
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_nome_categoria'
    ) THEN
        ALTER TABLE categoria
            ADD CONSTRAINT uk_nome_categoria UNIQUE (nome);
    END IF;
END$$;

INSERT INTO categoria (nome, descricao) VALUES
('Pessoal', 'Assuntos pessoais e vida cotidiana'),
('Trabalho', 'Materiais e rotinas do trabalho'),
('Estudos', 'Conteúdos de estudo e aprendizado'),
('Projetos', 'Projetos ativos e ideias em andamento'),
('Tarefas', 'To-dos, GTD e checklists'),
('Diário', 'Registros e reflexões pessoais'),
('Saúde', 'Bem-estar, hábitos e consultas'),
('Finanças', 'Orçamentos, investimentos e gastos'),
('Família', 'Assuntos familiares e eventos'),
('Casa', 'Rotinas domésticas e manutenção'),
('Viagens', 'Planejamento de viagens e roteiros'),
('Lazer', 'Hobbies, filmes, séries, jogos'),
('Culinária', 'Receitas, cardápios e compras'),
('Leituras', 'Livros, resenhas e notas'),
('Anotações', 'Notas soltas e rascunhos'),
('Referências', 'Materiais de referência e arquivos'),
('Ideias', 'Banco de ideias e brainstorm'),
('Objetivos', 'Metas, OKRs e acompanhamento'),
('Compras', 'Listas de compras e orçamentos'),
('Eventos', 'Agenda de eventos e compromissos'),
('Documentos', 'Documentos importantes e comprovantes'),
('Tecnologia', 'Tecnologia em geral'),
('Programação', 'Desenvolvimento de software'),
('Backend', 'APIs, serviços e integrações'),
('Frontend', 'UI/UX, web e apps'),
('Mobile', 'Apps móveis, Android/iOS'),
('DevOps', 'Infra, CI/CD e observabilidade'),
('Banco de Dados', 'Modelagem, SQL e tunning'),
('Segurança', 'Segurança da informação e privacidade'),
('Cloud', 'Arquitetura e serviços em nuvem'),
('Arquitetura', 'Padrões e boas práticas'),
('Produtividade', 'Métodos, rotinas e automações'),
('Ferramentas', 'Softwares e utilitários'),
('Educação', 'Cursos, aulas e materiais'),
('Pesquisa', 'Pesquisas e experimentos'),
('Comunicação', 'E-mails, reuniões, pautas'),
('Design', 'Design visual e protótipos'),
('Marketing', 'Conteúdo, SEO e campanhas'),
('Dados', 'Data Eng, Analytics e ML'),
('Legal', 'Assuntos jurídicos e compliance'),
('Backup/Arquivo', 'Arquivamento e histórico')
ON CONFLICT (nome) DO NOTHING;
