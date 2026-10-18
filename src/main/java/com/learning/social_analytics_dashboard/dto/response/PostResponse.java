package com.learning.social_analytics_dashboard.dto.response;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.entity.PostEntity;
import java.time.Instant;
import java.util.UUID;

public record PostResponse(UUID id, SourcePlatform source, String externalId,
                           String content, Instant publishedAt, Instant createdAt) {
    public static PostResponse from(PostEntity post) {
        return new PostResponse(post.getId(), post.getSource(), post.getExternalId(),
                post.getContent(), post.getPublishedAt(), post.getCreatedAt());
    }
}
