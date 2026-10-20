# Implementation Plan

## Architecture

- **Backend:** Java 21, Spring Boot 3.5, PostgreSQL/Flyway. Strict package-by-layer under `com.learning.socialanalytics`: `config`, `controller`, `dto`, `entity`, `exception`, `listener`, `repository`, `scheduler`, `service`, and `util`. Controllers adapt HTTP/SOAP/WebSocket; services hold business rules.
- **Frontend:** independent `frontend/` Next.js App Router application. Feature-based layered structure: `features/<feature>/{api,components,hooks,model}` plus shared `components`, `lib`, `providers`, and `constants`. Server data cache belongs to TanStack Query; Zustand stores transient UI state.
- **Dashboard navigation:** The sidebar has one Overview destination. Posts and Sync settings remain sections inside the dashboard page and are reached by normal page scrolling; direct fragment links to those sections remain supported.
- **Persistence:** users, posts, per-user sync settings, sync runs, and append-only metric snapshots. Migrations own schema changes.
- **Security:** Facebook OAuth session, CSRF enabled, same-origin/credentialed API setup, user ownership checks for each data operation. No access-token storage.
- **Sync:** DB-backed due-user dispatcher publishes jobs to ActiveMQ Artemis; a listener generates Datafaker snapshots, persists them, and emits a user-scoped STOMP event. Manual sync publishes the same job type.
- **Import/export:** bounded `.xlsx` import with validated columns (`post_url`, optional `title`) and row outcomes; report export with fixed columns.
- **SOAP/reflection:** local typed Spring-WS service; generic mapping restricted to annotated/allow-listed fields.

## Decisions

- Interval: integer minutes, range 1–1,440, default 60; UTC storage/display. UI permits minute/hour input and normalizes to minutes.
- Metrics: likes/reactions, comments, shares, followers; each sync appends a timestamped snapshot for every active post.
- Import: `.xlsx`, max 10 MiB / 5,000 data rows, atomic save of accepted unique rows.
- ESLint: Airbnb's official shared config supports ESLint 7/8. Pin ESLint 8 legacy configuration to meet the requested Airbnb rules.
- Local services: PostgreSQL and Artemis through Docker Compose; credentials remain environment-configured.

## Delivery Phases

1. Foundation: runtime configuration, DB migration, Next scaffold, shared API/security conventions.
2. Identity and posts: Facebook OAuth persistence, session/CSRF endpoints, post import and ownership.
3. Metrics: interval settings, due-run scheduler, JMS job consumer, manual sync, append-only snapshots.
4. Dashboard: chart/summary APIs, responsive UI, STOMP updates and refresh behavior.
5. Exchange/integration: Excel export, import completion events, SOAP contract, safe reflection mapper.
6. Integration: lint/compile/build, focused security and ownership checks, developer documentation.

## Risks and Mitigations

- Meta credentials/callback approval are external prerequisites; document env keys and graceful login errors.
- SameSite/session/CSRF behavior spans origins; configure explicit frontend origin and verify with browser-level integration.
- Scheduler retries can duplicate jobs; persist run IDs and make sync consumers idempotent.
- Synthetic data can be mistaken for real metrics; label it in the dashboard, APIs, and report.
- Airbnb ESLint compatibility requires legacy ESLint 8; pin it rather than silently dropping Airbnb.
