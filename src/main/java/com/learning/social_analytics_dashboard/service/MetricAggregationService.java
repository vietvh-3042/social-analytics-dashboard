package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.response.ChartPoint;
import com.learning.social_analytics_dashboard.dto.response.DashboardSummary;
import com.learning.social_analytics_dashboard.entity.SocialMetricEntity;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import com.learning.social_analytics_dashboard.repository.SocialMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricAggregationService implements DashboardQuery {
    private final SocialMetricRepository metricRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ChartPoint> chartData() {
        return metricRepository.findAllByOrderByMeasuredAtAsc().stream()
                .map(metric -> new ChartPoint(metric.getMeasuredAt(), metric.getLikes(), metric.getShares(),
                        metric.getComments(), metric.getFollowers())).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public DashboardSummary summary() {
        List<SocialMetricEntity> metrics = metricRepository.findAllByOrderByMeasuredAtAsc().stream()
                .collect(java.util.stream.Collectors.toMap(SocialMetricEntity::getPostId, metric -> metric,
                        (current, candidate) -> candidate.getMeasuredAt().isAfter(current.getMeasuredAt())
                                ? candidate : current))
                .values().stream().toList();
        long likes = metrics.stream().mapToLong(SocialMetricEntity::getLikes).sum();
        long shares = metrics.stream().mapToLong(SocialMetricEntity::getShares).sum();
        long followers = metrics.stream().mapToLong(SocialMetricEntity::getFollowers).max().orElse(0);
        Instant lastUpdated = metrics.stream().map(SocialMetricEntity::getMeasuredAt).max(Instant::compareTo).orElse(null);
        return new DashboardSummary(postRepository.count(), likes, shares, followers, lastUpdated);
    }
}
