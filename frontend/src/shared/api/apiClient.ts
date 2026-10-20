import axios from 'axios';
import { API_BASE_URL } from '@/shared/config/constants';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  withXSRFToken: true,
  xsrfCookieName: 'XSRF-TOKEN',
  xsrfHeaderName: 'X-XSRF-TOKEN',
  headers: { Accept: 'application/json' },
});

export const loadCsrfToken = async (): Promise<void> => {
  await apiClient.get('/api/auth/csrf');
};
