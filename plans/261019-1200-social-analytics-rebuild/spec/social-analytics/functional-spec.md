# Social Analytics — Draft Functional Spec

Status: draft. This is a planning artifact, not an approved implementation contract. Resolve the decisions in the parent plan before implementation.

## Purpose

Give Facebook-authenticated admins one dashboard to import post references, track synthetic engagement history, configure per-admin refresh schedules, sync immediately, chart updates in near realtime, and export Excel reports.

## In Scope

- Facebook OAuth login only; persist successful Facebook user identity. No X/Twitter and no local account login.
- Admin-owned posts imported from a validated Excel workbook.
- Per-admin metric crawl interval, scheduled execution, manual Sync Now, run history and last/next-run display.
- Datafaker-generated, timestamped metric snapshots attached to existing posts. Clearly mark values as synthetic; no live provider metrics integration.
- Aggregated charts, realtime update notification via WebSocket, Excel report export, JMS import-completion processing, one SOAP integration/demo, and safe reflection-backed generic import/export mapping.
- CSRF, authentication, authorization, secure token handling, OpenAPI documentation, and automated tests.

## User Stories

### US-001 — Facebook sign-in

As an admin, I sign in with Facebook so I can access my own analytics workspace.

Acceptance: successful OAuth callback upserts one persisted admin by Facebook subject, establishes an authenticated session, and redirects to the dashboard. Provider credentials never appear in browser responses or logs. No alternative local or X login exists.

### US-002 — Import post references

As an admin, I upload a workbook containing Facebook post references so those posts can be tracked.

Acceptance: valid rows create posts owned by the current admin; malformed and duplicate rows return row-level results; upload limits and ownership are enforced; failed parsing cannot corrupt existing data.

### US-003 — Configure and run metric sync

As an admin, I set my refresh interval and can run a sync immediately.

Acceptance: settings persist per admin; scheduled runs pick due admins; Sync Now invokes the same snapshot service; each eligible existing post receives a Datafaker-generated append-only snapshot; run status and last/next timestamps are visible; one admin's settings or failure do not affect another.

### US-004 — Analyze changes

As an admin, I view post metrics and aggregates over time and receive dashboard updates after a sync.

Acceptance: authorized REST requests return owned historical data; charts load aggregates; authenticated WebSocket update events trigger refresh; reconnect falls back to REST; loading, empty and failure states are understandable.

### US-005 — Export and integration behavior

As an admin, I export my report and the system processes import completion asynchronously.

Acceptance: workbook has stable documented columns and only owned data; `IMPORT_COMPLETED` processing is idempotent and has retry/DLQ behavior; SOAP contract is documented; reflection mapping rejects unallowlisted classes and fields.

## Global Acceptance Criteria

- Authenticated and admin-owned behavior is enforced for REST, import, export, scheduler, JMS processing and WebSocket subscriptions.
- CSRF remains enabled for browser session mutations; OAuth state and secure cookie rules are validated.
- PostgreSQL schema is migration-managed; backend and frontend builds and focused tests pass.
- UI uses Next.js, React, Tailwind CSS v4 and shadcn/ui. It follows feature-based layered architecture. Custom `.tsx` components and component names are PascalCase; framework special route filenames retain required conventions.
- Java remains on 21/Spring Boot and strictly package-by-layer. Named constants/enums replace duplicated literals.

## Out of Scope

- X/Twitter account access or login.
- Local password accounts.
- Live Facebook post metrics crawling or any claim that generated data is provider-reported.
- Arbitrary reflection access from user-controlled class, method or field names.
