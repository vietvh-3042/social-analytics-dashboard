# Tasks: Social Analytics Dashboard

**Input**: Design documents from `/specs/001-social-analytics-dashboard/`

**Prerequisites**: `plan.md`, `spec.md`, `research.md`, `data-model.md`, `contracts/openapi.md`

## Phase 1: Setup

- [X] T001 Configure Spring Boot 3.5.16 and Java 21 dependencies in `build.gradle`.
- [X] T002 [P] Add Docker Compose services, health checks, and persistent volumes in `docker-compose.yml`.
- [X] T003 [P] Add application container build in `Dockerfile` and `.dockerignore`.
- [X] T004 [P] Add non-secret environment template in `.env.example` and document local commands in `README.md`.
- [X] T005 Configure PostgreSQL, Flyway, multipart limits, logging, and feature flags in `src/main/resources/application.properties`.

## Phase 2: Foundational

- [X] T006 Create Flyway baseline schema for users, social accounts, posts, metrics, imports, and aggregates in `src/main/resources/db/migration/V1__create_core_schema.sql`.
- [X] T007 [P] Implement shared API error model and exception handler in `src/main/java/com/learning/social_analytics_dashboard/common/`.
- [X] T008 [P] Implement security configuration with local credentials, CSRF, optional OAuth2 client registration, and secret-safe defaults in `src/main/java/com/learning/social_analytics_dashboard/config/SecurityConfig.java`.
- [X] T009 [P] Implement entities and repositories for User, SocialAccount, Post, SocialMetric, ImportBatch, and MetricAggregate under their domain packages.
- [X] T010 Configure OpenAPI metadata and actuator health endpoint in `src/main/java/com/learning/social_analytics_dashboard/config/ApiConfig.java`.
- [ ] T011 Add repository and security slice tests under `src/test/java/com/learning/social_analytics_dashboard/foundation/`.

## Phase 3: User Story 1 — Dashboard (P1)

- [X] T012 [US1] Add chart aggregation DTOs and service in `src/main/java/com/learning/social_analytics_dashboard/metric/MetricAggregationService.java`.
- [X] T013 [US1] Add `GET /api/chart-data` and metrics summary endpoint in `src/main/java/com/learning/social_analytics_dashboard/metric/MetricController.java`.
- [X] T014 [US1] Build responsive dashboard template in `src/main/resources/templates/dashboard.html`.
- [X] T015 [P] [US1] Add design-system tokens and accessible dashboard styles in `src/main/resources/static/css/dashboard.css`.
- [X] T016 [P] [US1] Add Chart.js, WebSocket client, loading/empty/error states, and reconnect fallback in `src/main/resources/static/js/dashboard.js`.
- [ ] T017 [US1] Add MVC/API tests for empty, populated, and error dashboard states in `src/test/java/com/learning/social_analytics_dashboard/dashboard/DashboardControllerTest.java`.

## Phase 4: User Story 2 — Posts and Metrics CRUD (P1)

- [X] T018 [US2] Add request/response DTOs and validation in `src/main/java/com/learning/social_analytics_dashboard/post/PostDto.java` and `metric/MetricDto.java`.
- [X] T019 [US2] Implement post CRUD service/controller in `src/main/java/com/learning/social_analytics_dashboard/post/`.
- [X] T020 [US2] Implement metric creation/list service/controller in `src/main/java/com/learning/social_analytics_dashboard/metric/`.
- [ ] T021 [US2] Add MockMvc contract tests for valid, invalid, unauthorized, and not-found requests in `src/test/java/com/learning/social_analytics_dashboard/api/CrudApiTest.java`.

## Phase 5: User Story 3 — Excel Import/Export (P1)

- [X] T022 [US3] Implement constrained Reflection-based row mapper in `src/main/java/com/learning/social_analytics_dashboard/importexport/ExcelReflectionMapper.java`.
- [X] T023 [US3] Implement workbook parser, row diagnostics, and import service in `src/main/java/com/learning/social_analytics_dashboard/importexport/ExcelImportService.java`.
- [X] T024 [US3] Implement report exporter in `src/main/java/com/learning/social_analytics_dashboard/importexport/ExcelExportService.java`.
- [X] T025 [US3] Add import/export endpoints and batch responses in `src/main/java/com/learning/social_analytics_dashboard/importexport/ImportExportController.java`.
- [ ] T026 [US3] Add import/export contract tests with valid, malformed, duplicate, and oversized workbooks in `src/test/java/com/learning/social_analytics_dashboard/importexport/ExcelImportExportTest.java`.

## Phase 6: User Story 4 — Async Metrics and Realtime (P2)

