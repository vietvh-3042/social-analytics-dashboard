# Research: Social Analytics Dashboard

## Decisions

### Spring Boot version

- **Decision**: Spring Boot 3.5.16 on Java 21.
- **Reason**: It satisfies the source requirement of Spring Boot 3.x, supports Java 21, and keeps the application on the Jakarta/Spring Security 6 generation.
- **Source**: Official Spring Boot 3.5 system requirements and Gradle plugin documentation.

### Persistence

- **Decision**: PostgreSQL 16 in Docker Compose with Flyway migrations.
- **Reason**: The requirements explicitly allow PostgreSQL, it is the production-like relational choice, and migrations make schema changes reproducible.

### Messaging

- **Decision**: ActiveMQ Artemis through Spring JMS.
- **Reason**: It satisfies the JMS requirement without adding a second messaging abstraction; the broker runs locally in Compose.

### UI

- **Decision**: Thymeleaf + Chart.js served by Spring Boot.
- **Reason**: The source requirements make a frontend optional. A single deployable application keeps the first delivery small while still providing the requested dashboard and WebSocket experience.

### UI direction

- **Decision**: Dense analytics dashboard, light-first with dark-compatible tokens, navy/blue data colors and amber highlights, Fira Sans/Fira Code, responsive at 375/768/1024/1440px.
- **Reason**: The UI/UX Pro Max design search matched data-dense dashboards and explicitly called out contrast, visible focus, reduced motion, accessible chart fallbacks, loading states, and recoverable errors.

## Deferred choices

- Live Facebook/Twitter API calls remain behind provider interfaces and a deterministic mock provider until credentials and rate-limit contracts are supplied.
- SOAP uses a local mock endpoint/client contract; a real exchange-rate provider is not part of the initial local environment.
- OAuth2 provider registration is configurable but not required for local startup.
