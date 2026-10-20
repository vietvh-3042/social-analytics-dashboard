# Phase 5 — Excel, JMS, SOAP, and Reflection

## Context Links
- [Draft technical spec](./spec/social-analytics/technical-spec.md)
- [Requirement images 1–4](../../docs/social-analytics-requirements/images/social-analytics-requirements-page-1.png)

## Overview
- Priority: P2 · Status: Pending · Estimate: 8h
- Complete report export and bounded educational integrations called for by the source requirements.

## Requirements and Architecture
- Apache POI export uses stable column names, explicit types, safe cell handling, and ownership-scoped data.
- Publish `IMPORT_COMPLETED` to JMS after import commit; listener handles idempotent aggregate/notification work. Configure retry and DLQ.
- Implement one documented SOAP contract/client or local demo endpoint after target decision.
- Reflection mapper only supports allow-listed model classes and annotated fields; no arbitrary class/method access from request data.

## Implementation Steps
1. Agree export columns, SOAP target and whether JMS follow-up runs in the same deployment.
2. Implement export endpoint and downloadable workbook.
3. Configure Artemis queue, producer, listener, retry/DLQ and event idempotency.
4. Implement constrained reflection-based import/export mapper and tests for rejected fields/types.
5. Implement SOAP DTOs, contract, client/endpoint and timeouts/fault mapping.

## Related Files
- Modify: `build.gradle`, backend `controller`, `service`, `producer`, `listener`, `client`, `config`, `dto`, `exception` packages.
- Create: SOAP contract/config files, migration if import job status needs it, Excel/JMS/SOAP/reflection tests, frontend export/status UI.

## Todo
- [ ] Confirm service endpoint/queue runtime and SOAP target.
- [ ] Verify Excel round trip and unsafe/unallowlisted field rejection.
- [ ] Verify JMS retry, duplicate delivery and DLQ behavior.

## Success Criteria
- Export is valid and scoped; queue consumer handles duplicate delivery safely; SOAP failures map to safe errors.
- Reflection remains internal and allow-listed.

## Risks and Security
- Local Artemis increases setup cost; use the existing starter and one broker configuration.
- Avoid public SOAP demo endpoints with unstable availability or sensitive payloads.

## Next Steps
- Depends on phase 1; may run alongside phase 4 after shared foundation.
