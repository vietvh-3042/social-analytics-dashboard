# Implementation Plan: Social Analytics Dashboard

**Branch**: `001-social-analytics-dashboard` | **Date**: 2026-10-18 | **Spec**: [spec.md](spec.md)

## Summary

Build the requested analytics application as a single Spring Boot web service with a Thymeleaf dashboard. PostgreSQL stores users, posts, metrics, import batches, and aggregates. Docker Compose supplies PostgreSQL, pgAdmin, and ActiveMQ Artemis. The first delivery prioritizes a runnable local MVP: CRUD, Excel import/export, chart data, WebSocket updates, scheduled mock metrics, secure configuration, and API documentation. OAuth2, real provider adapters, and SOAP remain real extension points with local-safe defaults.

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**: Spring Boot 3.5.16, Spring Web, Spring Data JPA, Spring Security, Spring JMS, Spring WebSocket, Thymeleaf, Apache POI, Flyway, springdoc OpenAPI

**Storage**: PostgreSQL 16; Flyway migrations

**Testing**: JUnit 5, Mockito, MockMvc, Spring Boot Test, Testcontainers where Docker-backed integration is needed

**Target Platform**: Linux/macOS/Windows development via Docker Compose; JVM 21 runtime

**Project Type**: Single web application with server-rendered UI and REST/WebSocket APIs

**Performance Goals**: Dashboard API p95 under 500ms for 10,000 stored metric rows; 1,000-row Excel import with visible result and no silent loss

**Constraints**: No committed secrets; app starts without OAuth credentials; write operations are protected; async failures are logged and recoverable; source files stay under 200 lines where practical

**Scale/Scope**: Local MVP for up to 100 dashboard users, 10,000 posts, and 100,000 metric points

## Constitution Check

- **Working software**: PASS — every story has an executable local acceptance path.
- **Contract/data integrity**: PASS — entities, HTTP contracts, validation, and import/export tests are planned.
- **Secure defaults**: PASS — environment variables, CSRF/session security, and secret-safe logging are planned.
- **Observable async work**: PASS — scheduled jobs, JMS retries/DLQ, and WebSocket events have explicit logs/status.
- **Simple accessible delivery**: PASS — Thymeleaf keeps one deployable app; UI design system includes focus, contrast, responsive, loading, empty, error, and reduced-motion rules.

## Architecture

```text
Browser (Thymeleaf + Chart.js)
        │ REST / WebSocket STOMP
Spring Boot application
  web → application services → repositories → PostgreSQL
  import/export → Apache POI + Reflection mapper
  scheduler → provider ports → metrics service
  JMS producer/consumer → ActiveMQ Artemis
  security → session/basic local auth + optional OAuth2 providers
        │
Docker Compose: postgres, pgadmin, activemq
```

## Project Structure

```text
src/main/java/com/learning/social_analytics_dashboard/
├── config/
├── common/
├── user/
├── post/
├── metric/
├── importexport/
├── messaging/
├── scheduler/
├── integration/
└── web/
src/main/resources/
├── db/migration/
├── static/css/
├── static/js/
└── templates/
docker-compose.yml
Dockerfile
.env.example
```

## Delivery Phases

1. **Setup** — Gradle dependencies, Docker Compose, environment, Flyway, and application configuration.
2. **Foundation** — entities, repositories, validation, security, exception handling, and OpenAPI.
3. **MVP dashboard** — post/metric CRUD, chart aggregation, Thymeleaf dashboard, accessible states.
4. **Excel workflow** — Reflection mapper, import batch, export report, `IMPORT_COMPLETED` JMS message.
5. **Async/realtime** — scheduler, mock provider, thread pool, WebSocket event, Artemis retry/DLQ.
6. **Integration boundaries** — OAuth2 configuration, SOAP mock endpoint/client, integration tests.
7. **Polish** — documentation sync, Compose verification, test/lint/review, and cleanup.

## Complexity Tracking

| Choice | Why needed | Simpler alternative rejected because |
| --- | --- | --- |
| JMS broker | Explicit requirement for producer/consumer, retry, and DLQ | In-process events cannot prove queue behavior or survive process boundaries |
| Thymeleaf + WebSocket | Realtime dashboard is required but a separate frontend is optional | A second frontend project would increase setup and deployment cost without adding MVP value |
| Reflection mapper | Explicit requirement for dynamic Excel/entity mapping | Hand-coded per-entity mapping would not satisfy the stated learning goal |
