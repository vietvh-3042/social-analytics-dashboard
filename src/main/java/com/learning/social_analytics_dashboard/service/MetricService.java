package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.request.MetricRequest;
import com.learning.social_analytics_dashboard.dto.response.MetricResponse;
import com.learning.social_analytics_dashboard.entity.SocialMetricEntity;
import com.learning.social_analytics_dashboard.exception.NotFoundException;
import com.learning.social_analytics_dashboard.messaging.MetricEventPublisher;
import com.learning.social_analytics_dashboard.repository.PostRepository;
import com.learning.social_analytics_dashboard.repository.SocialMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetricService implements MetricUseCase {
    private final SocialMetricRepository metricRepository;
    private final PostRepository postRepository;
    private final MetricEventPublisher eventPublisher;
    private final MetricAggregateService aggregateService;

    @Transactional
    @Override
    public MetricResponse add(UUID postId, MetricRequest request) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found: " + postId);
        }
        SocialMetricEntity metric = new SocialMetricEntity(postId, request.likes(), request.shares(),
                request.comments(), request.followers(), request.measuredAt(), request.provider());
        MetricResponse response = MetricResponse.from(metricRepository.save(metric));
        aggregateService.refresh();
        eventPublisher.publishUpdated();
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MetricResponse> listForPost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found: " + postId);
        }
        return metricRepository.findByPostIdOrderByMeasuredAtDesc(postId).stream()
                .map(MetricResponse::from).toList();
    }
}
