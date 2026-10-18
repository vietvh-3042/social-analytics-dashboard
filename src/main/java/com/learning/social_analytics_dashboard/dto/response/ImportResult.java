package com.learning.social_analytics_dashboard.dto.response;

import java.util.List;
import java.util.UUID;

public record ImportResult(UUID batchId, int importedRows, int rejectedRows, List<String> diagnostics) {
}
