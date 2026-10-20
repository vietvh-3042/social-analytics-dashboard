# Backend implementation research (2026-10-19)

## Scope and repository baseline

The repository uses Spring Boot `3.5.16` with a Java 21 toolchain. Its Gradle dependencies already include MVC, JPA, Validation, Spring Security OAuth2 Client, WebSocket, Spring-WS, Artemis, Flyway core + PostgreSQL support, Apache POI OOXML, and PostgreSQL JDBC. So the requested backend capabilities are already represented in the dependency set; implementation should use Boot-managed versions where available and avoid adding duplicate starters.

Boot 3.5.16 supports Java 17 through 25, so Java 21 is supported. [Boot 3.5 system requirements](https://docs.spring.io/spring-boot/3.5/system-requirements.html) [Boot 3.5 managed versions](https://docs.spring.io/spring-boot/3.5/appendix/dependency-versions/coordinates.html)

## Recommendations

### Application layout

Keep this as a modular monolith organized by business feature (for example, `auth`, `accounts`, `analytics`, `reports`, `notifications`), and use a small, consistent internal split per feature: web/API, application/service, persistence, and domain/model as needed. Put cross-cutting framework configuration and external adapters in clearly named `config` and `integration` packages. Keep controllers and SOAP endpoints as transport adapters; put business rules in services. Place the main application class in the existing root package so component scanning naturally covers the application. This is a pragmatic interpretation of Spring Boot’s recommendation to put the main class in a root package; Boot does not mandate a particular layout. [Spring Boot: structuring code](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html)

### Facebook OAuth login, browser session, and CSRF

Use Spring Security OAuth2 Login (`oauth2Login`) and the existing `spring-boot-starter-oauth2-client`. Spring Security 6.5 lists Facebook among its `CommonOAuth2Provider` defaults, so a `facebook` registration can usually be configured with client ID/secret and the provider’s approved redirect URI, following the default `{baseUrl}/login/oauth2/code/{registrationId}` template. Keep credentials in environment-backed configuration, use a stable public HTTPS base URL behind proxies, and add a `userService`/success handler to map the provider identity to an internal user. Treat Facebook’s returned stable provider ID as the external identity key; do not use email as the sole account-linking key. If the product later calls Facebook APIs, request only needed scopes and persist/refresh authorized-client tokens deliberately rather than equating login with API access. [Spring Security 6.5 OAuth2 Login](https://docs.spring.io/spring-security/reference/6.5/servlet/oauth2/login/core.html)

Use a normal cookie-backed server session for this browser login flow unless the product has a specific stateless API requirement. Keep CSRF enabled for unsafe HTTP methods; it is on by default and Spring Security’s default token repository stores the expected token in the session. For a JavaScript frontend, implement the documented SPA token flow and refresh the token after login/logout; do not solve integration friction by globally disabling CSRF. Set secure cookie attributes in HTTPS deployments and rely on Spring Security’s session fixation defense. [CSRF protection](https://docs.spring.io/spring-security/reference/6.5/servlet/exploits/csrf.html) [Session management](https://docs.spring.io/spring-security/reference/6.5/servlet/authentication/session-management.html)

### Per-user periodic analytics

Persist each user’s enabled interval, next due instant, and last/next execution state in PostgreSQL. Use one bounded dispatcher driven by Spring `TaskScheduler` or a modest fixed-delay `@Scheduled` poll; claim a batch of due rows transactionally, enqueue/execute work, then update run state. Enforce a minimum and maximum interval, define timezone/clock semantics explicitly, and prevent duplicate claims with a database-level locking/claim strategy. Make jobs idempotent and put uniqueness constraints on the logical result window. Spring’s `TaskScheduler` supports trigger-driven dynamic schedules, but in-memory per-user scheduled futures alone lose their schedules on restart and can run twice across replicas; the persisted-dispatcher recommendation is an architectural inference for this app’s per-user, durable workload. [Spring scheduling and `TaskScheduler`](https://docs.spring.io/spring-framework/reference/6.2/integration/scheduling.html) [PostgreSQL constraints](https://www.postgresql.org/docs/current/ddl-constraints.html)

### JMS / ActiveMQ Artemis

Use the current `spring-boot-starter-artemis` dependency and Boot’s auto-configured `JmsTemplate` for publishing jobs plus `@JmsListener` consumers. Configure native broker mode and credentials externally outside tests; use a separate embedded broker only in local/test profiles. Define destinations explicitly, bound listener concurrency, and use transactional/idempotent consumers because delivery and retries can repeat work. If DB state changes and message publication must be atomic, use a transactional outbox rather than assuming a PostgreSQL transaction and a broker send commit together (architecture recommendation). Boot documents Artemis modes, `JmsTemplate`, listener setup, and listener transaction behavior. [Spring Boot 3.5 JMS](https://docs.spring.io/spring-boot/3.5/reference/messaging/jms.html)

### STOMP over WebSocket

Use STOMP over Spring WebSocket only for live client updates (for example analytics job progress and completed-report notifications); PostgreSQL remains the durable source of truth. Configure `/app` for inbound application handlers and `/topic`/`/queue` for broker destinations. For user-specific updates, publish to `/user/queue/...`, and authorize both inbound `MESSAGE` and `SUBSCRIBE` destinations with Spring Security’s message authorization. The normal authenticated HTTP session principal is associated with the WebSocket session; do not trust STOMP `login`/`passcode` headers for browser authentication. The built-in simple broker is in-memory and appropriate for one instance; use an external STOMP broker relay when multi-instance fan-out is required. [STOMP setup and broker choices](https://docs.spring.io/spring-framework/reference/6.2/web/websocket/stomp.html) [STOMP authentication](https://docs.spring.io/spring-framework/reference/6.2/web/websocket/stomp/authentication.html) [STOMP user destinations](https://docs.spring.io/spring-framework/reference/6.2/web/websocket/stomp/user-destination.html) [WebSocket authorization](https://docs.spring.io/spring-security/reference/6.5/servlet/integrations/websocket.html)

### XLSX export

Use the already-present `poi-ooxml` dependency. For ordinary-sized reports, POI’s XSSF user model is simpler. For large exports, use `SXSSFWorkbook` to cap in-memory rows, and close/dispose workbook resources and temporary files after writing. The POI guide notes SXSSF’s sliding row window and limitations (such as formula evaluation). Stream the HTTP response where practical and enforce export row/size limits. [Apache POI spreadsheet guide](https://poi.apache.org/components/spreadsheet/quick-guide.html) [Apache POI spreadsheet APIs](https://poi.apache.org/components/spreadsheet/)

### SOAP integrations

Use the existing Spring-WS starter for the required SOAP contract. Prefer contract-first XSD/WSDL and typed JAXB payloads; expose focused `@Endpoint` methods mapped by namespace + local payload root (`@PayloadRoot`), delegate immediately to an application service, and translate domain failures to stable SOAP faults. Keep SOAP endpoints separate from REST controllers and share application services rather than transport DTOs. Spring-WS describes endpoints as transport adapters that map incoming XML payloads to service behavior. [Spring-WS reference](https://docs.spring.io/spring-ws/docs/current/reference/html/)

### PostgreSQL and Flyway

Keep PostgreSQL as the system of record. Use Spring Data JPA for aggregates and repositories, with explicit indexes/unique constraints/FKs derived from actual access patterns and ownership rules. Add schema changes as ordered, versioned SQL migrations under `src/main/resources/db/migration`; never use Hibernate `ddl-auto=update` as the schema change process. Flyway tracks applied versions/checksums; once a migration is applied to a shared/permanent environment, add a forward migration rather than editing it. Keep Boot-managed Flyway and JDBC driver versions. [Spring Boot 3.5 SQL database configuration](https://docs.spring.io/spring-boot/3.5/reference/data/sql.html) [Flyway versioned migrations](https://documentation.red-gate.com/fd/versioned-migrations-273973333.html) [PostgreSQL constraints](https://www.postgresql.org/docs/current/ddl-constraints.html)

## Practical sequencing

1. Define PostgreSQL schema and Flyway migrations for users/provider identities, user-owned analytics configuration, runs/results, and report metadata.
2. Implement application services and REST/SOAP adapters around the same service contracts.
3. Add Facebook login, internal-account mapping, authorization rules, CSRF-aware browser integration, and tests around the security boundary.
4. Add the persisted due-work dispatcher, Artemis job messages/consumers, retry/idempotency, then STOMP user notifications.
5. Add XLSX export from persisted report data; use SXSSF only for exports that exceed the normal in-memory size envelope.

## Constraints and open implementation checks

- The current repository has only the application entry point and no established feature package pattern yet; use the root package `com.learning.socialanalytics` and keep the initial package structure small.
- Exact Facebook permissions, API version, callback URL, and app mode depend on the product’s Meta app configuration. Confirm these with the deployed app setup; do not hard-code a Graph API version based solely on Spring defaults.
- A single application instance can run the DB-backed dispatcher without distributed locks. Multiple replicas require an atomic row-claim/lease strategy and clear stale-claim recovery.
- Verify the chosen production broker topology supports the needed STOMP relay or use WebSocket local fan-out backed by durable JMS/DB events; JMS Artemis and STOMP broker relay are separate broker protocols/configuration concerns.
