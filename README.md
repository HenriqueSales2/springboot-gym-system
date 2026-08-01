# Spring Boot Gym System

REST API desenvolvida com **Java 21**, **Spring Boot**, **Spring Data JPA**, **MySQL**, **Flyway**, **Swagger/OpenAPI**, **JasperReports**, **JUnit 5**, **Mockito** e **Testcontainers** para gerenciamento de pessoas, exercícios de academia, armazenamento de arquivos, geração de relatórios e envio de e-mails.

[![CI/CD with GitHub Actions](https://github.com/HenriqueSales2/springboot-gym-system/actions/workflows/continuos-deployment.yml/badge.svg)](https://github.com/HenriqueSales2/springboot-gym-system/actions/workflows/continuos-deployment.yml)

---

## Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Web
* Spring Mail
* Spring HATEOAS
* MySQL
* Flyway
* Swagger/OpenAPI
* JasperReports
* JUnit 5
* Mockito
* Testcontainers
* Maven

---

## Funcionalidades

### Pessoas

* Cadastro de pessoas
* Atualização de pessoas
* Exclusão de pessoas
* Busca por ID
* Busca por nome
* Listagem paginada
* Exportação individual em PDF
* Exportação de páginas em:

  * PDF
  * CSV
  * XLSX
* Importação em massa através de arquivos CSV e XLSX

### Exercícios

* Cadastro de exercícios
* Atualização de exercícios
* Exclusão de exercícios
* Busca por ID
* Listagem paginada

### Arquivos

* Upload de arquivo único
* Upload múltiplo
* Download de arquivos

### E-mails

* Envio de e-mail simples
* Envio de e-mail com anexo

---

# Árvore do projeto

```text
src
├── main
│   ├── java
│   │   └── br/com/application
│   │       ├── config/          # Configurações da aplicação
│   │       ├── controller/      # Endpoints REST e documentação com Swagger
│   │       ├── data/
│   │       │   ├── dto/         # Objetos de transferência
│   │       │   └── vo/          # Value Objects
│   │       ├── exception/       # Tratamento global de exceções
│   │       ├── file/            # Exportador e Importador de arquivos
│   │       ├── mail/            # Lógica de mandar email
│   │       ├── mapper/          # Conversão entre entidades e DTOs
│   │       ├── model/           # Entidades JPA
│   │       ├── repository/      # Camada de acesso a dados
│   │       ├── serialization/   # Suporte para YAML
│   │       ├── service/         # Regras de negócio
│   │       └── SpringBootGymSystemApplication.java
│   │
│   └── resources
│       ├── db/migration/        # Scripts Flyway
│       ├── templates/           # Templates JasperReports e auxiliares
│       └── application.yml      # Configurações da aplicação
│
└── test                         # todos os testes
    └── java
        └── br/com/application
            ├── integrationtests/
            ├── mocks/
            ├── repository/
            └── services/
```
---

# Configuração do Ambiente

## Banco de Dados

Crie um banco MySQL:

```sql
CREATE DATABASE rest_with_spring_boot_java;
```

Configure o arquivo no application.yml:
```text
└── resources
       ├── db/migration/
       ├── templates/       
       └── application.yml      # AQUI
```
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rest_with_spring_boot_java
    username: seu_usuario
    password: sua_senha
```

---

## Configuração do Gmail

Crie as variáveis de ambiente:

### Windows

```cmd
setx EMAIL_USERNAME "seuemail@gmail.com"
setx EMAIL_PASSWORD "senha_de_aplicativo_google"
```

### Linux

```bash
export EMAIL_USERNAME=seuemail@gmail.com
export EMAIL_PASSWORD=sua_senha_de_aplicativo
```

> Utilize uma Senha de Aplicativo do Google e não sua senha normal.

---

## Configuração do Diretório de Upload

Ajuste o caminho:

```yaml
file:
  upload-dir: C:/caminho/do/projeto/UploadDir
```

---

# Executando o Projeto

Clone o repositório:

```bash
git clone https://github.com/HenriqueSales2/springboot-gym-system.git
```

Entre na pasta:

```bash
cd gym-system
```

Execute:

```bash
mvn spring-boot:run
```

Ou:

```bash
mvn clean install
java -jar target/*.jar
```

---

# Swagger ou Postman

Após iniciar a aplicação:

```text
http://localhost:8080/swagger-ui/index.html
```

> Sugiro que utilize o Postman e importe a Collection que eu irei mencionar abaixo.

---


# Endpoints de Pessoas

Base URL:

```text
/api/person/v1
```

---

## Listar Pessoas

```http
GET /api/person/v1
```

Parâmetros:

| Parâmetro | Padrão |
| --------- | ------ |
| page      | 0      |
| size      | 12     |
| direction | asc    |

Exemplo:

```http
GET /api/person/v1?page=0&size=6&direction=asc
```

<img width="1435" height="958" alt="image" src="https://github.com/user-attachments/assets/00382b9c-7fe5-4de9-b685-87998407aea6" />

---

## Buscar Pessoa por ID

```http
GET /api/person/v1/{id}
```

Exemplo:

```http
GET /api/person/v1/1
```

<img width="1433" height="961" alt="image" src="https://github.com/user-attachments/assets/273f2ef3-1e30-46e2-af56-009be03e3322" />

---

## Buscar Pessoa por Nome

```http
GET /api/person/v1/findPeopleByName/{firstName}
```

Exemplo:

```http
GET /api/person/v1/findPeopleByName/Phil
```

<img width="1435" height="953" alt="image" src="https://github.com/user-attachments/assets/64f01c8f-0e22-4267-a6ed-f0cfd4b04bb7" />

---

## Criar Pessoa

```http
POST /api/person/v1
```

Body:

```xml
<PersonDTO>
    <id>1</id>
    <firstName>John</firstName>
    <lastName>Doe</lastName>
    <address>São Paulo - Brazil</address>
    <gender>Male</gender>
    <enabled>True</enabled>
</PersonDTO>
```

<img width="1436" height="958" alt="image" src="https://github.com/user-attachments/assets/cf8e952d-405d-4dbe-ad76-c549dad139af" />

---

## Atualizar Pessoa

```http
PUT /api/person/v1
```

Body:

```json
{
  "id": 1,
  "firstName": "Mary",
  "lastName": "Doe",
  "address": "New York - USA",
  "gender": "Female",
  "enabled": true
}
```

<img width="1436" height="958" alt="image" src="https://github.com/user-attachments/assets/a2de0ce6-4259-4202-9a78-a3e296108675" />

---

## Atualização Parcial

```http
PATCH /api/person/v1/{id}
```

Exemplo:

```http
PATCH /api/person/v1/1
```

<img width="1436" height="960" alt="image" src="https://github.com/user-attachments/assets/c4fbdbb8-695d-4341-81c8-b5ff74d5d0e8" />

---

## Excluir Pessoa

```http
DELETE /api/person/v1/{id}
```

Exemplo:

```http
DELETE /api/person/v1/1
```

<img width="1436" height="958" alt="image" src="https://github.com/user-attachments/assets/2e3ee27c-0534-4847-a643-0cf26657ef77" />

---

# Exportação de Relatórios

## Exportar Pessoa

```http
GET /api/person/v1/exportPerson/{id}
```

Header:

```http
Accept: application/pdf
```

<img width="1435" height="962" alt="image" src="https://github.com/user-attachments/assets/1baf270b-a049-4818-a6be-b38e883a091a" />

---

## Exportar Página

PDF:

```http
GET /api/person/v1/exportPage
Accept: application/pdf
```

<img width="1434" height="961" alt="image" src="https://github.com/user-attachments/assets/7662011b-02ea-4619-9c84-016dff797014" />

CSV:

```http
GET /api/person/v1/exportPage
Accept: text/csv
```
No Postman:

<img width="1433" height="960" alt="image" src="https://github.com/user-attachments/assets/8341c58f-9258-4430-bb29-8075dcecf8b0" />


No Excel:

<img width="1865" height="944" alt="image" src="https://github.com/user-attachments/assets/2ba0458b-750d-40fb-a688-0453c552735b" />


XLSX:

```http
GET /api/person/v1/exportPage
Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
```
No Postman:

<img width="1435" height="959" alt="image" src="https://github.com/user-attachments/assets/0659c9ba-ebc1-46ed-abab-f5a52ba32626" />


No Excel:

<img width="1865" height="942" alt="image" src="https://github.com/user-attachments/assets/76da4286-d3ab-4f2d-9176-ca61cababca5" />

---

# Endpoints de Exercícios

Base URL:

```text
/api/workout/v1
```

---

## Listar Exercícios

```http
GET /api/workout/v1
```

<img width="1433" height="958" alt="image" src="https://github.com/user-attachments/assets/b89f4bcd-289a-4cfc-89bb-8a88ddaf62ba" />

---

## Buscar Exercício por ID

```http
GET /api/workout/v1/{id}
```

<img width="1429" height="961" alt="image" src="https://github.com/user-attachments/assets/5e1bc777-8e06-4f6f-a00b-4724245df81e" />

---

## Criar Exercício

```http
POST /api/workout/v1
```

Exemplo:

```json
{
  "exerciseName": "Bench Press",
  "muscleGroup": "Chest",
  "equipment": "Barbell",
  "difficulty": "Intermediate"
}
```

<img width="1433" height="960" alt="image" src="https://github.com/user-attachments/assets/959ffb7f-ca57-469e-abf1-4ab32cdac5e5" />

---

## Atualizar Exercício

```http
PUT /api/workout/v1
```

<img width="1437" height="960" alt="image" src="https://github.com/user-attachments/assets/d91e8bbf-1963-417d-99db-e7eba52f6030" />

---

## Excluir Exercício

```http
DELETE /api/workout/v1/{id}
```

<img width="1435" height="960" alt="image" src="https://github.com/user-attachments/assets/903a9a8c-195c-41e8-a407-2429965f8e83" />

---

# Upload e Download de Arquivos

Base URL:

```text
/api/file/v1
```

---

## Upload de Arquivo

```http
POST /api/file/v1/uploadFile
```

Body:

```form-data
file: arquivo.pdf
```

<img width="1434" height="937" alt="image" src="https://github.com/user-attachments/assets/52add364-7b2f-4670-948a-504bf87a163f" />

---

## Upload Múltiplo

```http
POST /api/file/v1/uploadMultipleFiles
```

Body:

```form-data
files: arquivo1.pdf
files: arquivo2.xlsx
files: arquivo3.csv
```

<img width="1436" height="960" alt="image" src="https://github.com/user-attachments/assets/083b0cc0-6bc0-4762-9ca1-8683aaccce5d" />

---

## Download

```http
GET /api/file/v1/downloadFile/{fileName}
```

Exemplo:

```http
GET /api/file/v1/downloadFile/people_exported.pdf
```

<img width="1436" height="955" alt="image" src="https://github.com/user-attachments/assets/1be47c49-f148-4c1d-834a-bdb7c106b664" />

---

# Envio de E-mails

Base URL:

```text
/api/email/v1
```

---

## E-mail Simples

```http
POST /api/email/v1
```

Body:

```json
{
  "to": "destinatario@email.com",
  "subject": "Teste",
  "message": "Mensagem enviada pela API"
}
```
No Postman:
<img width="1437" height="961" alt="print email" src="https://github.com/user-attachments/assets/2e9f686f-1cc6-4ff6-8814-b74b04e20ed6" />

No Gmail:
<img width="1869" height="944" alt="print email gmail" src="https://github.com/user-attachments/assets/c590db40-a315-4ef0-b277-1f7e1b329885" />


---

## E-mail com Anexo

```http
POST /api/email/v1/withAttachment
```

Body (form-data):

```text
emailRequest = {
  "to":"destinatario@email.com",
  "subject":"Teste",
  "message":"Mensagem com anexo"
}

attachment = arquivo.pdf
```
No Postman:
<img width="1436" height="959" alt="print email with image" src="https://github.com/user-attachments/assets/9a00b6dd-2120-4761-ae22-23080109d1ed" />

No Gmail:
<img width="1864" height="942" alt="print email with image gmail" src="https://github.com/user-attachments/assets/70a18ab6-0546-42f7-afa9-72e21d597d7b" />

---

# Testes

Executar todos os testes:

```bash
mvn test
```

Executar testes de integração:

```bash
mvn verify
```

---

# Coleção Postman

O projeto possui coleções prontas:

```text
Collections/
├── Gym Training API.postman_collection.json
└── Spring_Boot_Application.postman_environment.json
```

Importe ambos no Postman para testar todos os endpoints rapidamente.

---

# Autor

Henrique Oliveira Sales

LinkedIn:
https://www.linkedin.com

GitHub:
https://github.com/HenriqueSales2
