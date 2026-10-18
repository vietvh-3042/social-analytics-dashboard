package com.learning.social_analytics_dashboard.dto.request;

public class ExcelPostRow {
    private String externalId;
    private String content;
    private String publishedAt;

    public ExcelPostRow() {
    }

    public String getExternalId() { return externalId; }
    public String getContent() { return content; }
    public String getPublishedAt() { return publishedAt; }
}
