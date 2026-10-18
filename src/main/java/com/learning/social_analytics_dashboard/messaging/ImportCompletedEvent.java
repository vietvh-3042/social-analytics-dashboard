package com.learning.social_analytics_dashboard.messaging;

import java.time.Instant;
import java.util.UUID;

public record ImportCompletedEvent(UUID batchId, int importedRows, Instant completedAt) {
}
