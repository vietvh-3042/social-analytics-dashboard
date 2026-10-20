package com.learning.socialanalytics.scheduler;

import com.learning.socialanalytics.service.MetricSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DueSyncScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DueSyncScheduler.class);
    private final MetricSyncService syncService;

    public DueSyncScheduler(MetricSyncService syncService) {
        this.syncService = syncService;
    }

    @Scheduled(fixedDelayString = "60000")
    public void enqueueDueRuns() {
        int queued = syncService.enqueueDueRuns();
        if (queued > 0) {
            LOGGER.info("Queued {} due metric sync runs", queued);
        }
    }
}
