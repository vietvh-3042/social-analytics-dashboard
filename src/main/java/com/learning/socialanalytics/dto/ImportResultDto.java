package com.learning.socialanalytics.dto;

import java.util.List;

public record ImportResultDto(int imported, int rejected, List<ImportRowResultDto> rows) {}
