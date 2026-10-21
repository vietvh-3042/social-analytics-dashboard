# Code Standards

## Frontend

- Next.js App Router, React, TypeScript, Tailwind CSS v4, and shadcn/ui.
- Organize domain code under `src/features/<feature>/{api,components,hooks,model}`. Shared integration code belongs under `src/shared`.
- Use PascalCase for custom-authored component identifiers and `.tsx` component filenames. Keep Next.js route filenames and generated shadcn component names as required by those tools.
- Use Axios for browser API calls, TanStack Query for remote data, and Zustand only for client-owned UI state.
- Define routes, query keys, limits, defaults, metric labels, and event destinations in shared constants.
- ESLint 9 flat config loads Airbnb 19 through FlatCompat; Prettier handles formatting and Tailwind class sorting.

## Backend

- Java 21 / Spring Boot 3.5, organized strictly package-by-layer.
- Keep REST and SOAP transport code in adapter packages and business behavior in services. Use DTOs at external boundaries and never serialize JPA entities.
- PostgreSQL schema changes go in ordered Flyway SQL migrations. Hibernate uses `validate`, not schema mutation.
- Scope all data reads and writes by the authenticated admin. Keep CSRF enabled and secrets in environment-backed configuration.
- Declare shared route, queue, event, file-limit, and metric values in constants/enums.
- Use Lombok for constructor injection (`@RequiredArgsConstructor`), entity accessors (`@Getter`), protected JPA no-args constructors, and class-based logging (`@Slf4j`). Avoid `@Data` on JPA entities.
- Java formatting uses Spotless with the default Google Java Format style (2-space indentation), unused-import removal, trailing-whitespace cleanup, and a final newline. Run `./gradlew spotlessApply` before review; `check` includes `spotlessCheck`.

## File size and maintainability

Keep source files focused and under 200 lines where practical. Prefer simple service and component composition. Avoid introducing abstractions that have no current caller.
