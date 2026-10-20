# Social Analytics Dashboard

**Status:** Ready for implementation  
**Date:** 2026-10-19

## Purpose

Give an administrator a Facebook-authenticated workspace to import posts, collect synthetic engagement history on a schedule or on demand, inspect changes in a dashboard, and export reports.

## User Scenarios

### US-001 — Sign in with Facebook (P1)

An administrator signs in with Facebook and lands in their dashboard.

**Acceptance:** A successful login creates or updates one local user keyed by Facebook's stable subject ID. The application has no local password or X login. The browser receives a secure authenticated session; provider access tokens are not retained because the application does not call Facebook APIs.

### US-002 — Import posts (P1)

An administrator uploads an Excel workbook containing Facebook post URLs and optional labels.

**Acceptance:** Valid, unique rows are saved under the signed-in administrator. Invalid URLs and duplicate posts receive row-level results. An invalid workbook does not partially change the database. Uploads are limited to `.xlsx`, 10 MB, and 5,000 data rows.

### US-003 — Schedule and run metric sync (P1)

An administrator chooses a refresh interval and can start a sync immediately.

**Acceptance:** The interval persists per administrator in whole minutes from 1 through 1,440. The UI allows minutes or hours; all timestamps are stored and displayed in UTC. Scheduled and manual syncs use the same operation. Each successful run appends one Datafaker-generated metric snapshot per active post and records run status and timestamps.

### US-004 — Review analytics (P1)

An administrator reviews post metrics, historical charts, last sync, and sync status.

**Acceptance:** Views contain only the signed-in administrator's data. The dashboard shows likes/reactions, comments, shares, and followers as synthetic metrics. A completed run updates the dashboard through an authenticated realtime event; reconnecting refreshes data through REST. Empty, loading, and error states are communicated.

### US-005 — Export and background processing (P2)

An administrator exports an Excel report; the system publishes and consumes an import-completed message and exposes a documented SOAP demonstration contract.

**Acceptance:** The export contains only owned data and has stable documented columns. Import completion is processed asynchronously and idempotently. SOAP payloads are typed and scoped; reflection-based mapping accepts only explicitly allow-listed model fields.

## Functional Requirements

- **FR-001** Support Facebook OAuth login only and persist the returned user identity after success.
- **FR-002** Enforce administrator ownership for posts, settings, sync runs, snapshots, chart data, exports, and realtime subscriptions.
- **FR-003** Validate and import Facebook post references from Excel with row-level outcomes and bounded upload size.
- **FR-004** Persist per-user sync interval, enabled state, last/next run, and run result.
- **FR-005** Allow a user to trigger an immediate sync; append synthetic metrics for each active owned post.
- **FR-006** Expose REST endpoints for session, posts, import, metrics, chart aggregates, sync settings, sync history, and report export.
- **FR-007** Notify the owning administrator after sync completion over authenticated WebSocket/STOMP.
- **FR-008** Publish `IMPORT_COMPLETED` through JMS and process it asynchronously with retry/DLQ configuration.
- **FR-009** Provide an Excel report and a safe reflection mapper with a fixed allow-list.
- **FR-010** Provide a small, typed SOAP integration demonstration and publish its contract.
- **FR-011** Keep CSRF protection enabled for browser mutations; validate inputs and never leak credentials in responses or logs.
- **FR-012** Mark all generated engagement values as synthetic in the product and documentation.
- **FR-013** Keep the dashboard on one page with a single Overview item in the sidebar. Posts and sync settings remain content sections within that page, not sidebar destinations.

## Non-Functional Requirements

- Schema changes use versioned Flyway migrations; PostgreSQL is the source of truth.
- A failed sync or import reports a safe status and does not expose stack traces or secrets.
- Dashboard layouts support mobile and desktop, keyboard navigation, visible focus, and reduced-motion preferences.
- The frontend uses Next.js/React, Tailwind CSS v4, shadcn/ui, Axios, TanStack Query, Zustand, Prettier, and Airbnb ESLint.
- Frontend code uses feature-based layered architecture. Custom-authored `.tsx` components and their component identifiers are PascalCase; framework route files and shadcn primitives retain their required naming.
- Backend Java code uses package-by-layer organization.
- Repeated API routes, event names, limits, defaults, and metric names are declared as constants/enums.

## Out of Scope

- X/Twitter authentication or data.
- Local username/password accounts.
- Fetching real engagement metrics from Facebook or claiming generated metrics are provider data.
- Arbitrary class/method/field selection through the reflection mapper.

## Assumptions

- Facebook OAuth app credentials and approved callback configuration are supplied through environment variables.
- Facebook profile identity is sufficient; no provider token is stored.
- The reference document's SOAP requirement is met by a local SOAP contract/demo because no real third-party provider was selected.
- A successful import saves all valid rows atomically; duplicates and invalid rows are reported and excluded from the accepted set.
