package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.request.ExcelPostRow;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelReflectionMapperTest {
    @Test
    void mapsOnlyDeclaredStringFields() {
        ExcelPostRow row = new ExcelReflectionMapper().map(
                Map.of("externalid", "post-1", "content", "Imported post"), ExcelPostRow.class);

        assertThat(row.getExternalId()).isEqualTo("post-1");
        assertThat(row.getContent()).isEqualTo("Imported post");
    }
}
