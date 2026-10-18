package com.learning.social_analytics_dashboard.integration.social;

import com.learning.social_analytics_dashboard.entity.PostEntity;

public interface SocialMetricsProvider {
    ProviderMetric fetch(PostEntity post);

    record ProviderMetric(long likes, long shares, long comments, long followers) {
    }
}
