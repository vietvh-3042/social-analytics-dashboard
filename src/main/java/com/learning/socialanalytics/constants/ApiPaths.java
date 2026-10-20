package com.learning.socialanalytics.constants;

public final class ApiPaths {
    public static final String AUTH = "/api/auth";
    public static final String AUTH_CSRF = "/api/auth/csrf";
    public static final String AUTH_ME = "/api/auth/me";
    public static final String AUTH_LOGOUT = "/api/auth/logout";
    public static final String POSTS = "/api/posts";
    public static final String POSTS_IMPORT = "/import";
    public static final String CRAWL_SETTINGS = "/api/crawl-settings";
    public static final String SYNC_NOW = "/api/sync-now";
    public static final String SYNC_RUNS = "/api/sync-runs";
    public static final String METRICS_HISTORY = "/api/metrics/history";
    public static final String CHART_DATA = "/api/chart-data";
    public static final String REPORT_EXPORT = "/api/reports/export";
    public static final String HEALTH = "/api/health";

    private ApiPaths() {}
}
