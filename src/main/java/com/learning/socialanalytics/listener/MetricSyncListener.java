package com.learning.socialanalytics.listener;

import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.SyncMessage;
import com.learning.socialanalytics.service.MetricSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MetricSyncListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricSyncListener.class);
    private final MetricSyncService syncService;

    public MetricSyncListener(MetricSyncService syncService) {
        this.syncService = syncService;
    }

    @JmsListener(
            destination = ApplicationConstants.METRIC_SYNC_QUEUE,
            concurrency = ApplicationConstants.JMS_LISTENER_CONCURRENCY)
    public void onSyncRequested(SyncMessage message) {
        try {
            syncService.execute(message.runId());
        } catch (RuntimeException exception) {
            LOGGER.error("Metric sync run {} failed", message.runId());
            syncService.markFailed(message.runId());
            throw exception;
        }
    }
}
