# HTTP Contract: Social Analytics Dashboard

## Posts

- `GET /api/posts` — posts ordered by `publishedAt` descending; the dashboard displays the first ten rows.
- `POST /api/posts` — create a post; requires authenticated user.
- `GET /api/posts/{id}` — retrieve one post.
- `PUT /api/posts/{id}` — update one post; requires authenticated user.
- `DELETE /api/posts/{id}` — delete one post; requires authenticated user.

## Metrics

- `POST /api/posts/{postId}/metrics` — add a point-in-time metric.
- `GET /api/chart-data` — aggregated trend data for the dashboard.
- `GET /api/summary` — current post count, latest per-post likes/shares/followers totals and last update time.

## Excel

- `POST /api/import-posts` — multipart upload with `.xlsx` file; returns batch id, imported rows, rejected rows, and diagnostics.
- `GET /api/export-report` — downloads an `.xlsx` report.
- Import accepts `.xlsx` files up to 10 MB and 10,000 data rows; malformed rows are returned in `diagnostics`.

## Events

- STOMP endpoint: `/ws`.
- Broadcast topic: `/topic/metrics`.
- Event name: `METRICS_UPDATED`.
- JMS event: `IMPORT_COMPLETED`.
- SOAP WSDL: `GET /ws/exchange-rate.wsdl`.

## Error shape

```json
{
  "timestamp": "2026-10-18T00:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "content must not be blank",
  "path": "/api/posts"
}
```
