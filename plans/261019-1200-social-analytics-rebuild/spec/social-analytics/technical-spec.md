# Social Analytics — Draft Technical Spec

Status: draft; derived from the supplied requirements images and task scope. Confirm open decisions in the parent plan before implementation.

## Product Scope

An admin imports Facebook post references, signs in with Facebook, configures an individual metric refresh interval, and views/export metric history. The system stores a successful Facebook-authenticated admin. It does not provide X/Twitter or local account login. Snapshot values are generated with Datafaker for existing posts; do not represent them as provider-reported metrics.

## Users and Stories

- **US-001 Admin login:** As an admin, I sign in with Facebook and return to the dashboard; a successful identity is persisted and linked to owned data.
- **US-002 Post import:** As an admin, I upload a valid Excel workbook of Facebook post references and review accepted/rejected rows.
- **US-003 Crawl control:** As an admin, I configure my refresh interval, see last/next run, and trigger Sync Now.
- **US-004 Metric analysis:** As an admin, I view per-post and aggregate snapshots, charts, and live refresh notifications.
- **US-005 Data exchange:** As an admin, I export a report workbook; the backend also demonstrates queued work, SOAP messaging, and generic reflection-based import/export mapping.

## Functional Requirements

- **FR-001** Authenticate only through Facebook OAuth2. On successful callback, upsert and persist the Facebook user; do not create a local-account flow.
- **FR-002** Enforce ownership: each admin sees and mutates only their posts, settings, sync runs, and metrics.
- **FR-003** Import post references from Excel using validated headers/rows, bounded upload size, duplicate detection, and actionable row errors.
- **FR-004** Persist each admin's crawl interval and run metadata. Execute only due admins on scheduled runs; Sync Now invokes the same service path.
- **FR-005** For each existing post, create a timestamped metric snapshot with Datafaker-generated values. Keep history; do not overwrite prior snapshots.
- **FR-006** Return post, metric-history, aggregate chart, crawl-settings, sync-status, import, and export APIs with OpenAPI documentation.
- **FR-007** Notify authorized dashboard subscribers over STOMP/WebSocket after a snapshot run completes; the client refreshes chart data.
- **FR-008** Publish an `IMPORT_COMPLETED` JMS event after a successful import; consume asynchronously for aggregate refresh/notification. Configure retries and a dead-letter path.
- **FR-009** Provide Excel report export and a generic reflection-backed mapper restricted to explicitly annotated/allow-listed fields.
- **FR-010** Provide a narrowly scoped Spring-WS SOAP client/endpoint demo, documented with its request/response contract.
- **FR-011** Preserve CSRF protections for browser session requests; handle OAuth state, secure cookies, token secrecy, upload validation, and authorization.

## Technical Constraints

- Backend: existing Spring Boot 3.x, Java 21, Gradle, PostgreSQL, Spring Data JPA, Flyway; strict package-by-layer organization.
- Frontend: Next.js + React + Tailwind CSS v4 + shadcn/ui; feature-based layered architecture. Custom component `.tsx` filenames and component identifiers are PascalCase.
- Existing `build.gradle` already declares OAuth2, Security, WebSocket, Web Services, Artemis, POI, Datafaker, Flyway, OpenAPI and test libraries; verify versions/configuration and add only missing pieces.
- Define shared/named constants and enums. Keep backend API DTOs separate from JPA entities.
- Avoid public metric crawling/provider calls in this iteration; Datafaker is the metric source.

## Proposed Data Model

- `AdminUser`: id, Facebook provider subject (unique), display name, email/avatar if granted, created/updated timestamps.
- `OAuthCredential`: admin FK, encrypted access/refresh token as applicable, expiry/scopes; never serialize into API DTOs.
- `Post`: id, admin FK, provider=`FACEBOOK`, external post URL/id, title/label, importedAt, active flag; unique per admin/provider/external reference.
- `MetricSnapshot`: id, post FK, capturedAt, reactions, comments, shares, optional followers; append-only.
- `CrawlSetting`: admin FK (unique), interval value/unit, enabled, nextRunAt, lastRunAt.
- `SyncRun`: admin FK, source (`SCHEDULED`/`MANUAL`), status, startedAt/finishedAt, counts, safe error summary.
- `ImportJob` / row errors as needed for asynchronous import status and UI feedback.

## API and Event Outline

- OAuth: Spring Security authorization/callback routes; successful callback persists/upserts user and returns to dashboard.
- REST: `/api/posts`, `/api/posts/import`, `/api/reports/export`, `/api/metrics`, `/api/chart-data`, `/api/crawl-settings`, `/api/sync-runs`, `/api/sync-now`.
- WebSocket: authenticated `/ws`; admin-scoped topic/event for `METRICS_UPDATED` and sync status.
- JMS: `IMPORT_COMPLETED` event includes import job id and admin id only; consumer reloads trusted DB state.
- SOAP: one explicit service contract and bounded DTOs; never expose reflection or arbitrary class/method names on the wire.

## Acceptance Criteria

- **SC-001** OAuth success persists one Facebook admin; repeated callback for the same provider subject does not create duplicates. X and local login are absent.
- **SC-002** Admin isolation holds for every API, export, WebSocket subscription, scheduled run, and imported row.
- **SC-003** Valid import creates owned posts; malformed/duplicate rows return stable row-level results without partial corruption.
- **SC-004** Sync Now and due scheduled runs append one snapshot per eligible existing post and update run metadata; retries do not duplicate a run's snapshots.
- **SC-005** Persisted interval changes affect that admin's next due run without altering another admin's schedule.
- **SC-006** Dashboard charts load historical aggregates and update after an authorized realtime event; disconnect/reconnect falls back to REST refresh.
- **SC-007** Export matches documented columns; generic mapper rejects unallowlisted fields/types.
- **SC-008** JMS failure/retry and SOAP contract paths have integration coverage; no credentials or tokens appear in logs/responses.

## Risks / Decisions

Provider scopes and Meta review constrain what can be accessed. The exact interval bounds, Excel schema, metric set, timezone, and SOAP target are unresolved; see parent plan.
