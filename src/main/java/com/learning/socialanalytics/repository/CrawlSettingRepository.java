package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.CrawlSetting;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CrawlSettingRepository extends JpaRepository<CrawlSetting, UUID> {
  @Query(
      """
            select setting
            from CrawlSetting setting
            where setting.admin.id = :adminId
            """)
  Optional<CrawlSetting> findByAdminId(@Param("adminId") UUID adminId);

  @Query(
      """
            select setting
            from CrawlSetting setting
            where setting.enabled = true
              and setting.nextRunAt <= :now
            """)
  List<CrawlSetting> findDue(@Param("now") Instant now);
}
