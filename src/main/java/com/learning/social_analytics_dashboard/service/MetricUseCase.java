package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.request.MetricRequest;
import com.learning.social_analytics_dashboard.dto.response.MetricResponse;

import java.util.List;
import java.util.UUID;

public interface MetricUseCase {
    MetricResponse add(UUID postId, MetricRequest request);
    List<MetricResponse> listForPost(UUID postId);
}
