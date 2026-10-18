package com.learning.social_analytics_dashboard.messaging;

import com.learning.social_analytics_dashboard.service.MetricAggregateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImportMessageListener {
    private static final Logger log = LoggerFactory.getLogger(ImportMessageListener.class);
    private final MetricAggregateService aggregateService;

    @JmsListener(destination = ImportMessagePublisher.DESTINATION)
    public void onImportCompleted(ImportCompletedEvent event) {
        aggregateService.refresh();
        log.info("IMPORT_COMPLETED batchId={} importedRows={}", event.batchId(), event.importedRows());
    }
}
