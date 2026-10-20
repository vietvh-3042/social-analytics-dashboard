package com.learning.socialanalytics.constants;

public final class ApplicationConstants {
    public static final String API_BASE_PATH = "/api";
    public static final String METRIC_SYNC_QUEUE = "metric-sync";
    public static final String IMPORT_COMPLETED_QUEUE = "import-completed";
    public static final String WEBSOCKET_ENDPOINT = "/ws";
    public static final String WEBSOCKET_METRICS_DESTINATION = "/queue/metrics";
    public static final String SOURCE_MANUAL = "MANUAL";
    public static final String SOURCE_SCHEDULED = "SCHEDULED";
    public static final String STATUS_QUEUED = "QUEUED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    public static final String JMS_LISTENER_CONCURRENCY = "1-4";
    public static final String JMS_QUEUE_PREFIX = "/queue";
    public static final String REPORT_SHEET_NAME = "Synthetic metrics";
    public static final String DEFAULT_FRONTEND_ORIGIN = "http://localhost:3000";
    public static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
    public static final String DEFAULT_SOCIAL_POST_TITLE = "Facebook post";
    public static final String SYNTHETIC_DATA_SOURCE = "Synthetic / Datafaker";
    public static final String XLSX_EXTENSION = ".xlsx";
    public static final String REQUIRED_IMPORT_HEADER = "post_url";
    public static final String XLSX_MEDIA_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String REPORT_FILENAME = "social-analytics-report.xlsx";
    public static final int DEFAULT_CHART_DAYS = 30;
    public static final String DEFAULT_CHART_DAYS_TEXT = "30";
    public static final int DEFAULT_SYNC_INTERVAL_MINUTES = 60;
    public static final int MIN_SYNC_INTERVAL_MINUTES = 1;
    public static final int MAX_SYNC_INTERVAL_MINUTES = 1440;
    public static final long MAX_IMPORT_BYTES = 10 * 1024 * 1024;
    public static final int MAX_IMPORT_ROWS = 5000;

    private ApplicationConstants() {}
}
