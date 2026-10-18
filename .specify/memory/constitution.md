# Social Analytics Dashboard Constitution

## Core Principles

### I. Working Software First
Every slice must run through the real application path. No fake repositories, placeholder endpoints, or mock-only implementations may be presented as complete functionality.

### II. Contract and Data Integrity
HTTP, messaging, persistence, import/export, and WebSocket contracts must be explicit, validated at boundaries, and covered by the smallest reliable tests.

### III. Secure Defaults
Secrets stay in environment variables, credentials are never committed, user data is scoped to authenticated users, and error responses do not leak tokens or infrastructure details.

### IV. Observable Asynchronous Work
Scheduled jobs, threads, queues, retries, dead-letter handling, and WebSocket broadcasts must emit actionable logs and expose failure states that users can recover from.

### V. Simple, Accessible Delivery
Prefer the smallest design that meets the requirements. UI must support keyboard use, readable contrast, responsive layouts, explicit loading/empty/error states, and reduced motion.

## Technical Constraints

- Java 21 and Spring Boot 3.5.x.
- PostgreSQL is the source of truth for application data.
- Local development uses Docker Compose with PostgreSQL, pgAdmin, and ActiveMQ Artemis.
- OAuth providers and external social APIs are optional integrations; the application must run without provider credentials.
- Docker health checks, environment-based configuration, and a non-secret `.env.example` are required.

## Development Workflow

- Every feature starts from `spec.md`, `plan.md`, and `tasks.md`.
- Compile and test the changed surface before delivery; safety-critical changes require the wider suite.
- Keep Java source files under 200 lines where practical and split by responsibility.
- Update project documentation when architecture, deployment, or public contracts change.

## Governance
<!-- Example: Constitution supersedes all other practices; Amendments require documentation, approval, migration plan -->

The constitution is the quality floor for this project. Any exception must be recorded in the feature plan with the reason, impact, and follow-up. Simplicity, security, and evidence take precedence over speculative extensibility.

**Version**: 1.0.0 | **Ratified**: 2026-10-18 | **Last Amended**: 2026-10-18
