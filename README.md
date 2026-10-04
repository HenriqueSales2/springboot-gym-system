# Spring Boot Gym System

[![CI/CD with GitHub Actions](https://github.com/HenriqueSales2/springboot-gym-system/actions/workflows/continuos-deployment.yml/badge.svg)](https://github.com/HenriqueSales2/springboot-gym-system/actions/workflows/continuos-deployment.yml)

API REST para gerenciamento de **pessoas**, **exercícios de academia**, **arquivos**, **relatórios** e **e-mails**, protegida com **Spring Security (JWT)** e com front-end para uso no navegador.

**Stack:** Java 21 · Spring Boot · Spring Security · Spring Data JPA · H2 / MySQL · Flyway · Scalar/OpenAPI · JasperReports · JUnit 5 · Mockito · Testcontainers · Docker · GitHub Actions · Node.js (front-end)

## Índice

- [Quick Start](#quick-start)
- [Duas formas de testar](#duas-formas-de-testar)
- [Capturas de tela do front-end](#capturas-de-tela-do-front-end)
- [Autenticação (Spring Security)](#autenticação-spring-security)
- [Funcionalidades](#funcionalidades)
- [Endpoints](#endpoints)
- [Postman](#postman-alternativa)
- [Capturas de tela da API](#capturas-de-tela-da-api)
- [Outras formas de executar](#outras-formas-de-executar)
- [Arquitetura](#arquitetura)
- [Testes](#testes)
- [Autor](#autor)

---

## Quick Start

O projeto usa o banco **H2 (em memória) como padrão**, então você não precisa instalar nem configurar banco de dados.

**Pré-requisitos:** Java 21, Maven e Node.js.

### 1. Subir a API (back-end)

```bash
git clone https://github.com/HenriqueSales2/springboot-gym-system.git
cd springboot-gym-system/gym-system
mvn spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

### 2. Subir o front-end (em outro terminal)

```bash
cd springboot-gym-system/gym-system/client
npm install
npm run dev
```

O front-end fica disponível em `http://localhost:3000`.

### 3. Fazer login e testar

Use o usuário de teste **`john` / `admin123`** (veja [Autenticação](#autenticação-spring-security)) e escolha como quer testar:

> Como o H2 roda em memória, os dados são zerados toda vez que a aplicação reinicia.

---

## Duas formas de testar

| | Opção A: **Scalar** (API) | Opção B: **Front-end** (interface) |
|---|---|---|
| **Para quem?** | Quem quer ver os endpoints, contratos e respostas | Quem quer usar o sistema como um usuário final |
| **Link** | http://localhost:8080/scalar | http://localhost:3000 |
| **O que dá pra fazer** | Executar qualquer endpoint direto da documentação, como exportar relatórios e subir arquivos, além de ver schemas e exemplos de body | Cadastrar, editar e excluir exercícios |

### Opção A: Scalar ou Swagger (documentação interativa da API)

1. Abra **http://localhost:8080/scalar** (ou, se preferir o Swagger, **http://localhost:8080/swagger-ui/index.html**).
2. No menu lateral, abra **Authentication** e escolha **Authenticates an user and returns a token**.
3. Envie este body:
   ```json
   {
     "username": "john",
     "password": "admin123"
   }
   ```
4. Copie o valor do campo **accessToken** da resposta.
5. Informe o token na ferramenta:
   - **Scalar:** no canto superior direito, em **bearerAuth**, clique na caixinha e cole o token.
   - **Swagger:** clique no cadeado (**Authorize**), cole o token e confirme.
6. No menu lateral, escolha um grupo: **Person**, **Workout**, **File** ou **Email**.
7. Clique em **Test Request** (Scalar) ou **Try it out** (Swagger) no endpoint desejado.

Roteiro rápido para testar:

1. `POST /api/person/v1`: cria uma pessoa
2. `GET /api/person/v1`: lista paginada
3. `GET /api/person/v1/exportPage` com `Accept: application/pdf`: baixa o relatório

> A especificação OpenAPI crua também fica disponível em `/v3/api-docs`.

### Opção B: Front-end

1. Com a API e o front-end rodando, abra **http://localhost:3000**.
2. Faça login com `john` / `admin123`.
3. Navegue pelas telas de **Exercícios**.
4. Experimente ouvir uma música enquanto cadastra, edita ou exclui um treino.

---

## Capturas de tela do front-end

<details>
<summary>Ver prints das telas</summary>

**Tela de login**

<img width="700" alt="Tela de login" src="gym-system/client/src/assets/screenshots/front-end/login.png" />

**Tela de treinos**

<img width="700" alt="Lista de treinos" src="gym-system/client/src/assets/screenshots/front-end/workouts.png" />

**Tela de cadastro e edição de treinos**

<img width="700" alt="Cadastro de treino" src="gym-system/client/src/assets/screenshots/front-end/addWorkouts.png" />
<img width="700" alt="Edição de treino" src="gym-system/client/src/assets/screenshots/front-end/editWorkouts.png" />

</details>

---

## Autenticação (Spring Security)

Os endpoints são protegidos com **Spring Security** e **JWT**. Antes de testar, faça login para obter um token e envie esse token nas requisições.

### Usuário de teste (admin padrão)

| Campo | Valor |
|---|---|
| **username** | `john` |
| **password** | `admin123` |

> Credenciais criadas apenas para demonstração e testes locais. Troque ou remova este usuário em qualquer ambiente real.

### 1. Fazer login

```http
POST /auth/signin
Content-Type: application/json
```

```json
{
   "username": "john",
   "password": "admin123"
}
```

A resposta traz o token de acesso:

```json
{
   "username": "john",
   "authenticated": true,
   "created": "2026-10-04T14:52:51.610+00:00",
   "expiration": "2026-10-04T15:52:51.610+00:00",
   "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
   "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Usar o token

Envie o `accessToken` no header de todas as outras requisições:

```http
Authorization: Bearer <accessToken>
```

> O token expira após **1 hora**. Se receber **401/403**, faça login novamente ou renove o token com o endpoint de refresh (veja os prints em [Capturas de tela da API](#capturas-de-tela-da-api)).

### Como fazer em cada ferramenta

- **Scalar / Swagger:** siga o passo a passo da [Opção A](#opção-a-scalar-ou-swagger-documentação-interativa-da-api).
- **Front-end:** na tela de login, informe `john` / `admin123`.
- **Postman:** na aba **Authorization** da requisição ou da collection, escolha **Bearer Token** e cole o token.

---

## Funcionalidades

| Módulo | O que faz |
|---|---|
| **Autenticação** | Login com JWT, criação de usuário e renovação de token |
| **Pessoas** | CRUD completo, busca por ID e por nome, listagem paginada, atualização parcial (PATCH), exportação individual em PDF, exportação de página em PDF/CSV/XLSX, importação em massa via CSV/XLSX |
| **Exercícios** | CRUD completo, busca por ID e listagem paginada |
| **Arquivos** | Upload único, upload múltiplo e download |
| **E-mails** | Envio de e-mail simples e com anexo (Gmail SMTP) |

---

## Endpoints

### Autenticação — `/auth`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/signin` | Autentica o usuário e retorna o token |

Também existem os endpoints de criação de usuário e de renovação de token (veja os prints e o Scalar).

### Pessoas — `/api/person/v1`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/` | Lista paginada (`page=0`, `size=12`, `direction=asc`) |
| GET | `/{id}` | Busca por ID |
| GET | `/findPeopleByName/{firstName}` | Busca por nome |
| POST | `/` | Cria pessoa |
| PUT | `/` | Atualiza pessoa |
| PATCH | `/{id}` | Atualização parcial |
| DELETE | `/{id}` | Exclui pessoa |
| GET | `/exportPerson/{id}` | Exporta uma pessoa (`Accept: application/pdf`) |
| GET | `/exportPage` | Exporta a página atual (`application/pdf`, `text/csv` ou `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`) |

### Exercícios — `/api/workout/v1`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/` | Lista exercícios |
| GET | `/{id}` | Busca por ID |
| POST | `/` | Cria exercício |
| PUT | `/` | Atualiza exercício |
| DELETE | `/{id}` | Exclui exercício |

### Arquivos — `/api/file/v1`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/uploadFile` | Upload de um arquivo (`file`) |
| POST | `/uploadMultipleFiles` | Upload múltiplo (`files`) |
| GET | `/downloadFile/{fileName}` | Download |

### E-mails — `/api/email/v1`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/` | E-mail simples (`to`, `subject`, `message`) |
| POST | `/withAttachment` | E-mail com anexo (form-data: `emailRequest` + `attachment`) |

> Os endpoints de e-mail são opcionais para testar o resto do sistema. Para usá-los, configure as variáveis de ambiente `EMAIL_USERNAME` e `EMAIL_PASSWORD` (veja [Outras formas de executar](#outras-formas-de-executar)); sem elas, esses endpoints vão falhar.

<details>
<summary>Exemplos de body</summary>

**Pessoa (JSON)**
```json
{
   "firstName": "Mary",
   "lastName": "Doe",
   "address": "New York - USA",
   "gender": "Female",
   "enabled": true
}
```

**Exercício**
```json
{
   "exerciseName": "Bench Press",
   "muscleGroup": "Chest",
   "equipment": "Barbell",
   "difficulty": "Intermediate"
}
```

**E-mail**
```json
{
   "to": "destinatario@email.com",
   "subject": "Teste",
   "message": "Mensagem enviada pela API"
}
```
</details>

---

## Postman (alternativa)

Importe os dois arquivos da pasta `Collections/` no Postman para testar todos os endpoints:

- `Gym Training API.postman_collection.json`
- `Spring_Boot_Application.postman_environment.json`

---

## Capturas de tela da API

<details>
<summary>Ver prints dos endpoints</summary>

**Autenticação**

<img width="700" alt="Login" src="gym-system/client/src/assets/screenshots/back-end/authentication/signin.png" />
<img width="700" alt="Criar usuário" src="gym-system/client/src/assets/screenshots/back-end/authentication/createUser.png" />
<img width="700" alt="Atualizar token" src="gym-system/client/src/assets/screenshots/back-end/authentication/refreshToken.png" />

**Pessoas**

<img width="700" alt="Listar pessoas" src="gym-system/client/src/assets/screenshots/back-end/person/findAll.png" />
<img width="700" alt="Buscar por ID" src="gym-system/client/src/assets/screenshots/back-end/person/findById.png" />
<img width="700" alt="Buscar por nome" src="gym-system/client/src/assets/screenshots/back-end/person/findByFirstname.png" />
<img width="700" alt="Criar pessoa" src="gym-system/client/src/assets/screenshots/back-end/person/create.png" />
<img width="700" alt="Atualizar pessoa" src="gym-system/client/src/assets/screenshots/back-end/person/update.png" />
<img width="700" alt="Atualização parcial (desabilitar pessoa)" src="gym-system/client/src/assets/screenshots/back-end/person/disablePerson.png" />
<img width="700" alt="Excluir pessoa" src="gym-system/client/src/assets/screenshots/back-end/person/delete.png" />

**Relatórios**

<img width="700" alt="Exportar página PDF" src="gym-system/client/src/assets/screenshots/back-end/person/exportPersonPDF.png" />
<img width="700" alt="CSV no Excel" src="gym-system/client/src/assets/screenshots/back-end/person/exportPageCSV.png" />
<img width="700" alt="XLSX no Excel" src="gym-system/client/src/assets/screenshots/back-end/person/exportPageXLSX.png" />

**Exercícios**

<img width="700" alt="Listar exercícios" src="gym-system/client/src/assets/screenshots/back-end/workout/findAll.png" />
<img width="700" alt="Exercício por ID" src="gym-system/client/src/assets/screenshots/back-end/workout/findById.png" />
<img width="700" alt="Criar exercício" src="gym-system/client/src/assets/screenshots/back-end/workout/create.png" />
<img width="700" alt="Atualizar exercício" src="gym-system/client/src/assets/screenshots/back-end/workout/update.png" />
<img width="700" alt="Excluir exercício" src="gym-system/client/src/assets/screenshots/back-end/workout/delete.png" />

**Arquivos**

<img width="700" alt="Upload" src="gym-system/client/src/assets/screenshots/back-end/file/uploadFile.png" />
<img width="700" alt="Upload múltiplo" src="gym-system/client/src/assets/screenshots/back-end/file/uploadMultipleFiles.png" />
<img width="700" alt="Download" src="gym-system/client/src/assets/screenshots/back-end/file/downloadFile.png" />

**E-mails**

<img width="700" alt="E-mail" src="gym-system/client/src/assets/screenshots/back-end/email/sendEmail.png" />
<img width="700" alt="E-mail no Gmail" src="gym-system/client/src/assets/screenshots/back-end/email/sendEmailGmail.png" />
<img width="700" alt="E-mail com anexo" src="gym-system/client/src/assets/screenshots/back-end/email/sendEmailWithAttachment.png" />
<img width="700" alt="E-mail com anexo no Gmail" src="gym-system/client/src/assets/screenshots/back-end/email/sendEmailWithAttachmentGmail.png" />

</details>

---

## Outras formas de executar

### Configurar o envio de e-mail (opcional)

Use uma **Senha de Aplicativo** do Google (não a sua senha normal) e defina as variáveis de ambiente antes de subir a aplicação:

```bash
# Linux/macOS
export EMAIL_USERNAME=seuemail@gmail.com
export EMAIL_PASSWORD=sua_senha_de_aplicativo
```

```cmd
:: Windows (abra um novo terminal depois de executar)
setx EMAIL_USERNAME "seuemail@gmail.com"
setx EMAIL_PASSWORD "senha_de_aplicativo_google"
```

### Diretório de upload

Ajuste o caminho no `application.yml` (`gym-system/src/main/resources`):

```yaml
file:
   upload-dir: C:/caminho/do/projeto/UploadDir
```

### Usando MySQL

**Manualmente:** crie o banco e ajuste o `application.yml`:

```sql
CREATE DATABASE rest_with_spring_boot_java;
```

```yaml
spring:
   datasource:
      url: jdbc:mysql://localhost:3306/rest_with_spring_boot_java
      username: seu_usuario
      password: sua_senha
```

**Com Docker Compose** (sobe o MySQL e a aplicação em containers):

```bash
cp .env-example .env        # Windows (cmd): copy .env-example .env
docker compose up -d --build
```

Exemplo de `.env`:

```env
MYSQL_ROOT_PASSWORD=root123
MYSQL_DATABASE=rest_with_spring_boot_java
MYSQL_USER=gym
MYSQL_PASSWORD=gym123
MYSQL_URL=jdbc:mysql://db:3306/rest_with_spring_boot_java
EMAIL_USERNAME=seuemail@gmail.com
EMAIL_PASSWORD=senha_de_aplicativo_google
```

| Serviço | URL |
|---|---|
| API / Scalar | http://localhost:8080/scalar |
| MySQL (acesso externo) | `localhost:3310` |
| Portainer (gestão dos containers) | http://localhost:9000 |

O front-end continua rodando separadamente com Node.js (veja o [Quick Start](#quick-start)).

Para derrubar tudo: `docker compose down` (use `-v` para apagar também os volumes).

> As migrations do **Flyway** criam as tabelas automaticamente na primeira execução.

---

## Arquitetura

```text
springboot-gym-system
├── Collections/                 # Collection e environment do Postman
├── docker-compose.yml
├── .env-example
└── gym-system
    ├── client/                  # Front-end (Node.js)
    ├── pom.xml
    └── src
        ├── main
        │   ├── java/br/com/application
        │   │   ├── config/          # Configurações da aplicação
        │   │   ├── controller/      # Endpoints REST + documentação OpenAPI
        │   │   ├── data/dto|vo/     # DTOs e Value Objects
        │   │   ├── exception/       # Tratamento global de exceções
        │   │   ├── file/            # Exportadores e importadores (PDF, CSV, XLSX)
        │   │   ├── mail/            # Envio de e-mails
        │   │   ├── mapper/          # Entidade <-> DTO
        │   │   ├── model/           # Entidades JPA
        │   │   ├── repository/      # Acesso a dados
        │   │   ├── serialization/   # Suporte a YAML
        │   │   └── service/         # Regras de negócio
        │   └── resources
        │       ├── db/migration/    # Scripts Flyway
        │       ├── templates/       # Templates JasperReports
        │       └── application.yml
        └── test
            └── java/br/com/application
                ├── integrationtests/  # Testes de integração (Testcontainers)
                ├── mocks/
                ├── repository/
                └── services/          # Testes unitários (Mockito)
```

---

## Testes

Dentro da pasta `gym-system`:

```bash
mvn test      # testes unitários
mvn verify    # inclui testes de integração (requer Docker para o Testcontainers)
```

O pipeline **CI/CD com GitHub Actions** roda a build e os testes a cada push.

---

## Autor

**Henrique Oliveira Sales**

- GitHub: [@HenriqueSales2](https://github.com/HenriqueSales2)
- LinkedIn: [Henrique Sales](https://www.linkedin.com/in/henriquessales/)