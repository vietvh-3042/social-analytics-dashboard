package com.learning.social_analytics_dashboard.service;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.entity.MetricAggregateEntity;
import com.learning.social_analytics_dashboard.entity.SocialMetricEntity;
import com.learning.social_analytics_dashboard.repository.MetricAggregateRepository;
import com.learning.social_analytics_dashboard.repository.SocialMetricRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetricAggregateServiceTest {
    @Mock SocialMetricRepository metricRepository;
    @Mock MetricAggregateRepository aggregateRepository;
    @InjectMocks MetricAggregateService service;

    @Test
    void refreshesFromLatestMetricForEachPost() {
        UUID postId = UUID.randomUUID();
        when(metricRepository.findAll()).thenReturn(List.of(
                new SocialMetricEntity(postId, 1, 2, 3, 4,
                        Instant.parse("2026-01-01T10:00:00Z"), SourcePlatform.IMPORT),
                new SocialMetricEntity(postId, 10, 20, 30, 40,
                        Instant.parse("2026-01-01T11:00:00Z"), SourcePlatform.IMPORT)));

        service.refresh();

        verify(aggregateRepository).deleteAllInBatch();
        ArgumentCaptor<List<MetricAggregateEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(aggregateRepository).saveAll(captor.capture());
        MetricAggregateEntity aggregate = captor.getValue().getFirst();
        org.assertj.core.api.Assertions.assertThat(aggregate.getLikes()).isEqualTo(10);
        org.assertj.core.api.Assertions.assertThat(aggregate.getShares()).isEqualTo(20);
        org.assertj.core.api.Assertions.assertThat(aggregate.getFollowers()).isEqualTo(40);
    }
}
