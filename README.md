# MDD (Monde de Dev)

Full-stack application for sharing articles between developers, with a topic subscription system.

## Prerequisites

- **Java** 21
- **Maven** 3.9+
- **Node.js** 18+
- **npm** 9+
- **MySQL** 8+

## Installation

### Database

Create a MySQL user or use an existing one. The `mdd` database will be created automatically on startup.

### Back-end

```bash
cd back
```

Create a `.env` file at the root of the `back/` directory based on the provided example:

```bash
cp .env.example .env
```

Then edit `.env` with your own values (database credentials, JWT secret, etc.).

Install dependencies and run:

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

The server starts on `http://localhost:3001`.

### Front-end

```bash
cd front
npm install
npm start
```

The application starts on `http://localhost:4200`.

## Running tests

### Back-end tests (Java / JUnit 5 + Mockito)

```bash
cd back
DB_TEST_USERNAME=your_user DB_TEST_PASSWORD=your_password mvn test
```

- **Unit tests**: 29 tests (services with Mockito mocks)
- **Integration tests**: 30 tests (controllers via MockMvc + MySQL `mdd_test` database)

The JaCoCo coverage report is generated at `back/target/site/jacoco/index.html`.

### Front-end tests (Jest)

```bash
cd front
npm test
```

- **53 tests** covering components (integration with HttpTestingController), services, guard and interceptor
- ~89% coverage

The coverage report is generated at `front/coverage/lcov-report/index.html`.

### E2E tests (Cypress)

Prerequisites: both back-end and front-end must be running.

```bash
cd front
npm run e2e          # headless mode
npm run e2e:open     # interactive mode
```

- **13 scenarios** covering all user journeys

#### E2E coverage report

Start the instrumented server, run Cypress, then generate the report:

```bash
cd front
npx ng run front:serve-coverage   # start instrumented dev server on port 4200
npx cypress run                   # run E2E tests (in another terminal)
npx nyc report --reporter=lcov --reporter=text-summary --report-dir=coverage-e2e
```

The coverage report is generated at `front/coverage-e2e/lcov-report/index.html`.

## API endpoints

### Authentication (public)

| Method | Endpoint | Description | Body |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register a new user | `{ email, username, password }` |
| POST | `/api/auth/login` | Login | `{ emailOrUsername, password }` |

Response: `{ token }` (JWT valid for 24h)

### User

| Method | Endpoint | Description | Body |
|--------|----------|-------------|------|
| GET | `/api/user/me` | Get current user profile | - |
| PUT | `/api/user/me` | Update profile (partial) | `{ email?, username?, password? }` |

### Topics

| Method | Endpoint | Description | Body |
|--------|----------|-------------|------|
| GET | `/api/topics` | List all topics | - |

### Subscriptions

| Method | Endpoint | Description | Body |
|--------|----------|-------------|------|
| POST | `/api/topics/{id}/subscribe` | Subscribe to a topic | - |
| DELETE | `/api/topics/{id}/subscribe` | Unsubscribe from a topic | - |

### Posts

| Method | Endpoint | Description | Body |
|--------|----------|-------------|------|
| GET | `/api/posts/feed` | Get feed from subscribed topics | - |
| GET | `/api/posts/{id}` | Get post detail | - |
| POST | `/api/posts` | Create a post | `{ topicId, title, content }` |

### Comments

| Method | Endpoint | Description | Body |
|--------|----------|-------------|------|
| GET | `/api/posts/{postId}/comments` | Get comments for a post | - |
| POST | `/api/posts/{postId}/comments` | Add a comment | `{ content }` |

### Request authentication

All endpoints except `/api/auth/**` require the following header:

```
Authorization: Bearer <token>
```

