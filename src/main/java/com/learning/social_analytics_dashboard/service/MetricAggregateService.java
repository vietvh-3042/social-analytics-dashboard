package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.entity.MetricAggregateEntity;
import com.learning.social_analytics_dashboard.entity.SocialMetricEntity;
import com.learning.social_analytics_dashboard.repository.MetricAggregateRepository;
import com.learning.social_analytics_dashboard.repository.SocialMetricRepository;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetricAggregateService {
    private final SocialMetricRepository metricRepository;
    private final MetricAggregateRepository aggregateRepository;

    @Transactional
    public void refresh() {
        Map<UUID, SocialMetricEntity> latestByPost = new HashMap<>();
        metricRepository.findAll().forEach(metric -> latestByPost.merge(metric.getPostId(), metric,
                (current, candidate) -> candidate.getMeasuredAt().isAfter(current.getMeasuredAt())
                        ? candidate : current));

        Map<AggregateKey, Totals> totals = new HashMap<>();
        latestByPost.values().forEach(metric -> {
            Instant bucket = metric.getMeasuredAt().atZone(ZoneOffset.UTC).toLocalDate()
                    .atStartOfDay(ZoneOffset.UTC).toInstant();
            AggregateKey key = new AggregateKey(bucket, metric.getProvider());
            totals.computeIfAbsent(key, ignored -> new Totals()).add(metric);
        });

        aggregateRepository.deleteAllInBatch();
        aggregateRepository.saveAll(totals.entrySet().stream()
                .map(entry -> new MetricAggregateEntity(entry.getKey().bucket(), entry.getKey().source(),
                        entry.getValue().likes, entry.getValue().shares, entry.getValue().followers))
                .toList());
    }

    private record AggregateKey(Instant bucket, com.learning.social_analytics_dashboard.common.SourcePlatform source) {}

    private static final class Totals {
        private long likes;
        private long shares;
        private long followers;

        private void add(SocialMetricEntity metric) {
            likes += metric.getLikes();
            shares += metric.getShares();
            followers += metric.getFollowers();
        }
    }
}
