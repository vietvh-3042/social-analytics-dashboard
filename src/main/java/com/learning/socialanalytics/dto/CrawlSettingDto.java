package com.learning.socialanalytics.dto;

import java.time.Instant;

public record CrawlSettingDto(
    int intervalMinutes, boolean enabled, Instant lastRunAt, Instant nextRunAt) {}
