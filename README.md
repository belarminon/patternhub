# PatternHub

PatternHub is the challenge project for DIO and a portfolio project to improve Java/Kotlin backend skills using modern technologies (Kotlin, Spring Boot, Gradle Kotlin DSL, Testcontainers, Flyway, Docker, OpenAPI). It demonstrates design patterns, SOLID principles and clean architecture.

## Prerequisites

- Java 21+
- Gradle (wrapper provided)
- Node.js 18+ and npm
- Docker & Docker Compose (optional, recommended for full stack)

This repository's canonical root for the project is the `patternhub` directory; run CI and Docker commands from there.

## Quickstart (Docker)

This repository's project root is the `patternhub` directory. Run Docker Compose or local commands from there.

### Docker (recommended)

From `desafio/patternhub`:

```bash
docker compose up --build
```

This starts PostgreSQL, the backend on `http://localhost:8080` and the frontend on `http://localhost:5173`.

### Backend (local)

From `desafio/patternhub/patternhub-backend`:

```bash
./gradlew bootJar
java -jar build/libs/patternhub-backend-0.1.0.jar
```

API docs: `http://localhost:8080/swagger-ui.html`

### Basic API checks

Use these curl examples to verify the API. Replace `localhost:8080` if your backend runs elsewhere.

- List users:
```bash
curl -X GET http://localhost:8080/api/users
```
- Create user:
```bash
curl -X POST http://localhost:8080/api/users -H 'Content-Type: application/json' -d '{"name":"Alice","email":"alice@example.com"}'
```
- Update user:
```bash
curl -X PUT http://localhost:8080/api/users/1 -H 'Content-Type: application/json' -d '{"name":"Alice B","email":"aliceb@example.com"}'
```
- Delete user:
```bash
curl -X DELETE http://localhost:8080/api/users/1
```
- Create request (requires existing user id):
```bash
curl -X POST http://localhost:8080/api/requests -H 'Content-Type: application/json' -d '{"userId":1,"type":"GENERAL","description":"Test","priority":"NORMAL"}'
```

If you receive HTTP 400 or validation errors, the backend will return JSON describing the invalid fields. For HTTP 500, check backend logs:

```bash
docker compose logs backend --follow
# or if running locally from patternhub-backend
./gradlew bootRun
```

Note: If you get `./gradlew: No such file or directory` when running from WSL, run the wrapper from the repository root where `gradlew` exists, or use `docker compose up backend` to run the service in Docker.

### Users API

New endpoints were added to manage users:

- `GET /api/users` — list users
- `POST /api/users` — create user with JSON body `{ "name": "Alice", "email": "a@example.com" }`
- `GET /api/users/{id}` — get user by id

The backend includes a simple `User` entity, `UserRepository`, `UserService`, and `UserController`.

### Frontend (local)

From `desafio/patternhub/patternhub-frontend`:

```bash
npm install
npm run dev
```

The frontend proxies to `/api` — adjust if backend runs on another host.

New frontend features:
- Users view and creation form on the main page. It calls the new `/api/users` endpoints to list and create users.

To run the frontend in Docker Compose dev mode (source bind-mounted and served by Vite):

```bash
# from desafio/patternhub
docker compose up --build db backend
docker compose up frontend-dev
```

Alternatively run locally:

```bash
# start backend first (docker compose or local)
# then from patternhub-frontend
npm install
BACKEND_URL=http://localhost:8080 npm run dev
```

## Project Structure

- `patternhub-backend`: Kotlin + Spring Boot API demonstrating Strategy pattern for processing and NotificationChannel interface.
- `patternhub-frontend`: Minimal React app to list requests.

This project includes:

- Flyway migrations under `patternhub-backend/src/main/resources/db/migration`.
- Example unit and integration tests (Testcontainers) under `patternhub-backend/src/test/kotlin`.
- GitHub Actions workflow at `.github/workflows/ci.yml`.

## Implemented Patterns

- Strategy: `ProcessingStrategy` + concrete strategies
- Dependency Inversion: services depend on interfaces
- Repository pattern via Spring Data JPA

## Next Steps / Ideas

- Add unit and integration tests (use `spring-boot-starter-test`)
- Implement authentication and authorization
- Add more notification channels (Push, Webhook)
- Add CI pipeline to build and run tests

## Notes

- The backend targets Spring Boot 3 (Jakarta EE); entity imports were migrated to `jakarta.persistence`.
- Java 21+ is recommended. If you set `kotlinOptions.jvmTarget = "21"`, consider upgrading Kotlin to 1.9.20+ for full compatibility.
- Use the Gradle wrapper inside `desafio/patternhub/patternhub-backend` to build (`./gradlew bootJar`).

## Continuous Integration

- A basic GitHub Actions workflow is included at `.github/workflows/ci.yml` to build and test the backend and to build the frontend on push/pull requests.

## Tests

- A unit test example was added for `NormalProcessingStrategy` at `patternhub-backend/src/test/kotlin/br/com/patternhub/service/NormalProcessingStrategyTest.kt`.

## README policy

- I will update `README.md` whenever project structure, CI, or run instructions change. If you'd like a different canonical README location, tell me and I'll keep it synchronized.

