package com.learning.social_analytics_dashboard.messaging;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetricEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public void publishUpdated() {
        messagingTemplate.convertAndSend("/topic/metrics", new MetricsEvent("METRICS_UPDATED", Instant.now()));
    }
}
