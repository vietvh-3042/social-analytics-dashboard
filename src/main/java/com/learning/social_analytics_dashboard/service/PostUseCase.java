package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.request.PostRequest;
import com.learning.social_analytics_dashboard.dto.response.PostResponse;
import com.learning.social_analytics_dashboard.entity.PostEntity;

import java.util.List;
import java.util.UUID;

public interface PostUseCase {
    List<PostResponse> list();
    PostEntity get(UUID id);
    PostResponse create(PostRequest request);
    PostResponse update(UUID id, PostRequest request);
    void delete(UUID id);
}
