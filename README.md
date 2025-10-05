# SGCPD — Sistema de Gerenciamento de Conteúdo Pessoal Dinâmico

Disciplina: Desenvolvedor Front-End

Prof. Msc. Reinaldo de Souza Júnior

Aluno: Nícolas Georgeos Mantzos

Matrícula: 2025200254

---

## Introdução

Aplicação full‑stack para gestão de conteúdos pessoais com categorias, anexos e controle de publicação, além de administração de usuários. O projeto contém:

- **Backend**: Spring Boot (Java 21), JPA/Hibernate, validação, paginação/ordenação, autenticação JWT.
- **Frontend**: Angular standalone com Bootstrap, formulários reativos, componentes de modais, tabelas responsivas e UX acessível.
- **Banco**: PostgreSQL (via Docker Compose ou instalação local).

## Sumário

- [Demonstração rápida](#demonstração-rápida)
- [Arquitetura & Tech Stack](#arquitetura--tech-stack)
- [Funcionalidades](#funcionalidades)
- [Como rodar (com Docker)](#como-rodar-com-docker)
- [Como rodar (local / dev)](#como-rodar-local--dev)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Comandos úteis](#comandos-úteis)
- [Fluxos principais](#fluxos-principais)
- [Troubleshooting](#troubleshooting)
- [Licença](#licença)

---

## Demonstração rápida

- **Login** → redireciona para `/conteudo`.
- **Aba “Conteúdos”** → busca com debounce, filtros (status/categoria), paginação/ordenação, card com tabela e ações (visualizar/editar/excluir), estado vazio, spinner de carregamento.
- **Aba “Usuários”** → listagem com busca por nome/e‑mail, filtro de status (ativo/inativo), paginação/ordenação e ações (visualizar/editar/excluir).
- **Uploads** → anexos de arquivos em “Conteúdos” com **download (Blob)** e busca do binário via **query nativa** (sem mapear `byte[]` na entidade).
- **JWT** → saudação com nome decodificado corretamente em UTF‑8 (base64url).

---

## Arquitetura & Tech Stack

**Backend**
- Java 21, Spring Boot 3, Spring Web, Spring Data JPA, Validation
- HikariCP, PostgreSQL
- Autenticação com **JWT**
- Paginação/ordenação via `Pageable`
- **Specifications** para filtrar Usuários (texto/status)

**Frontend**
- Angular (standalone), RxJS, Forms
- Bootstrap 5 + Bootstrap Icons
- Componentes reutilizáveis (modais, diálogos, estados de carregamento/vazio)
- Download seguro via `responseType: 'blob'`

**Banco**
- PostgreSQL (Docker Compose já incluso no repositório)

---

## Funcionalidades

### Conteúdos
- CRUD de conteúdos: **título**, **corpo**, **status** (`RASCUNHO`, `PUBLICADO`, `ARQUIVADO`), **categoria**
- Upload/download de **arquivos** anexos
- Listagem com:
  - Busca (debounce), filtros (status, categoria)
  - Paginação/ordenação
  - Tabela responsiva e **estado vazio** (“Nenhum conteúdo…”)
  - **Spinner** de carregamento
- Modais:
  - **Novo/Editar Conteúdo** (com gerenciamento de anexos)
  - **Visualização de Conteúdo**
  - **Confirmação de exclusão**

### Usuários
- CRUD de usuários: **nome**, **e‑mail**, **senha**, **ativo/inativo**
- Listagem com:
  - Busca por **nome/e‑mail**
  - Filtro **ativo/inativo**
  - Paginação/ordenação
- Modais:
  - **Novo/Editar Usuário**
  - **Visualização de Usuário** (nome/e‑mail somente leitura, **toggle de status**, campo de **senha não revela valor**; só envia se for alterado)

### Autenticação & UX
- Login com **JWT**
- Saudação no header com nome (decodificação UTF‑8 correta do payload)
- Navegação por **abas** (Conteúdos/Usuários) centralizadas

---

## Como rodar (com Docker)

> Requer: **Docker** e **Docker Compose**.

1) Ajuste as variáveis no `docker-compose.yml` se necessário (DB, portas, secrets).

2) Suba os serviços:
```bash
docker compose up --build -d
```

> **Nota:** na primeira subida é comum o backend iniciar antes do PostgreSQL e falhar a conexão.
> Se ocorrer erro de conexão no backend, suba novamente:
> ```bash
> docker compose up -d
> ```
> ou reinicie apenas o serviço do backend.


3) Acesse:
- **Frontend**: http://localhost:4200  
- **Backend (API)**: http://localhost:8080

> Se o frontend consumir a API em outra origem, ajuste o `environment.ts` do Angular (`apiBaseUrl`) e/ou CORS no backend.


### Scripts de inicialização do banco (`sgcpd-db-init`)

- A pasta **`sgcpd-db-init`** contém scripts **.sql** que criam o banco e as tabelas, além de populá-las com alguns dados iniciais. (**usuários, categorias e tags**)
- Os usuários inseridos possuem senha padrão `usuario123` (hash BCrypt já presente nos scripts).
- Exemplos de e-mails criados:
  - `arthur_barros@scuderiagwr.com.br`
  - `calebe_pietro_bernardes@contjulioroberto.com.br`
  - `mariane-nascimento97@bemarius.com.br`
  - `filipe_mendes@zaniniengenharia.com.br`
  - `reginasabrinafernandes@geopx.com.br`
  - `luis_silva@msn.com.br`
  - `daiane-araujo71@bessa.net.br`
  - `gustavo-pereira88@limao.com.br`
  - `andre_mateus_melo@performa.com.br`
  - `emanuel_manuel_drumond@vnews.com.br`

---

## Como rodar (local / dev)

### Pré‑requisitos
- Java 21 e Maven 3.9+
- Node 18+, npm ou pnpm
- PostgreSQL (local) ou use o do Docker Compose

### 1) Banco
Crie o database e usuário (ou use o que está no Compose):
```sql
CREATE DATABASE sgcpd;
CREATE USER sgcpd WITH ENCRYPTED PASSWORD 'sgcpd';
GRANT ALL PRIVILEGES ON DATABASE sgcpd TO sgcpd;
```

### 2) Backend
Configure as variáveis (veja seção **Variáveis de ambiente**). Em dev, você pode usar `application.yml` ou variáveis de ambiente.

Rodar:
```bash
cd sgcpd-backend
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```

API em: `http://localhost:8080`

### 3) Frontend
Configure `src/environments/environment.ts`:
```ts
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080/api'
};
```

Rodar:
```bash
cd sgcpd-frontend
npm install
npm start
# ou
npm run start
```

App em: `http://localhost:4200`

---

## Variáveis de ambiente

**Backend (exemplos):**
```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sgcpd
SPRING_DATASOURCE_USERNAME=sgcpd
SPRING_DATASOURCE_PASSWORD=sgcpd

# DDL (dev): validate | update | create | create-drop
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# JWT
JWT_SECRET=troque-este-segredo
JWT_EXPIRATION_MINUTES=120

# CORS (se necessário)
ALLOWED_ORIGINS=http://localhost:4200
```

**Frontend (`environment.ts`):**
```ts
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080/api'
};
```

---

## Comandos úteis

**Backend**
```bash
# rodar
mvn spring-boot:run

# empacotar
mvn clean package

# testes
mvn test
```

**Frontend**
```bash
# dev
npm start

# build prod
npm run build

# lint (se configurado)
npm run lint
```

---

## Fluxos principais

### Login
- `POST /api/auth/login` → retorna **JWT**
- Front armazena o token e decodifica **payload base64url UTF‑8** para exibir o nome.

### Conteúdos
- `GET /api/conteudos` → paginação/ordenação/filtros
- `POST /api/conteudos` / `PUT /api/conteudos/{id}` → criação/edição (com anexos)
- `DELETE /api/conteudos/{id}`
- **Arquivos**:
  - Upload junto com o conteúdo
  - Download com `responseType: 'blob'`
  - Backend busca `byte[]` com **query nativa** (sem mapear na entidade)

### Usuários
- `GET /api/usuarios` → `q` (nome/e‑mail), `ativo`, paginação/ordenação (Specifications)
- `POST /api/usuarios` / `PUT /api/usuarios/{id}`
- `PATCH /api/usuarios/{id}/status` (opcional)
- `PATCH /api/usuarios/{id}/senha` (só altera se informado)

> Os endpoints podem ter nomes/rotas ligeiramente diferentes no seu código; adapte aqui se necessário.

---

## Troubleshooting

- **Caracteres acentuados quebrados no header**: use **decodificação UTF‑8** de **base64url** do payload do JWT (não use `JSON.parse(atob(...))` puro).
- **CORS**: ajuste `ALLOWED_ORIGINS` no backend ou configure o CORS global.
- **Paginação fazendo múltiplas chamadas** ao “Limpar filtros”: use `reset({ … }, { emitEvent: false })` e chame manualmente o carregamento (debounce/merge podem ajudar).
- **Download de arquivo corrompido**: no Angular, use `responseType: 'blob'`; no Spring, retorne `ResponseEntity<byte[]>` com `Content-Type` apropriado.

---

## Licença

Licenciado sob a **MIT License**.

```
MIT License

Copyright (c) 2025 Nícolas Mantzos

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
