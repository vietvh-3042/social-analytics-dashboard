# Quickstart

## Prerequisites

- Docker Engine with Compose plugin.
- Java 21.
- Internet access for the first Gradle dependency download.

## Start local infrastructure

```bash
cp .env.example .env
docker compose up -d --build
```

## Run the application

```bash
./gradlew bootRun
```

Open `http://localhost:8080` for the dashboard and `http://localhost:5050` for pgAdmin.

## Verify

```bash
./gradlew test --no-daemon
curl http://localhost:8080/actuator/health
curl -L http://localhost:8080/swagger-ui.html -o /dev/null -w '%{http_code}\n'
curl http://localhost:8080/ws/exchange-rate.wsdl -o /dev/null -w '%{http_code}\n'
```

Default local credentials are documented in `.env.example`; replace them before sharing an environment.
