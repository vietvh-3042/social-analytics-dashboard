# Feature Specification: Social Analytics Dashboard

**Feature Branch**: `001-social-analytics-dashboard`

**Created**: 2026-10-18

**Status**: Draft

**Input**: User description: "Implement the requirements in docs/social-analytics-requirements with Docker, PostgreSQL, pgAdmin, and the other requested capabilities."

## User Scenarios & Testing

### User Story 1 - View social metrics dashboard (Priority: P1)

As an authenticated dashboard user, I want to see posts and social metrics in a responsive dashboard so I can understand engagement without inspecting raw records.

**Why this priority**: The dashboard is the primary product value and the foundation for all later integrations.

**Independent Test**: Start the application with Docker services, open the dashboard, seed or create posts and metrics, and verify KPI cards, a trend chart, a table, loading state, empty state, and error recovery.

**Acceptance Scenarios**:

1. **Given** no posts exist, **When** the user opens the dashboard, **Then** the UI shows an explicit empty state with a path to import or create data.
2. **Given** posts and metrics exist, **When** the user opens the dashboard, **Then** the UI shows total posts, likes, shares, followers, a time trend, and the latest update time.
3. **Given** metrics change, **When** the server publishes an update, **Then** the dashboard refreshes the affected chart/table data without a full page reload.

### User Story 2 - Manage posts and metrics (Priority: P1)

As a dashboard user, I want CRUD endpoints for posts and metrics so the application has a reliable source of reportable data.

**Why this priority**: Import, scheduled updates, chart data, and export all depend on consistent persisted records.

**Independent Test**: Call the documented REST endpoints with valid and invalid payloads and verify persistence, validation errors, and not-found behavior.

**Acceptance Scenarios**:

1. **Given** a valid post payload, **When** the user creates it, **Then** the post is persisted and returned with an identifier.
2. **Given** a metric payload for an existing post, **When** the user saves it, **Then** the metric is persisted with its source and measurement time.
3. **Given** an invalid or missing identifier, **When** the user requests or changes data, **Then** the API returns a clear client error without changing stored data.

### User Story 3 - Import and export Excel reports (Priority: P1)

As a dashboard user, I want to upload posts from Excel and download a report so I can move data between the dashboard and existing workflows.

**Why this priority**: Excel is explicitly required and is the fastest path to useful data without live social credentials.

**Independent Test**: Upload a valid workbook, verify saved rows and completion feedback, then export a workbook and verify its headers and records.

**Acceptance Scenarios**:

1. **Given** a valid workbook with required columns, **When** the user uploads it, **Then** valid rows are saved and the result reports imported and rejected row counts.
2. **Given** malformed or oversized input, **When** the user uploads it, **Then** the API rejects it with a recoverable message and does not partially import invalid rows.
3. **Given** stored data, **When** the user exports a report, **Then** the downloaded workbook contains the documented post and metric columns.

### User Story 4 - Keep metrics current in the background (Priority: P2)

As a dashboard user, I want scheduled metric updates and visible update times so I can trust the freshness of the report.

**Why this priority**: Background work turns a static report into an analytics application while remaining useful with mock providers.

**Independent Test**: Trigger the update job in a test profile, verify concurrent account processing, persisted `lastUpdated`, logs, and failure isolation.

**Acceptance Scenarios**:

1. **Given** configured social accounts, **When** the scheduled job runs, **Then** metrics are refreshed and each record stores its update time.
2. **Given** one provider update fails, **When** the job processes multiple accounts, **Then** other accounts continue and the failure is visible in logs/status.

### User Story 5 - Secure and integrate the application (Priority: P2)

As an operator, I want secure defaults and local infrastructure so the application can run consistently without committing credentials.

**Why this priority**: Database, messaging, security, and deployment contracts affect every feature and must be reproducible locally.

**Independent Test**: Start Docker Compose from a clean checkout, verify health checks and database connectivity, run the application with no OAuth secrets, and confirm protected write operations reject unauthenticated requests.

**Acceptance Scenarios**:

1. **Given** a clean environment with Docker, **When** the operator starts Compose, **Then** PostgreSQL, pgAdmin, and ActiveMQ are healthy and reachable on documented ports.
2. **Given** no social OAuth credentials, **When** the operator starts the app, **Then** local CRUD/import/export/dashboard flows still work.
3. **Given** an unauthenticated write request, **When** it reaches a protected endpoint, **Then** it is rejected without mutating data.

## Edge Cases

