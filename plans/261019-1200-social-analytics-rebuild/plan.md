---
title: "Social Analytics Rebuild"
description: "Build the Facebook-only analytics dashboard on the existing Java 21 backend and a new Next.js frontend."
status: pending
priority: P1
effort: 48h
branch: master
tags: [feature, frontend, backend, database, api, auth]
blockedBy: []
blocks: []
work_type: feature
spec_draft: plans/261019-1200-social-analytics-rebuild/spec/social-analytics/
created: 2026-10-19
---

# Social Analytics Rebuild

## Overview

Build an admin dashboard that imports Facebook post URLs, stores Facebook-authenticated admins, generates metric snapshots for their existing posts on configured intervals or on demand, and presents/export metrics.

## Cross-Plan Dependencies

None found; no other plans exist in `plans/`.

## Phases

| Phase | Name | Status |
|---|---|---|
| 1 | [Foundation and persistence](./phase-01-foundation-and-persistence.md) | Pending |
| 2 | [Facebook login and post import](./phase-02-facebook-login-and-post-import.md) | Pending |
| 3 | [Metric snapshots and scheduling](./phase-03-metric-snapshots-and-scheduling.md) | Pending |
| 4 | [Dashboard, charts, and realtime](./phase-04-dashboard-charts-and-realtime.md) | Pending |
| 5 | [Excel, JMS, SOAP, and reflection](./phase-05-excel-jms-soap-and-reflection.md) | Pending |
| 6 | [Integration, security, and release readiness](./phase-06-integration-and-release-readiness.md) | Pending |

## Dependencies

- PostgreSQL and Flyway; use the existing Gradle/Spring Boot application and Java 21.
- Facebook OAuth app credentials, redirect URL, and approved scopes must be supplied through environment configuration.
- Frontend runs as a separate Next.js app and calls the backend over a documented API origin.
- Phase order is sequential; phases 2–5 depend on phase 1. Phase 6 depends on all feature phases.

## Decisions and Assumptions

- Facebook is the sole social provider. No X/Twitter login and no local username/password account.
- Successful Facebook OAuth persists the admin identity and the credential required by the configured workflow. Store secrets encrypted at rest; never expose tokens to the browser.
- Metrics are explicitly synthetic: Datafaker creates timestamped snapshots for imported/existing posts. This is not a live Facebook metrics API integration.
- Each admin owns a crawl interval setting. Scheduled runs select due admins; Sync Now runs the same snapshot operation immediately and safely.
- The reference images specify hourly crawling as an example. The final default and allowed interval range need product confirmation before implementation.
- Package-by-layer is strict in Java (`controller`, `service`, `repository`, `entity`, `dto`, `config`, `scheduler`, `listener`, `client`, `exception`, `util`); do not introduce feature-root packages.
- Frontend feature folders use layered separation. Custom `.tsx` component files and exported component names use PascalCase; Next.js special route filenames (`page.tsx`, `layout.tsx`) keep framework-required names.
- Define named constants/enums for API paths, event names, metric keys, supported file limits, destinations, and configuration defaults; avoid repeated string literals.
- SOAP is a bounded integration/demo requirement. Prefer a local Spring-WS endpoint/client fixture rather than an unreliable public service unless a real external SOAP provider is selected.

## Spec-Kit Artifacts

The draft spec in `spec/social-analytics/` is the source for functional and technical requirements. Before implementation, promote it into the repository's canonical feature-spec location and generate/maintain the spec-kit `spec.md`, `plan.md`, and `tasks.md` artifacts from the approved requirements. Keep the phase plan here as the implementation handoff; reconcile identifiers and acceptance criteria during promotion.

## Open Decisions to Resolve Before Coding

- Exact Facebook scopes/use case (profile identity only vs. Page access), provider token retention, and Meta app review status.
- Crawl interval unit/range/default, timezone semantics, and whether administrators may pause scheduled runs.
- Excel template columns, duplicate handling, maximum upload size, and row-level error behavior.
- Metric fields to snapshot (minimum proposal: reactions/likes, comments, shares, followers if applicable) and chart filters/date windows.
- Whether SOAP must call a real third-party service or a local demo endpoint satisfies the educational requirement.
