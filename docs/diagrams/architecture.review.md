# Architecture Review

- Nodes: 23
- Edges: 22
- Errors: 0
- Warnings: 10

## Findings

- **WARNING · no-cycles · services -> async -> artemis -> services** — cyclic dependency detected
  - Suggested action: break the cycle or make the dependency asynchronous
- **WARNING · no-cycles · deployment::services -> deployment::async -> deployment::artemis -> deployment::services** — cyclic dependency detected
  - Suggested action: break the cycle or make the dependency asynchronous
- **WARNING · no-cycles · dataflow::services -> dataflow::async -> dataflow::artemis -> dataflow::services** — cyclic dependency detected
  - Suggested action: break the cycle or make the dependency asynchronous
- **WARNING · single-point-of-failure · dataflow::services** — Application services connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
- **WARNING · single-point-of-failure · deployment::integration** — Integration adapters connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
- **WARNING · single-point-of-failure · deployment::services** — Application services connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
- **WARNING · single-point-of-failure · deployment::web** — Web and REST controllers connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
- **WARNING · single-point-of-failure · integration** — Integration adapters connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
- **WARNING · single-point-of-failure · services** — Application services connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
- **WARNING · single-point-of-failure · web** — Web and REST controllers connects otherwise separated parts of the system
  - Suggested action: add redundancy or an alternate path
