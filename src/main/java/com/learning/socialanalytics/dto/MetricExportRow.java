package com.learning.socialanalytics.dto;

import com.learning.socialanalytics.util.SpreadsheetColumn;

public class MetricExportRow {
    @SpreadsheetColumn(value = "post_url", order = 1)
    private final String postUrl;

    @SpreadsheetColumn(value = "post_title", order = 2)
    private final String postTitle;

    @SpreadsheetColumn(value = "likes", order = 3)
    private final long likes;

    @SpreadsheetColumn(value = "comments", order = 4)
    private final long comments;

    @SpreadsheetColumn(value = "shares", order = 5)
    private final long shares;

    @SpreadsheetColumn(value = "followers", order = 6)
    private final long followers;

    @SpreadsheetColumn(value = "captured_at_utc", order = 7)
    private final String capturedAt;

    @SpreadsheetColumn(value = "data_source", order = 8)
    private final String dataSource;

    public MetricExportRow(
            String postTitle,
            String postUrl,
            long likes,
            long comments,
            long shares,
            long followers,
            String capturedAt,
            String dataSource) {
        this.postTitle = postTitle;
        this.postUrl = postUrl;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
        this.followers = followers;
        this.capturedAt = capturedAt;
        this.dataSource = dataSource;
    }
}
