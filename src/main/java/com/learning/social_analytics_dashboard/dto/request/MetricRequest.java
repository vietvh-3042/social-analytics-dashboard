package com.learning.social_analytics_dashboard.dto.request;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record MetricRequest(
        @Min(0) long likes,
        @Min(0) long shares,
        @Min(0) long comments,
        @Min(0) long followers,
        @NotNull Instant measuredAt,
        @NotNull SourcePlatform provider
) {
}
