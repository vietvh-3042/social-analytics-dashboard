package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.SyncRun;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SyncRunRepository extends JpaRepository<SyncRun, UUID> {
  @Query(
      """
            select run
            from SyncRun run
            where run.admin.id = :adminId
            order by run.startedAt desc
            """)
  List<SyncRun> findRecentByAdmin(@Param("adminId") UUID adminId, Pageable pageable);
}