- [X] T027 [US4] Add provider port and deterministic mock social provider in `src/main/java/com/learning/social_analytics_dashboard/integration/social/`.
- [X] T028 [US4] Configure scheduled execution and bounded async executor in `src/main/java/com/learning/social_analytics_dashboard/config/AsyncConfig.java`.
- [X] T029 [US4] Implement `updateSocialMetricsJob` with per-account failure isolation in `src/main/java/com/learning/social_analytics_dashboard/scheduler/SocialMetricsJob.java`.
- [X] T030 [US4] Configure STOMP/WebSocket and broadcast metrics events in `src/main/java/com/learning/social_analytics_dashboard/messaging/WebSocketConfig.java` and `MetricEventPublisher.java`.
- [ ] T031 [US4] Add job and WebSocket tests in `src/test/java/com/learning/social_analytics_dashboard/async/`.

## Phase 7: User Story 5 — JMS, OAuth2, SOAP, Infrastructure (P2)

- [ ] T032 [US5] Configure Artemis JMS, `IMPORT_COMPLETED` producer/consumer, retry, and DLQ in `src/main/java/com/learning/social_analytics_dashboard/messaging/`.
- [X] T033 [US5] Add OAuth2 provider properties and login success handling in `src/main/java/com/learning/social_analytics_dashboard/config/OAuth2Config.java`.
- [X] T034 [US5] Add mock SOAP exchange-rate schema, endpoint, client adapter, and integration test in `src/main/java/com/learning/social_analytics_dashboard/integration/soap/` and `src/test/java/com/learning/social_analytics_dashboard/integration/soap/`.
- [X] T035 [US5] Add Testcontainers integration tests for PostgreSQL and Artemis in `src/test/java/com/learning/social_analytics_dashboard/integration/containers/`.
- [X] T036 [US5] Add OpenAPI annotations and verify the documented endpoints match `specs/001-social-analytics-dashboard/contracts/openapi.md`.

## Phase 8: Polish and Verification

- [X] T037 [P] Update `docs/system-architecture.md`, `docs/code-standards.md`, `docs/development-roadmap.md`, and `docs/project-changelog.md` to match the delivered system.
- [X] T038 [P] Add production-safe Docker health/readiness checks and secret scanning patterns to `.gitignore`.
- [X] T039 Run the Scope Contract test suite and Compose smoke test from `specs/001-social-analytics-dashboard/quickstart.md`.
- [X] T040 Run code review, fix all accepted findings, and record final verification evidence in `plans/reports/`.

## Dependencies and Execution Order

- Phase 1 blocks Phase 2.
- Phase 2 blocks all user stories.
- US1 and US2 can proceed after Phase 2; US3 depends on Post and Metric services from US2; US4 depends on Metric persistence and chart events; US5 depends on import events and the foundational security/configuration layer.
- Phase 8 runs after the desired stories are complete.

## MVP Scope

Phases 1–5 deliver the first usable dashboard: Docker PostgreSQL/pgAdmin, CRUD, dashboard, chart data, and Excel import/export. Phases 6–7 add realtime, scheduling, JMS, OAuth2, and SOAP boundaries.

## Phase 9: Convergence

- [X] T041 Add `MetricAggregate` persistence, migration, refresh service, and dashboard queries so aggregates are rebuilt after metric changes and imports per FR-009 and the data model (missing).
- [X] T042 Change dashboard KPI and chart aggregation to use the latest metric per post and cover empty, historical, and populated cases per US1/AC2 (partial).
- [ ] T043 Configure Artemis retry and dead-letter handling for failed import messages and expose actionable failure logging per FR-010 and Constitution IV (missing).
- [X] T044 Add explicit OAuth2 Facebook/Twitter registration properties and safe local defaults per FR-012 (partial).
- [X] T045 Add a SOAP exchange-rate client adapter and integration boundary tests per FR-011 (missing).
- [ ] T046 Add repository, security, REST CRUD, dashboard, import/export, scheduler, WebSocket, JMS, and SOAP contract tests per FR-002, FR-003, FR-007, FR-008, FR-010, FR-011, and FR-016 (missing).
- [X] T047 Add Testcontainers-backed PostgreSQL and Artemis integration tests with a Docker-aware test profile per FR-015 and FR-016 (missing).
- [X] T048 Add import validation for file type, empty workbook, malformed dates, and oversized input with rollback-safe diagnostics per US3 edge cases (partial).
- [X] T049 Add source-backed architecture, data-flow, import/export, metrics-update, and deployment diagrams in editable `.drawio` format per the project documentation workflow (missing).
- [X] T050 Synchronize README, architecture, roadmap, changelog, API contract, and quickstart with the implemented behavior per Constitution V and documentation-management rules (partial).
