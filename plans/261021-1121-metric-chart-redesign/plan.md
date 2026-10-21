# Metric Chart Redesign

## Goal

Improve the dashboard metric visualization so single-day and multi-scale data remain readable without changing the API or metric model.

## Status

| Phase | Status | Scope |
| --- | --- | --- |
| 1. Inspect and blueprint | Complete | Read existing chart, DTO, design system, and frontend standards |
| 2. Implement combo chart | Complete | Replace point-heavy line chart with engagement bars plus followers line |
| 3. Validate and review | Complete | Lint, typecheck, production build, and reviewer re-check all pass |
| 4. Sync documentation | Complete | Update architecture, roadmap, and changelog |

## Decisions

- Keep `ChartPointDto` and `/api/chart-data` unchanged.
- Render likes, comments, and shares as grouped rounded bars on the primary axis.
- Render followers as a smooth line on a secondary axis because its magnitude is much larger.
- Keep the existing date-range selector, empty state, tooltip, legend, and synthetic-data disclosure.
- Avoid a new chart library; reuse the installed Chart.js and `react-chartjs-2` dependencies.

## Files

- Modify `frontend/src/features/dashboard/components/MetricTrendChart.tsx`.
- Modify `docs/system-architecture.md`, `docs/development-roadmap.md`, and `docs/project-changelog.md`.

## Success Criteria

- One chart point produces visible bars instead of isolated markers.
- Followers no longer compress engagement series against the zero line.
- Chart remains responsive, keyboard-readable through its accessible label, and uses the existing palette.
- Frontend lint, typecheck, and production build pass.
