# Phase 3 — Metric Snapshots and Scheduling

## Context Links
- [Draft technical spec](./spec/social-analytics/technical-spec.md)
- [Requirement image 2](../../docs/social-analytics-requirements/images/social-analytics-requirements-page-2.png)

## Overview
- Priority: P1 · Status: Pending · Estimate: 9h
- Generate append-only Datafaker snapshots for existing posts from manual and per-admin scheduled syncs.

## Requirements and Architecture
- Keep metric generation in a service shared by manual and scheduled triggers.
- Store interval setting and next/last-run metadata per admin. A periodic dispatcher claims due admins and invokes bounded async workers.
- Create one snapshot per eligible existing post; enforce idempotency per sync run/post. Persist run status/counts/errors.
- Datafaker values are synthetic and labeled as such in API/UI. Do not call the Facebook metrics API in this scope.

## Implementation Steps
1. Resolve interval units/range/default, timezone, pause behavior and maximum concurrent admins.
2. Implement settings APIs with validation and ownership checks.
3. Implement Sync Now API and shared snapshot service using Datafaker.
4. Add scheduled due-admin dispatch, bounded executor, per-admin failure isolation and run history.
5. Expose latest/previous snapshots and last/next update time to the UI.

## Related Files
- Modify: layer packages `controller`, `service`, `repository`, `entity`, `dto`, `scheduler`, `config`, `exception`.
- Create: migrations, snapshot/scheduler tests, frontend `features/metrics/**` and `features/crawl-settings/**` components.

## Todo
- [ ] Prove each admin's setting controls only their schedule.
- [ ] Prove Sync Now and scheduler append history and update run metadata.
- [ ] Ensure failure on one admin does not stop remaining due admins.

## Success Criteria
- Manual and scheduled run use the same operation; snapshots are per post and append-only.
- Concurrent/retried execution cannot create duplicate snapshots for the same run/post.

## Risks and Security
- User-defined short intervals can overload the service; enforce approved bounds and concurrency limits.
- Use explicit UTC persistence and convert only at the display boundary after timezone decision.

## Next Steps
- Depends on phases 1–2; updates feed phase 4.
