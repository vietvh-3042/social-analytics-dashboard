package com.learning.social_analytics_dashboard.messaging;

import java.time.Instant;

public record MetricsEvent(String event, Instant occurredAt) {
}
