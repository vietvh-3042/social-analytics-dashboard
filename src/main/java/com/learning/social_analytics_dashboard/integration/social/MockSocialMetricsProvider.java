package com.learning.social_analytics_dashboard.integration.social;

import com.learning.social_analytics_dashboard.entity.PostEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!production")
public class MockSocialMetricsProvider implements SocialMetricsProvider {
    @Override
    public ProviderMetric fetch(PostEntity post) {
        long seed = Math.abs((long) post.getContent().hashCode());
        return new ProviderMetric(seed % 1000, seed % 300, seed % 100, 1000 + seed % 10000);
    }
}
