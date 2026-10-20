package com.learning.socialanalytics.dto;

import java.io.Serializable;
import java.util.UUID;

public record SyncMessage(UUID runId) implements Serializable {
    private static final long serialVersionUID = 1L;
}
