package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.SyncRun;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncRunRepository extends JpaRepository<SyncRun, UUID> {
    List<SyncRun> findTop20ByAdmin_IdOrderByStartedAtDesc(UUID adminId);
}
