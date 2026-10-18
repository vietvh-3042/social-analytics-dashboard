package com.learning.social_analytics_dashboard.repository;

import com.learning.social_analytics_dashboard.entity.SocialMetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SocialMetricRepository extends JpaRepository<SocialMetricEntity, UUID> {
    List<SocialMetricEntity> findAllByOrderByMeasuredAtAsc();
    List<SocialMetricEntity> findByPostIdOrderByMeasuredAtDesc(UUID postId);
}
