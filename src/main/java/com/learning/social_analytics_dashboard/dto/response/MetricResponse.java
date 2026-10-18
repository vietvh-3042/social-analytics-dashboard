package com.learning.social_analytics_dashboard.dto.response;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.entity.SocialMetricEntity;
import java.time.Instant;
import java.util.UUID;

public record MetricResponse(UUID id, UUID postId, long likes, long shares, long comments,
                             long followers, Instant measuredAt, SourcePlatform provider) {
    public static MetricResponse from(SocialMetricEntity metric) {
        return new MetricResponse(metric.getId(), metric.getPostId(), metric.getLikes(),
                metric.getShares(), metric.getComments(), metric.getFollowers(),
                metric.getMeasuredAt(), metric.getProvider());
    }
}
