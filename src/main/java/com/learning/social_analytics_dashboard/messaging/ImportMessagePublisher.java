package com.learning.social_analytics_dashboard.messaging;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImportMessagePublisher {
    public static final String DESTINATION = "import.completed";
    private final JmsTemplate jmsTemplate;

    public void publish(ImportCompletedEvent event) {
        jmsTemplate.convertAndSend(DESTINATION, event);
    }
}
