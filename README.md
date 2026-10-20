# Social Analytics Dashboard

Facebook-only analytics workspace built with Spring Boot, PostgreSQL, Next.js, React, Tailwind CSS v4, and shadcn/ui.

The app imports Facebook post references from Excel and creates **synthetic engagement snapshots** with Datafaker. It does not fetch metrics from Facebook.

## Requirements

- Java 21
- Node.js 20.9 or newer and npm
- Docker Compose
- A Facebook app with OAuth login enabled for `http://localhost:8080/login/oauth2/code/facebook`

## Local startup

1. Copy `.env.example` to `.env` and enter the Facebook app ID and secret. Do not commit `.env`.
2. Start PostgreSQL, pgAdmin, and ActiveMQ Artemis for local development:

   ```bash
   docker compose up -d postgres pgadmin artemis
   ```

   Open pgAdmin at `http://localhost:5050`. Sign in with `PGADMIN_DEFAULT_EMAIL` and
   `PGADMIN_DEFAULT_PASSWORD` from `.env`; when adding the database, use host `postgres` and port
   `5432`.

3. Start Spring Boot from the repository root:

   ```bash
   ./gradlew bootRun
   ```

4. Copy `frontend/.env.example` to `frontend/.env.local`.
5. Start the frontend:

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

   Open `http://localhost:3000`. Facebook login requires valid Meta app credentials and the callback URL above.

## Run the full stack locally in Docker

Copy `.env.example` to `.env`, set Facebook credentials, and replace the sample database,
Artemis, and pgAdmin passwords before starting the services:

```bash
docker compose up --build
```

Open `http://localhost:3000`. The API listens on `http://localhost:8080`. For deployments where
users reach the API at a different public URL, set `NEXT_PUBLIC_API_BASE_URL` before building the
frontend image. This value is embedded in the browser bundle. pgAdmin is available at
`http://localhost:5050`; use the credentials from `.env` and connect to PostgreSQL with host
`postgres`, port `5432`, and the configured database credentials.

Compose binds published ports to localhost for local development. Before deploying elsewhere,
replace all sample credentials and configure network exposure and HTTPS for that environment.

## User flows

- Sign in through Facebook OAuth. The backend upserts the provider subject and profile in PostgreSQL; it does not retain an OAuth token.
- Import an `.xlsx` workbook with `post_url` as the first column and an optional `title` column. The file limit is 10 MiB and the data limit is 5,000 rows.
- Set the per-user sync interval from 1 minute to 24 hours, or select **Sync now**.
- A scheduled or manual run creates one append-only Datafaker snapshot for each active post. The dashboard receives a user-scoped STOMP event after the database transaction commits.
- Export the latest 100 snapshots to Excel. The report marks the data as `Synthetic / Datafaker`.

## Development checks

```bash
./gradlew spotlessApply
./gradlew compileJava
./gradlew spotlessCheck
```

```bash
cd frontend
npm run format
npm run lint
npm run typecheck
npm run build
```

The frontend pins ESLint 9 with Airbnb 19 via FlatCompat. Airbnb 19's declared peer range stops at ESLint 8, so `frontend/.npmrc` enables legacy peer resolution; the current flat configuration has been verified with the requested stack.

## Backend API

- `GET /api/auth/csrf`, `GET /api/auth/me`, `POST /api/auth/logout`
- `GET /api/posts`, `POST /api/posts/import`
- `GET /api/crawl-settings`, `PUT /api/crawl-settings`
- `POST /api/sync-now`, `GET /api/sync-runs`
- `GET /api/metrics/history`, `GET /api/chart-data?days=30`
- `GET /api/reports/export`
- Authenticated STOMP endpoint: `/ws`; updates arrive at `/user/queue/metrics`.
- SOAP demo WSDL: `/soap/mockExchangeRate.wsdl`.

## Configuration notes

Flyway manages schema changes and Hibernate validates the schema at startup. Facebook OAuth, database, broker, CORS origin, and secure-cookie settings are environment-backed. For HTTPS deployments set `COOKIE_SECURE=true` and configure `COOKIE_DOMAIN` when the frontend and API use sibling subdomains.
