package com.learning.social_analytics_dashboard.dto.response;

import java.time.Instant;

public record DashboardSummary(long totalPosts, long totalLikes, long totalShares,
                               long totalFollowers, Instant lastUpdated) {
}
