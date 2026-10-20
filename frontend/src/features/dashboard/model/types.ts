export type CrawlSettings = {
  intervalMinutes: number;
  enabled: boolean;
  lastRunAt: string | null;
  nextRunAt: string | null;
};

export type MetricHistory = {
  id: string;
  postId: string;
  postTitle: string;
  likes: number;
  comments: number;
  shares: number;
  followers: number;
  capturedAt: string;
  synthetic: boolean;
};

export type ChartPoint = {
  capturedAt: string;
  likes: number;
  comments: number;
  shares: number;
  followers: number;
};

export type SyncRun = {
  id: string;
  source: 'MANUAL' | 'SCHEDULED';
  status: 'QUEUED' | 'COMPLETED' | 'FAILED';
  snapshotsCreated: number;
  startedAt: string;
  completedAt: string | null;
  errorSummary: string | null;
};
