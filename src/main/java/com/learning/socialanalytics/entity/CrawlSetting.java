package com.learning.socialanalytics.entity;

import com.learning.socialanalytics.constants.ApplicationConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "crawl_settings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlSetting {
  @Id private UUID adminId;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId
  @JoinColumn(name = "admin_id")
  private AdminUser admin;

  @Column(nullable = false)
  private int intervalMinutes = ApplicationConstants.DEFAULT_SYNC_INTERVAL_MINUTES;

  @Column(nullable = false)
  private boolean enabled = true;

  private Instant lastRunAt;
  private Instant nextRunAt =
      Instant.now().plusSeconds(ApplicationConstants.DEFAULT_SYNC_INTERVAL_MINUTES * 60L);

  public CrawlSetting(AdminUser admin) {
    this.admin = admin;
  }

  public void update(int minutes, boolean isEnabled) {
    intervalMinutes = minutes;
    enabled = isEnabled;
    nextRunAt = isEnabled ? Instant.now().plusSeconds(minutes * 60L) : null;
  }

  public void markRun(Instant now) {
    lastRunAt = now;
    nextRunAt = enabled ? now.plusSeconds(intervalMinutes * 60L) : null;
  }
}
