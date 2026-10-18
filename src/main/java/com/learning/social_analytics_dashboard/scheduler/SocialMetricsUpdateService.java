package com.learning.social_analytics_dashboard.scheduler;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.integration.social.SocialMetricsProvider;
import com.learning.social_analytics_dashboard.dto.request.MetricRequest;
import com.learning.social_analytics_dashboard.entity.PostEntity;
import com.learning.social_analytics_dashboard.service.MetricUseCase;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SocialMetricsUpdateService {
    private final SocialMetricsProvider provider;
    private final MetricUseCase metricService;

    @Async("metricsExecutor")
    public void update(PostEntity post) {
        SocialMetricsProvider.ProviderMetric metric = provider.fetch(post);
        metricService.add(post.getId(), new MetricRequest(metric.likes(), metric.shares(), metric.comments(),
                metric.followers(), Instant.now(), post.getSource() == SourcePlatform.IMPORT ? SourcePlatform.IMPORT : post.getSource()));
    }
}
