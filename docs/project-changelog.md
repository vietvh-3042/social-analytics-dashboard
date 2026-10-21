# Project Changelog

## 2026-10-21 — Java formatting style

- Switched Spotless from the AOSP Google Java Format variant to the default Google Java Format style, reducing excessive indentation in nested lambdas and constructor calls across Java source and test files.

## 2026-10-21 — Dashboard metric chart readability

- Replaced the point-heavy metric line chart with grouped engagement bars and a followers trend on a secondary scale, keeping low-volume metrics readable when follower totals are much larger.

## 2026-10-21 — Lombok adoption

- Replaced manual dependency-injection constructors, entity getters/JPA no-args constructors, and logger fields with Lombok annotations across the backend without changing API or persistence behavior.

## 2026-10-21 — Importable report export columns

- Fixed generated report workbooks so `post_url` is the first column and can be uploaded directly through the post import endpoint.

## 2026-10-20 — Report export content negotiation

- Fixed frontend report downloads returning `406 Not Acceptable` by requesting the XLSX media type instead of JSON for `GET /api/reports/export`.

## 2026-10-20 — SPA CSRF token handling

- Fixed Spring Security 6.5 rejecting raw CSRF tokens sent from the frontend cookie in `X-XSRF-TOKEN` on PUT/POST requests.
- Kept BREACH protection for rendered CSRF tokens and added security-chain tests for accepted and missing tokens.

## 2026-10-20 — Crawl settings initialization failure

- Fixed first-time crawl settings creation when the authenticated admin entity is detached by loading a managed reference inside the service transaction.
- Added integration coverage for GET-style creation and PUT-style creation of a missing setting, plus server-side logging for otherwise hidden runtime exceptions.

## 2026-10-20 — Dashboard sidebar navigation

- Kept the sidebar focused on Overview; posts and sync settings remain sections of the dashboard page.
- Preserved direct fragment navigation and reduced-motion-aware scrolling for dashboard sections.
- Removed the ESLint config file from its own ignore list so Next.js can detect its configured plugin during builds.

## 2026-10-19 — Full project rebuild

- Replaced the starter screen with a Next.js 15 / React 19 dashboard using Tailwind CSS v4 and shadcn/ui.
- Added browser API data management through Axios and TanStack Query, transient state through Zustand, Chart.js visualizations, and STOMP updates.
- Added Facebook-only OAuth identity persistence, CSRF/session endpoints, admin-owned posts/settings/snapshots, Excel import/export, and Datafaker sync jobs.
- Added per-admin scheduled sync dispatch, Artemis JMS listeners, an authenticated session-based WebSocket endpoint, a local SOAP demonstration, and an allow-listed reflection mapper.
- Added Flyway schema, local PostgreSQL/Artemis Compose services, project architecture/setup docs, and Gradle Spotless formatting.
- Verified Java formatting/compile and frontend lint/typecheck/build. Runtime login still needs valid Facebook app credentials and local services.
