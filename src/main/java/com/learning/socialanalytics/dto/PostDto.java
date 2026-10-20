package com.learning.socialanalytics.dto;

import java.time.Instant;
import java.util.UUID;

public record PostDto(UUID id, String title, String postUrl, Instant importedAt) {}
