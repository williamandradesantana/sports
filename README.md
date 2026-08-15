# ⚽ Sports API

API REST para consulta de dados de futebol (ligas, times, partidas e temporadas), com sincronização automática a partir de uma fonte externa (API-Football) e autenticação via JWT e login social com Google.

## 📋 Sobre o projeto

O **Sports API** centraliza e expõe informações esportivas ligas, temporadas, times e partidas através de endpoints REST versionados. Os dados são obtidos periodicamente de um provedor externo (**API-Football**) por meio de jobs agendados e também podem ser sincronizados sob demanda por usuários administradores.

O projeto segue uma arquitetura em camadas inspirada em **Clean Architecture / Arquitetura Hexagonal**, separando claramente:

- **`domain`**: entidades e regras de negócio (League, Team, Match, Season, Venue, User, Permission), independentes de framework;
- **`application`**: casos de uso (use cases) que orquestram o domínio;
- **`infrastructure`**: implementações técnicas: persistência JPA, segurança (JWT/OAuth2), integração com a API-Football, agendamento de jobs e configurações;
- **`interfaces`**: controllers REST e DTOs expostos publicamente.

## ✨ Funcionalidades

- 🏆 Consulta de **ligas**, **times**, **partidas** e **temporadas**, com paginação e ordenação;
- 🔄 **Sincronização automática** de ligas, times e partidas via jobs agendados (`@Scheduled`), consumindo a API-Football;
- 🔐 **Autenticação** via JWT (registro/login local) e **login social com Google** (OAuth2);
- 🛡️ Endpoints administrativos protegidos por permissão (`ADMIN`) para sincronização manual de dados;
- 📄 **Documentação interativa** da API via OpenAPI/Swagger, renderizada com **Scalar**;
- 🗄️ Versionamento de banco de dados com **Flyway**;
- 🔗 Respostas HATEOAS-ready (Spring HATEOAS);
- ✅ Testes unitários e de integração com **JUnit 5** e **Testcontainers** (PostgreSQL).

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 4.1**
- **Spring Data JPA** + **PostgreSQL**
- **Flyway** (migrations)
- **Spring Security** + **JWT (java-jwt)** + **OAuth2 Client (Google)**
- **Spring HATEOAS**
- **springdoc-openapi** + **Scalar** (documentação interativa)
- **Testcontainers** + **JUnit 5** (testes)
- **Docker Compose** (banco de dados local)
- **Maven**

## 🏗️ Arquitetura

```
src/main/java/io/github/williamandradesantana/sports/
├── domain/            # Entidades e regras de negócio puras
├── application/       # Casos de uso (use cases)
├── infrastructure/     # Persistência, segurança, scheduling, integração externa
└── interfaces/         # Controllers REST e DTOs
```

### Principais recursos (endpoints)

| Recurso | Endpoint público | Endpoint administrativo (sync) |
|---|---|---|
| Autenticação | `POST /api/v1/auth/register`, `POST /api/v1/auth/login` | — |
| Ligas | `GET /api/v1/leagues`, `GET /api/v1/leagues/{id}` | `POST /api/v1/admin/leagues/sync` |
| Times | `GET /api/v1/teams`, `GET /api/v1/teams/{id}` | `POST /api/v1/admin/teams/sync`, `POST /api/v1/admin/teams/sync-league` |
| Partidas | `GET /api/v1/matches`, `GET /api/v1/matches/{id}` | `POST /api/v1/admin/matches/sync`, `/sync-league`, `/sync-batch` |

Os endpoints administrativos exigem um usuário autenticado com a permissão `ADMIN`.

## 🚀 Como executar localmente

### Pré-requisitos

- Java 21+
- Docker e Docker Compose
- Uma chave de API válida da [API-Football](https://www.api-football.com/)
- Credenciais OAuth2 do Google (Client ID e Secret)

### Passo a passo

1. Clone o repositório:
   ```bash
   git clone https://github.com/williamandradesantana/sports.git
   cd sports
   ```

2. Copie o arquivo de variáveis de ambiente e preencha com seus valores:
   ```bash
   cp .env.example .env
   ```

3. Suba o banco de dados PostgreSQL:
   ```bash
   docker compose up -d
   ```

4. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

5. Acesse a documentação interativa da API em:
   ```
   http://localhost:8080/scalar
   ```

### Rodando os testes

```bash
./mvnw test
```

Os testes de integração utilizam **Testcontainers** para subir um PostgreSQL descartável automaticamente não é necessário nenhum banco externo para rodá-los.

## 📄 Licença

Este projeto está sob livre uso para fins de estudo e portfólio. Sinta-se à vontade para explorar o código.

---

Desenvolvido por [William Andrade Santana](https://github.com/williamandradesantana).
