# Phase 2 — Facebook Login and Post Import

## Context Links
- [Draft technical spec](./spec/social-analytics/technical-spec.md)
- [Requirement images 1–2](../../docs/social-analytics-requirements/images/social-analytics-requirements-page-1.png)

## Overview
- Priority: P1 · Status: Pending · Estimate: 9h
- Deliver Facebook-only admin identity, ownership boundaries, and validated Excel post import.

## Requirements and Architecture
- Spring Security OAuth2 client handles Facebook authorization/callback. Successful login upserts `AdminUser` by provider subject.
- Use server-side session/OAuth tokens, secure cookie settings, CSRF, state verification, and encrypted credential persistence where a token is retained.
- Post/import controllers call services, repositories own persistence, DTOs are separate from entities.
- Apache POI parses a documented workbook template; enforce file size, MIME/extension, row count, URL/provider validation and duplicate policy.
- Frontend login/import components live in feature-layered folders; no local login or X entry points.

## Implementation Steps
1. Finalize OAuth scopes, redirect/cookie/session behavior and credential retention.
2. Configure Facebook client without hardcoded secrets; persist successful user and map to admin principal.
3. Implement ownership-aware post query/create services and constraints.
4. Implement import preview/result contract with row-level errors and idempotent duplicate handling.
5. Add login, import, empty/error states and integration against backend APIs.

## Related Files
- Modify: `build.gradle`, backend `config`, `controller`, `service`, `repository`, `entity`, `dto`, `exception` packages; `application.properties`.
- Create: migration files, Facebook auth/import service tests, frontend `features/auth/**`, `features/posts/**`, custom PascalCase `.tsx` components.

## Todo
- [ ] Persist successful Facebook identity.
- [ ] Add CSRF and admin ownership coverage.
- [ ] Document and validate workbook template and duplicate policy.

## Success Criteria
- Facebook sign-in creates or reuses an admin, redirects to dashboard, and leaks no credential.
- Import result identifies accepted/rejected rows; one admin cannot read another admin's posts.

## Risks and Security
- Facebook scopes and review may block post/page access; import only references and do not claim API access not approved.
- Validate all workbook content; avoid formula execution and unsafe spreadsheet output.

## Next Steps
- Depends on phase 1; feeds metric scheduling and dashboard phases.
