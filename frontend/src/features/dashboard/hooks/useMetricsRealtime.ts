'use client';

import { useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useQueryClient } from '@tanstack/react-query';
import { API_BASE_URL, QUERY_KEYS, WEBSOCKET } from '@/shared/config/constants';

export const useMetricsRealtime = (enabled: boolean) => {
  const queryClient = useQueryClient();

  useEffect(() => {
    if (!enabled) return undefined;

    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_BASE_URL}${WEBSOCKET.endpoint}`),
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe(WEBSOCKET.metricsDestination, () => {
          queryClient.invalidateQueries({ queryKey: QUERY_KEYS.posts });
          queryClient.invalidateQueries({ queryKey: QUERY_KEYS.chartData });
          queryClient.invalidateQueries({ queryKey: QUERY_KEYS.metricHistory });
          queryClient.invalidateQueries({ queryKey: QUERY_KEYS.syncRuns });
        });
      },
    });

    client.activate();
    return () => {
      client.deactivate().catch(() => undefined);
    };
  }, [enabled, queryClient]);
};
