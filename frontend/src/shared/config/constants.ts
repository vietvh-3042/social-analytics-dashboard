export const APPLICATION_NAME = "Social Analytics";
export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";
export const DASHBOARD_SECTIONS = {
  overview: { id: "overview", label: "Overview" },
  posts: { id: "posts", label: "Posts" },
  settings: { id: "settings", label: "Sync settings" },
} as const;
export const DASHBOARD_SECTION_TARGETS = Object.values(DASHBOARD_SECTIONS);
export const API_PATHS = {
  csrf: "/api/auth/csrf",
  currentUser: "/api/auth/me",
  logout: "/api/auth/logout",
  posts: "/api/posts",
  postImport: "/api/posts/import",
  crawlSettings: "/api/crawl-settings",
  syncNow: "/api/sync-now",
  syncRuns: "/api/sync-runs",
  metricHistory: "/api/metrics/history",
  chartData: "/api/chart-data",
  reportExport: "/api/reports/export",
  oauthFacebook: "/oauth2/authorization/facebook",
};

export const QUERY_KEYS = {
  currentUser: ["current-user"],
  posts: ["posts"],
  crawlSettings: ["crawl-settings"],
  syncRuns: ["sync-runs"],
  metricHistory: ["metric-history"],
  chartData: ["chart-data"],
};

export const SYNC_INTERVAL = {
  minimumMinutes: 1,
  maximumMinutes: 1440,
  defaultMinutes: 60,
  defaultUnit: "hours",
};

export const SUPPORTED_IMPORT = {
  extension: ".xlsx",
  maximumSizeBytes: 10 * 1024 * 1024,
  maximumRows: 5000,
  templateHeaders: ["post_url", "title"],
};

export const WEBSOCKET = {
  endpoint: "/ws",
  metricsDestination: "/user/queue/metrics",
};

export const METRIC_LABELS = [
  { key: "likes", label: "Likes", color: "#1e40af" },
  { key: "comments", label: "Comments", color: "#3b82f6" },
  { key: "shares", label: "Shares", color: "#d97706" },
  { key: "followers", label: "Followers", color: "#0f766e" },
] as const;

export const DATE_RANGE_DAYS = 30;
export const RECENT_METRIC_LIMIT = 100;
export const AUTH_STALE_TIME_MS = 60_000;
export const REPORT_FILENAME = "social-analytics-report.xlsx";
export const ERROR_MESSAGES = {
  authenticationRequired: "Please sign in with Facebook to continue.",
  apiUnavailable: "The analytics service is unavailable. Try again shortly.",
  workbookRequired: "Choose an .xlsx workbook to import.",
  syncQueued: "Metric sync queued. New synthetic data will appear shortly.",
};
