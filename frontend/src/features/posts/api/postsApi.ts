import { apiClient, loadCsrfToken } from '@/shared/api/apiClient';
import { API_PATHS } from '@/shared/config/constants';
import type { ImportResult, SocialPost } from '@/features/posts/model/types';

export const postsApi = {
  list: async (): Promise<SocialPost[]> => {
    const { data } = await apiClient.get<SocialPost[]>(API_PATHS.posts);
    return data;
  },
  importWorkbook: async (file: File): Promise<ImportResult> => {
    await loadCsrfToken();
    const form = new FormData();
    form.append('file', file);
    const { data } = await apiClient.post<ImportResult>(API_PATHS.postImport, form);
    return data;
  },
};
