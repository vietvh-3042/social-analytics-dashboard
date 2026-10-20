import { apiClient, loadCsrfToken } from '@/shared/api/apiClient';
import { API_PATHS, REPORT_MEDIA_TYPE } from '@/shared/config/constants';
import type { ChartPoint, CrawlSettings, MetricHistory, SyncRun } from '@/features/dashboard/model/types';

export const dashboardApi = {
  settings: async (): Promise<CrawlSettings> => {
    const { data } = await apiClient.get<CrawlSettings>(API_PATHS.crawlSettings);
    return data;
  },
  updateSettings: async (settings: Pick<CrawlSettings, 'intervalMinutes' | 'enabled'>): Promise<CrawlSettings> => {
    await loadCsrfToken();
    const { data } = await apiClient.put<CrawlSettings>(API_PATHS.crawlSettings, settings);
    return data;
  },
  syncNow: async (): Promise<SyncRun> => {
    await loadCsrfToken();
    const { data } = await apiClient.post<SyncRun>(API_PATHS.syncNow);
    return data;
  },
  runs: async (): Promise<SyncRun[]> => {
    const { data } = await apiClient.get<SyncRun[]>(API_PATHS.syncRuns);
    return data;
  },
  history: async (): Promise<MetricHistory[]> => {
    const { data } = await apiClient.get<MetricHistory[]>(API_PATHS.metricHistory);
    return data;
  },
  chartData: async (days: number): Promise<ChartPoint[]> => {
    const { data } = await apiClient.get<ChartPoint[]>(API_PATHS.chartData, { params: { days } });
    return data;
  },
  exportReport: async (): Promise<Blob> => {
    const { data } = await apiClient.get<Blob>(API_PATHS.reportExport, {
      responseType: 'blob',
      headers: { Accept: REPORT_MEDIA_TYPE },
    });
    return data;
  },
};
