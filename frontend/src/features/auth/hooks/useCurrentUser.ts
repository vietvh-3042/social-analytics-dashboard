import { useQuery } from '@tanstack/react-query';
import { authApi } from '@/features/auth/api/authApi';
import { AUTH_STALE_TIME_MS, QUERY_KEYS } from '@/shared/config/constants';

export const useCurrentUser = () =>
  useQuery({
    queryKey: QUERY_KEYS.currentUser,
    queryFn: authApi.currentUser,
    staleTime: AUTH_STALE_TIME_MS,
    retry: false,
  });
