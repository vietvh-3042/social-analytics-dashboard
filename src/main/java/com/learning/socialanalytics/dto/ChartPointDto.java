package com.learning.socialanalytics.dto;

import java.time.Instant;

public record ChartPointDto(
        Instant capturedAt, long likes, long comments, long shares, long followers) {}
