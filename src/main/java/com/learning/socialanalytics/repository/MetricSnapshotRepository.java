package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.MetricSnapshot;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricSnapshotRepository extends JpaRepository<MetricSnapshot, UUID> {
    List<MetricSnapshot> findAllByPost_Owner_IdAndCapturedAtAfterOrderByCapturedAtAsc(
            UUID ownerId, Instant since);

    List<MetricSnapshot> findTop100ByPost_Owner_IdOrderByCapturedAtDesc(UUID ownerId);
}
