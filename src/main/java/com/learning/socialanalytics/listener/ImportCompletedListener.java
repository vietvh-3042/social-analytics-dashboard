package com.learning.socialanalytics.listener;

import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.ImportCompletedMessage;
import com.learning.socialanalytics.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportCompletedListener {
  private final AdminUserRepository adminRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @JmsListener(destination = ApplicationConstants.IMPORT_COMPLETED_QUEUE)
  public void onImportCompleted(ImportCompletedMessage event) {
    adminRepository
        .findById(event.adminId())
        .ifPresent(
            admin ->
                messagingTemplate.convertAndSendToUser(
                    admin.getFacebookSubject(),
                    ApplicationConstants.WEBSOCKET_METRICS_DESTINATION,
                    new ImportCompletedResponse(event.imported())));
  }

  public record ImportCompletedResponse(int imported) {}
}
