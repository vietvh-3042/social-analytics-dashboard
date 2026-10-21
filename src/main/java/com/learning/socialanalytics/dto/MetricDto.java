package com.learning.socialanalytics.dto;

import java.time.Instant;
import java.util.UUID;

public record MetricDto(
    UUID id,
    UUID postId,
    String postTitle,
    long likes,
    long comments,
    long shares,
    long followers,
    Instant capturedAt,
    boolean synthetic) {}
