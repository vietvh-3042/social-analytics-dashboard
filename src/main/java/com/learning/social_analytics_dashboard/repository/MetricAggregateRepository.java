package com.learning.social_analytics_dashboard.repository;

import com.learning.social_analytics_dashboard.entity.MetricAggregateEntity;
import com.learning.social_analytics_dashboard.entity.MetricAggregateId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricAggregateRepository extends JpaRepository<MetricAggregateEntity, MetricAggregateId> {
    List<MetricAggregateEntity> findAllByOrderById_BucketAsc();
}
