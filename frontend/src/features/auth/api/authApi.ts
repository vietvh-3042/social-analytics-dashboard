import { apiClient, loadCsrfToken } from '@/shared/api/apiClient';
import { API_PATHS } from '@/shared/config/constants';
import type { AuthUser, CsrfResponse } from '@/features/auth/model/types';

export const authApi = {
  currentUser: async (): Promise<AuthUser> => {
    const { data } = await apiClient.get<AuthUser>(API_PATHS.currentUser);
    return data;
  },
  csrf: async (): Promise<CsrfResponse> => {
    const { data } = await apiClient.get<CsrfResponse>(API_PATHS.csrf);
    return data;
  },
  logout: async (): Promise<void> => {
    await loadCsrfToken();
    await apiClient.post(API_PATHS.logout);
  },
};
