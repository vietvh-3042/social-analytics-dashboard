package com.learning.socialanalytics.scheduler;

import com.learning.socialanalytics.service.MetricSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DueSyncScheduler {
  private final MetricSyncService syncService;

  @Scheduled(fixedDelayString = "60000")
  public void enqueueDueRuns() {
    int queued = syncService.enqueueDueRuns();
    if (queued > 0) {
      log.info("Queued {} due metric sync runs", queued);
    }
  }
}
