package com.learning.socialanalytics.dto;

import java.io.Serializable;
import java.util.UUID;

public record ImportCompletedMessage(UUID adminId, int imported) implements Serializable {
  private static final long serialVersionUID = 1L;
}
