package com.learning.social_analytics_dashboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.entity.PostEntity;
import com.learning.social_analytics_dashboard.entity.SocialMetricEntity;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import com.learning.social_analytics_dashboard.repository.SocialMetricRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetricAggregationServiceTest {
    @Mock SocialMetricRepository metricRepository;
    @Mock PostRepository postRepository;
    @InjectMocks MetricAggregationService service;

    @Test
    void summaryUsesLatestMetricPerPost() {
        UUID firstPost = UUID.randomUUID();
        UUID secondPost = UUID.randomUUID();
        when(postRepository.count()).thenReturn(2L);
        when(metricRepository.findAllByOrderByMeasuredAtAsc()).thenReturn(List.of(
                metric(firstPost, 1, 2, 3, 4, "2026-01-01T10:00:00Z"),
                metric(firstPost, 10, 20, 30, 40, "2026-01-01T11:00:00Z"),
                metric(secondPost, 5, 6, 7, 8, "2026-01-01T12:00:00Z")));

        var summary = service.summary();

        assertThat(summary.totalPosts()).isEqualTo(2);
        assertThat(summary.totalLikes()).isEqualTo(15);
        assertThat(summary.totalShares()).isEqualTo(26);
        assertThat(summary.totalFollowers()).isEqualTo(40);
    }

    private SocialMetricEntity metric(UUID postId, long likes, long shares, long comments,
                                      long followers, String measuredAt) {
        return new SocialMetricEntity(postId, likes, shares, comments, followers,
                Instant.parse(measuredAt), SourcePlatform.IMPORT);
    }
}
