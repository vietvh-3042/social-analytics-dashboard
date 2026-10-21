# Development Roadmap

Updated: 2026-10-20

| Phase                             | Status                              | Notes                                                                                                                                          |
| --------------------------------- | ----------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| Foundation and persistence        | Complete                            | PostgreSQL/Flyway schema, environment config, Docker Compose, frontend scaffold, Spotless.                                                     |
| Facebook login and post import    | Implemented; external setup pending | OAuth persistence and owner-scoped XLSX import are in place; valid Meta credentials are required to sign in.                                   |
| Metric snapshots and scheduling   | Implemented; runtime pending        | Manual/scheduled Datafaker snapshots, per-admin intervals, and Artemis consumer; first-time crawl settings creation is covered by PostgreSQL integration tests. |
| Dashboard, charts, and realtime   | Implemented; runtime pending        | Next dashboard, Chart.js combo chart with dual-scale metrics, TanStack Query, user-scoped STOMP listener, and a focused Overview sidebar.       |
| Excel, JMS, SOAP, and reflection  | Implemented; runtime pending        | Report export, import-completion queue, SOAP demo, allow-listed reflection mapper.                                                             |
| Integration and release readiness | In progress                         | Frontend lint/typecheck/build and backend compile pass; real OAuth/broker/database runtime and automated test coverage remain to be exercised. |

## Success measures

- A Facebook-authenticated admin can import posts and only access their own records.
- Manual and scheduled sync append snapshots and refresh the dashboard after commit.
- The frontend lint, typecheck, and production build pass; Java source passes Spotless and compiles.
- Setup and OAuth callback requirements are documented without committing credentials.
