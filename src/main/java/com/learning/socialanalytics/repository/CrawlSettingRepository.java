package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.CrawlSetting;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlSettingRepository extends JpaRepository<CrawlSetting, UUID> {
    Optional<CrawlSetting> findByAdmin_Id(UUID adminId);

    List<CrawlSetting> findAllByEnabledTrueAndNextRunAtLessThanEqual(Instant now);
}
