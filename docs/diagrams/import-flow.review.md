# Architecture Review

- Nodes: 7
- Edges: 12
- Errors: 0
- Warnings: 10

## Findings

- **WARNING · no-cycles · act2 -> act0 -> act4 -> act5 -> act2** — cyclic dependency detected
  - Suggested action: break the cycle or make the dependency asynchronous
- **WARNING · no-cycles · act2 -> act0 -> act2** — cyclic dependency detected
  - Suggested action: break the cycle or make the dependency asynchronous
- **WARNING · no-cycles · act2 -> act1 -> act2** — cyclic dependency detected
  - Suggested action: break the cycle or make the dependency asynchronous
- **WARNING · every-service-has-owner · act0** — act0 has no owner
  - Suggested action: set properties.owner
- **WARNING · every-service-has-owner · act1** — act1 has no owner
  - Suggested action: set properties.owner
- **WARNING · every-service-has-owner · act2** — act2 has no owner
  - Suggested action: set properties.owner
- **WARNING · every-service-has-owner · act3** — act3 has no owner
  - Suggested action: set properties.owner
- **WARNING · every-service-has-owner · act4** — act4 has no owner
  - Suggested action: set properties.owner
- **WARNING · every-service-has-owner · act5** — act5 has no owner
  - Suggested action: set properties.owner
- **WARNING · single-point-of-failure · act2** — act2 connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
- **INFO · high-coupling · act2** — act2 has 6 connections
  - Suggested action: verify the component is intentionally a hub
- **INFO · long-synchronous-chain · act0 -> act4 -> act5 -> act2 -> act1 -> act3** — synchronous path spans 6 components
  - Suggested action: verify latency budget, timeouts, and whether an asynchronous boundary is appropriate
- **INFO · long-synchronous-chain · act1 -> act2 -> act0 -> act4 -> act5 -> act3** — synchronous path spans 6 components
  - Suggested action: verify latency budget, timeouts, and whether an asynchronous boundary is appropriate
- **INFO · long-synchronous-chain · user -> act2 -> act0 -> act4 -> act5 -> act3** — synchronous path spans 6 components
  - Suggested action: verify latency budget, timeouts, and whether an asynchronous boundary is appropriate
- **INFO · long-synchronous-chain · act2 -> act0 -> act4 -> act5 -> act3** — synchronous path spans 5 components
  - Suggested action: verify latency budget, timeouts, and whether an asynchronous boundary is appropriate
- **INFO · long-synchronous-chain · act4 -> act5 -> act2 -> act0 -> act3** — synchronous path spans 5 components
  - Suggested action: verify latency budget, timeouts, and whether an asynchronous boundary is appropriate
