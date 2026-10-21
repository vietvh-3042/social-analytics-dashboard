package com.learning.socialanalytics.listener;

import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.SyncMessage;
import com.learning.socialanalytics.service.MetricSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MetricSyncListener {
  private final MetricSyncService syncService;

  @JmsListener(
      destination = ApplicationConstants.METRIC_SYNC_QUEUE,
      concurrency = ApplicationConstants.JMS_LISTENER_CONCURRENCY)
  public void onSyncRequested(SyncMessage message) {
    try {
      syncService.execute(message.runId());
    } catch (RuntimeException exception) {
      log.error("Metric sync run {} failed", message.runId());
      syncService.markFailed(message.runId());
      throw exception;
    }
  }
}
