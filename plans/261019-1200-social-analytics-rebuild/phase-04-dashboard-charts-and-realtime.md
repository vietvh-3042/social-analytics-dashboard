# Phase 4 — Dashboard, Charts, and Realtime

## Context Links
- [Draft technical spec](./spec/social-analytics/technical-spec.md)
- [Requirement image 3](../../docs/social-analytics-requirements/images/social-analytics-requirements-page-3.png)

## Overview
- Priority: P1 · Status: Pending · Estimate: 8h
- Present owned post/snapshot aggregates and update charts after metrics are generated.

## Requirements and Architecture
- Backend offers paged post/history and aggregate chart-data queries, scoped by authenticated admin.
- Authenticated STOMP/WebSocket subscriptions are admin-scoped; send a compact update event after commit.
- Next.js feature-layered dashboard uses React chart library selected during scaffold (Chart.js meets source requirement); shadcn/ui for controls.
- REST is source of truth. On reconnect, reload REST data; do not rely on socket event replay.

## Implementation Steps
1. Define chart series, date windows, filters, empty/loading/error states and metric units.
2. Implement aggregate queries/API and OpenAPI response examples.
3. Configure authenticated STOMP endpoint and publish event after snapshot transaction commits.
4. Build dashboard, post list, chart and last/next updated time with responsive accessible states.
5. Add reconnect/fallback behavior and verify admin cannot subscribe to another admin's updates.

## Related Files
- Modify: backend `controller`, `service`, `repository`, `config`, `dto` packages.
- Create: frontend `features/dashboard/**`, `features/metrics/**`, shared custom PascalCase `.tsx` components and focused tests.

## Todo
- [ ] Specify metric fields and chart filters.
- [ ] Validate event auth/ownership and reconnect REST fallback.
- [ ] Check keyboard, contrast, responsive layouts and empty/error states.

## Success Criteria
- Aggregate chart loads historical synthetic snapshots and refreshes after authorized metric update.
- WebSocket disconnection leaves dashboard usable and eventually consistent via REST.

## Risks and Security
- Per-admin topic authorization must be explicit; never trust client-supplied admin identifiers.
- Large histories require pagination/date bounds and indexed aggregate filters.

## Next Steps
- Depends on phases 1 and 3.
