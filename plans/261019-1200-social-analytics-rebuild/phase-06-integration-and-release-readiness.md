# Phase 6 — Integration and Release Readiness

## Context Links
- [Draft technical spec](./spec/social-analytics/technical-spec.md)
- [Repository Gradle configuration](../../build.gradle)

## Overview
- Priority: P1 · Status: Pending · Estimate: 6h
- Validate the end-to-end flow, API contract, security boundaries, operational setup and final docs.

## Requirements and Architecture
- Run focused unit tests for service rules, repository tests with PostgreSQL, and integrated OAuth/session, import/export, JMS, SOAP, scheduler and WebSocket paths where feasible.
- Keep backend and frontend checks in their respective apps; add one documented end-to-end happy path and key failure paths.
- Update roadmap/changelog/architecture/code standards only where their current project files exist and claims are verified.

## Implementation Steps
1. Run migrations from an empty database and verify repeatable local startup.
2. Exercise login → import → Sync Now → snapshots → chart update → export.
3. Verify scheduled per-admin isolation, CSRF, unauthorized access, token redaction and WebSocket authorization.
4. Run backend/frontend compile, typecheck, lint and scoped tests; address failures on final code.
5. Publish API/OAuth/setup documentation, known synthetic-data limitation, and operational queue/scheduler guidance.

## Related Files
- Modify: tests across touched layers, OpenAPI/security config, project docs if present.
- Create: integration tests, setup and feature documentation, environment example without secrets.

## Todo
- [ ] Trace each SC acceptance criterion to executable verification.
- [ ] Confirm logs and actuator expose no secrets or personal data beyond intended fields.
- [ ] Reconcile spec, plan, tasks, roadmap and changelog.

## Success Criteria
- Documented end-to-end flow passes; no cross-admin data leak; no credential appears in logs or API responses.
- Spec-kit artifacts and project docs describe the implemented behavior accurately.

## Risks and Security
- OAuth provider review/configuration can block a live demo; document required app configuration and local callback setup.
- Test external integrations at their contract boundary; avoid making CI depend on third-party services.

## Next Steps
- Complete rollout only after the approved spec decisions and implementation review.
