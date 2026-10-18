package com.learning.social_analytics_dashboard.repository;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import com.learning.social_analytics_dashboard.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    List<PostEntity> findAllByOrderByPublishedAtDesc();
    Optional<PostEntity> findBySourceAndExternalId(SourcePlatform source, String externalId);
}
