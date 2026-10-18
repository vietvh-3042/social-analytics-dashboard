package com.learning.social_analytics_dashboard.dto.request;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record PostRequest(
        @NotNull SourcePlatform source,
        @Size(max = 180) String externalId,
        @NotBlank @Size(max = 5000) String content,
        @NotNull Instant publishedAt
) {
}
