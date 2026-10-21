# Metric Snapshot Query Refactor

## Goal

Shorten long derived-query methods across the backend repository package while preserving owner scoping, filtering, ordering, and result limits.

## Plan

1. Replace long derived-query names with concise repository methods backed by explicit JPQL `@Query` declarations.
2. Pass existing history/run limits through `Pageable` from service callers.
3. Update all callers and tests without changing API or persistence behavior.
4. Format the Java sources, compile the backend, and run the available tests.
5. Review the final diff for behavior or documentation impact.

## Success Criteria

- Chart history remains owner-scoped and ascending after the requested timestamp.
- Recent history and sync-run reads remain owner-scoped and descending, with their existing limits.
- Post listing, active-post filtering, duplicate checks, and due-setting selection preserve behavior.
- Backend formatting, compilation, and tests pass.
