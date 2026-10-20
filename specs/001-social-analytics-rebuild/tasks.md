# Implementation Tasks

## Phase 1 — Foundation

- [ ] Replace starter configuration with environment-based PostgreSQL, Artemis, OAuth, CORS, and logging settings.
- [ ] Add Flyway schema for users, posts, settings, sync runs, and metric snapshots.
- [ ] Scaffold Next.js App Router and install the requested stack with compatible pinned dependencies.
- [ ] Add frontend/backend constants, shared API error shape, and architecture documentation.
- [ ] Add Docker Compose for local PostgreSQL and Artemis.

## Phase 2 — Facebook identity and post import

- [ ] Implement Facebook OAuth login and persist provider subject/profile without storing OAuth tokens.
- [ ] Implement session, CSRF, logout, and authenticated-user endpoints.
- [ ] Implement owner-scoped post listing and validated `.xlsx` import with row-level outcomes.
- [ ] Build login, post import, and post list UI.

## Phase 3 — Metric snapshots

- [ ] Implement persisted per-user interval settings and run status APIs.
- [ ] Implement due-run dispatcher and manual sync command.
- [ ] Generate and append Datafaker snapshots per active owned post.
- [ ] Add JMS job listener, retry/DLQ behavior, and idempotency.

## Phase 4 — Dashboard and realtime

- [ ] Implement aggregate/history endpoints and user-scoped STOMP events.
- [ ] Build responsive analytics dashboard with charts, filters, synthetic-data labels, and sync controls.
- [x] Keep the sidebar focused on Overview while retaining posts and sync controls within the dashboard page.
- [ ] Wire Axios/TanStack Query requests and use Zustand only for transient dashboard state.

## Phase 5 — Excel and SOAP integrations

- [ ] Implement owner-scoped Excel report export.
- [ ] Publish and consume `IMPORT_COMPLETED` using stable message constants.
- [ ] Add typed SOAP demo contract/client or endpoint.
- [ ] Add annotation allow-list for reflection-backed mapping.

## Phase 6 — Delivery

- [ ] Add focused tests for OAuth persistence, ownership, import validation, sync snapshots, and scheduler settings.
- [ ] Run backend compile and frontend lint/typecheck/build; fix failures.
- [ ] Update startup, environment, architecture, roadmap, and changelog documentation.
