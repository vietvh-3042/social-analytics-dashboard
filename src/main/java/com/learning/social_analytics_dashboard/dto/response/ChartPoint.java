package com.learning.social_analytics_dashboard.dto.response;

import java.time.Instant;

public record ChartPoint(Instant measuredAt, long likes, long shares, long comments, long followers) {
}
