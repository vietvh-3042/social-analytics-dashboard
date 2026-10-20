# Phase 1 — Foundation and Persistence

## Context Links
- [Draft technical spec](./spec/social-analytics/technical-spec.md)
- [Requirement image 1](../../docs/social-analytics-requirements/images/social-analytics-requirements-page-1.png)
- [Requirement image 4](../../docs/social-analytics-requirements/images/social-analytics-requirements-page-4.png)

## Overview
- Priority: P1 · Status: Pending · Estimate: 8h
- Establish reproducible backend/frontend foundations, schema and conventions before feature work.

## Requirements and Architecture
- Preserve Spring Boot/Java 21/Gradle. Organize Java strictly by layer under `com.learning.socialanalytics`.
- Add PostgreSQL/Flyway configuration via environment variables; migrations own schema evolution.
- Scaffold Next.js + React + Tailwind v4 + shadcn/ui. Use feature folders with `domain`, `application`, `infrastructure`, `presentation`; shared UI/utilities stay under `src/shared`.
- Custom `.tsx` components use PascalCase filename and exported component name. Retain lowercase Next special route filenames.
- Establish named constants on both sides and API/error response conventions.

## Implementation Steps
1. Confirm repo instructions and current Gradle dependencies; remove no existing feature dependencies without evidence.
2. Add local-development service configuration and secrets-through-environment setup; document startup commands.
3. Create migration-backed tables for admins, credentials, posts, snapshots, crawl settings, sync runs, and import jobs as justified.
4. Scaffold frontend, lint/typecheck/build scripts, API origin configuration, and layered feature skeleton.
5. Add initial health/readiness endpoints and frontend shell.

## Related Files
- Modify: `build.gradle`, `src/main/resources/application.properties`.
- Create: `src/main/resources/db/migration/*`; backend package-by-layer directories; `frontend/**`; local service and setup docs.

## Todo
- [ ] Agree interval, import, metric, OAuth and SOAP open decisions in parent plan.
- [ ] Verify app and database boot from documented commands.
- [ ] Generate and validate spec-kit `spec.md`, plan, and task artifacts before coding feature phases.

## Success Criteria
- Backend and frontend start independently; Flyway creates the schema; no secrets are committed.
- Structure and naming rules are documented and enforced by lightweight checks.

## Risks and Security
- Existing dependency versions are pinned but not verified for compatibility; check before upgrades.
- Never store bearer tokens in plaintext; provision encryption key outside source control.

## Next Steps
- Blocks phases 2–5.
