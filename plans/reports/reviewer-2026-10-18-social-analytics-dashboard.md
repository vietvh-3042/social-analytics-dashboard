# Review report — Social Analytics Dashboard

## Scope

Reviewed the Spring Boot layered boundaries, Flyway schema strategy, Docker services, security defaults, import/export flow and dashboard assets.

## Findings addressed

- Added `SocialAccountEntity` and repository with explicit column mappings to the single V1 schema.
- Removed the second Flyway migration; V1 now contains `connected_at`.
- Hardened SOAP document factory settings against external entities.
- Kept controller → service → repository flow and isolated external integrations under integration/messaging packages.
- Fixed browser CSRF flow to use the token returned by `/api/csrf`, which is required by the cookie repository flow.
- Replaced wildcard WebSocket origins with the configurable `APP_ALLOWED_ORIGINS` allowlist.
- Converted all JPA entities to controlled Lombok getters/protected no-args constructors without using `@Data`.
- Added unit tests for post creation/deletion and constrained Excel reflection mapping.
- Added metric aggregate persistence with a versioned V2 migration and latest-metric KPI calculation.
- Added Excel extension, size and row-count validation.
- Added conditional Facebook/Twitter OAuth2 registrations with one repository for both providers.
- Added SOAP client and MockWebServiceClient boundary test.
- Added Testcontainers smoke coverage for PostgreSQL and Artemis.
- Added editable architecture, deployment, ERD and import-flow diagrams with structural validation.
- Fixed `/swagger-ui.html` security matcher so the documented Swagger URL is reachable.
- Hardened SOAP response transformation against external DTD and stylesheet access.

## Remaining scope

- Real Facebook/Twitter OAuth credentials and provider API adapters remain configuration/integration work.
- Artemis broker-specific max-redelivery/DLQ policy, broad REST/dashboard/JMS/WebSocket contract coverage and browser E2E remain tracked as open tasks.

## Verification evidence

- `./gradlew check --no-daemon` passed; the final test run passed 9 tests, with PostgreSQL available for the Spring context.
- A second context run with both Facebook and Twitter client IDs passed.
- Docker Compose rebuilt and started the app image successfully.
- PostgreSQL, pgAdmin, Artemis and app containers are healthy; `/actuator/health` returned `UP`.
- `/swagger-ui.html` followed to HTTP 200, `/ws/exchange-rate.wsdl` returned HTTP 200, and dashboard summary endpoints returned HTTP 200.
- All four `.drawio` files validated with 0 errors and 0 warnings.
- SunLint was unavailable in the environment (`sunlint: command not found`), so no SunLint result is claimed.
