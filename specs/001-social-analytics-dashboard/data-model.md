# Data Model: Social Analytics Dashboard

## User

| Field | Type | Constraints |
| --- | --- | --- |
| id | UUID | Primary key |
| username | string | Required, unique, max 120 |
| displayName | string | Required, max 160 |
| role | enum | `USER` or `ADMIN`, required |
| createdAt | instant | Required |

## SocialAccount

| Field | Type | Constraints |
| --- | --- | --- |
| id | UUID | Primary key |
| userId | UUID | Required foreign key to User |
| provider | enum | `FACEBOOK` or `TWITTER`, required |
| externalId | string | Required, max 180 |
| accessTokenReference | string | Optional, never returned in API |
| lastSyncedAt | instant | Optional |

## Post

| Field | Type | Constraints |
| --- | --- | --- |
| id | UUID | Primary key |
| socialAccountId | UUID | Optional foreign key |
| source | enum | `FACEBOOK`, `TWITTER`, or `IMPORT`, required |
| externalId | string | Optional, unique with source when present |
| content | string | Required, max 5000 |
| publishedAt | instant | Required |
| createdAt | instant | Required |

## SocialMetric

| Field | Type | Constraints |
| --- | --- | --- |
| id | UUID | Primary key |
| postId | UUID | Required foreign key to Post |
| likes | long | Required, >= 0 |
| shares | long | Required, >= 0 |
| comments | long | Required, >= 0 |
| followers | long | Required, >= 0 |
| measuredAt | instant | Required |
| provider | enum | Required |

## ImportBatch

| Field | Type | Constraints |
| --- | --- | --- |
| id | UUID | Primary key |
| filename | string | Required, max 255 |
| status | enum | `PROCESSING`, `COMPLETED`, `FAILED` |
| importedRows | integer | Required, >= 0 |
| rejectedRows | integer | Required, >= 0 |
| completedAt | instant | Optional |

## MetricAggregate

| Field | Type | Constraints |
| --- | --- | --- |
| bucket | instant | Composite key with source |
| source | enum | Composite key with bucket |
| likes | long | Required, >= 0 |
| shares | long | Required, >= 0 |
| followers | long | Required, >= 0 |

## Relationships

- User 1:N SocialAccount.
- SocialAccount 1:N Post.
- Post 1:N SocialMetric.
- ImportBatch records one import attempt and is independent from individual posts for auditability.
- MetricAggregate is derived and can be rebuilt from SocialMetric records.
