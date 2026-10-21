package com.learning.socialanalytics.dto;

import java.time.Instant;
import java.util.UUID;

public record SyncRunDto(
    UUID id,
    String source,
    String status,
    int snapshotsCreated,
    Instant startedAt,
    Instant completedAt,
    String errorSummary) {}
