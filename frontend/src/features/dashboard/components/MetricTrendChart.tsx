'use client';

import {
  Chart as ChartJS,
  CategoryScale,
  Filler,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  Tooltip,
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import { format } from 'date-fns';
import { METRIC_LABELS } from '@/shared/config/constants';
import type { ChartPoint } from '@/features/dashboard/model/types';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Tooltip, Legend, Filler);

type MetricTrendChartProps = {
  points: ChartPoint[];
};

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  interaction: { mode: 'index' as const, intersect: false },
  plugins: {
    legend: { position: 'bottom' as const, labels: { usePointStyle: true, boxWidth: 7, padding: 18 } },
    tooltip: { backgroundColor: '#1e3a8a', padding: 12, cornerRadius: 8 },
  },
  scales: {
    x: { grid: { display: false }, ticks: { maxTicksLimit: 7, color: '#64748b' } },
    y: { beginAtZero: true, border: { display: false }, grid: { color: '#e9eef6' }, ticks: { color: '#64748b' } },
  },
};

export const MetricTrendChart = ({ points }: MetricTrendChartProps) => {
  const data = {
    labels: points.map((point) => format(new Date(point.capturedAt), 'MMM d')),
    datasets: METRIC_LABELS.map((metric) => ({
      label: metric.label,
      data: points.map((point) => point[metric.key]),
      borderColor: metric.color,
      backgroundColor: `${metric.color}14`,
      fill: false,
      tension: 0.35,
      pointRadius: points.length > 20 ? 0 : 3,
      pointHoverRadius: 5,
      borderWidth: 2,
    })),
  };

  if (points.length === 0) {
    return (
      <div className="grid h-72 place-items-center rounded-lg border border-dashed bg-slate-50 text-center">
        <div>
          <p className="font-medium">No metrics yet</p>
          <p className="mt-1 text-sm text-muted-foreground">Sync posts to create your first data point.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="h-72 w-full" aria-label="Engagement metrics trend chart">
      <Line options={chartOptions} data={data} />
    </div>
  );
};
