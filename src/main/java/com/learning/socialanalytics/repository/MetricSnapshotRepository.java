package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.MetricSnapshot;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MetricSnapshotRepository extends JpaRepository<MetricSnapshot, UUID> {
  @Query(
      """
            select snapshot
            from MetricSnapshot snapshot
            where snapshot.post.owner.id = :ownerId
              and snapshot.capturedAt > :since
            order by snapshot.capturedAt asc
            """)
  List<MetricSnapshot> findForChart(@Param("ownerId") UUID ownerId, @Param("since") Instant since);

  @Query(
      """
            select snapshot
            from MetricSnapshot snapshot
            where snapshot.post.owner.id = :ownerId
            order by snapshot.capturedAt desc
            """)
  List<MetricSnapshot> findRecent(@Param("ownerId") UUID ownerId, Pageable pageable);
}
