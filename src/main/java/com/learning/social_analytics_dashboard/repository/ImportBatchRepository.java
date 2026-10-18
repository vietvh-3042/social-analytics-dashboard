package com.learning.social_analytics_dashboard.repository;

import com.learning.social_analytics_dashboard.entity.ImportBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ImportBatchRepository extends JpaRepository<ImportBatchEntity, UUID> {
}