- Duplicate post imports must be idempotent by source and external post identifier when available.
- Excel files with missing headers, invalid dates, duplicate rows, or unsupported types must return row-level diagnostics.
- Empty charts must render a readable message rather than a blank panel.
- WebSocket clients that disconnect must reconnect or fall back to periodic refresh without crashing the page.
- Scheduled updates must not overlap the same account or corrupt counters.
- Queue delivery failures must retry and eventually become visible in the dead-letter queue.
- Provider tokens and database passwords must never appear in logs, exports, or API error bodies.

## Requirements

### Functional Requirements

- **FR-001**: System MUST persist `User`, `Post`, `SocialMetric`, and social-account data in PostgreSQL.
- **FR-002**: System MUST expose CRUD REST endpoints for posts and metrics with validation and documented error responses.
- **FR-003**: System MUST expose `/api/import-posts` for Excel upload and `/api/export-report` for Excel download.
- **FR-004**: System MUST use a single mapping path for Excel rows to entities and entities to export rows, supporting Reflection-based field mapping without exposing arbitrary class loading.
- **FR-005**: System MUST provide `/api/chart-data` with aggregated metrics suitable for a time-series chart.
- **FR-006**: System MUST provide a responsive dashboard with KPI cards, a trend chart, a data table, loading, empty, and error states.
- **FR-007**: System MUST broadcast a metrics-updated event over STOMP WebSocket when persisted metrics change.
- **FR-008**: System MUST run `updateSocialMetricsJob` on a configurable schedule and isolate provider failures per account.
- **FR-009**: System MUST publish `IMPORT_COMPLETED` after a successful import and consume it to refresh aggregate reporting data.
- **FR-010**: System MUST configure retry and dead-letter handling for failed import messages.
- **FR-011**: System MUST provide a SOAP endpoint and a client adapter for a mock exchange-rate service, with an integration test boundary.
- **FR-012**: System MUST support OAuth2 login configuration for Facebook and Twitter without making provider credentials mandatory for local development.
- **FR-013**: System MUST protect state-changing operations with CSRF/session security and never store secrets in source control.
- **FR-014**: System MUST provide OpenAPI documentation for public HTTP endpoints.
- **FR-015**: System MUST provide Docker Compose services for the application dependencies, PostgreSQL, pgAdmin, and ActiveMQ Artemis with health checks.
- **FR-016**: System MUST provide unit tests for services/controllers and integration tests for PostgreSQL, messaging, import/export, WebSocket, and SOAP boundaries that are implemented in the MVP.

### Key Entities

- **User**: A dashboard user with display identity, provider identity, and role.
- **SocialAccount**: A connected Facebook or Twitter account with provider metadata and protected token reference.
- **Post**: A social post with source, external identifier, text/title, published time, and owner/account.
- **SocialMetric**: A point-in-time measurement for a post or account, including likes, shares, comments, followers, and measured time.
- **ImportBatch**: An import attempt with status, counts, filename, and completion timestamp.
- **MetricAggregate**: A derived time bucket used by dashboard charts and refreshed after imports or metric updates.

## Success Criteria

### Measurable Outcomes

- **SC-001**: A new developer can start the local dependencies and open the dashboard using the documented commands in under 10 minutes.
- **SC-002**: A valid workbook of 1,000 rows imports with a result showing imported and rejected counts, without silent data loss.
- **SC-003**: Dashboard users can identify total likes, shares, followers, and the latest update time within 30 seconds of opening the page.
- **SC-004**: At least 95% of valid CRUD, import, export, chart, and background-job contract tests pass in the default verification command.
- **SC-005**: The dashboard remains usable at 375px, 768px, 1024px, and 1440px widths without horizontal scrolling.
- **SC-006**: No automated test or source scan finds committed credentials, access tokens, or database passwords.

## Assumptions

- Version 1 uses Thymeleaf and Chart.js for a server-rendered minimal UI; a separate React application is out of scope unless later requested.
- PostgreSQL is selected from the alternatives in the source requirements; ActiveMQ Artemis is selected for JMS.
- OAuth2 provider credentials and live social API calls are optional in local development; a provider adapter interface and deterministic mock provider are included.
- The first implementation uses session-based authentication with a local development login and leaves production provider credentials to environment configuration.
- Docker Compose is the local deployment target; Kubernetes and cloud deployment are out of scope for this iteration.
- The source image contains requirements, not a complete API schema, field-level privacy policy, or provider-specific rate-limit contract; those are documented as follow-up constraints.
