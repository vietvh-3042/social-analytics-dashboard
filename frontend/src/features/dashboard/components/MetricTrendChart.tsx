'use client';

import {
  BarController,
  BarElement,
  Chart as ChartJS,
  CategoryScale,
  Filler,
  Legend,
  LinearScale,
  LineController,
  LineElement,
  PointElement,
  Tooltip,
  type ChartData,
  type ChartOptions,
} from 'chart.js';
import { Chart } from 'react-chartjs-2';
import { format } from 'date-fns';
import { METRIC_LABELS } from '@/shared/config/constants';
import type { ChartPoint } from '@/features/dashboard/model/types';

ChartJS.register(
  BarController,
  BarElement,
  CategoryScale,
  Filler,
  Legend,
  LinearScale,
  LineController,
  LineElement,
  PointElement,
  Tooltip,
);

type MetricTrendChartProps = {
  points: ChartPoint[];
};

const formatMetricValue = (value: number) =>
  new Intl.NumberFormat('en-US', { notation: 'compact', maximumFractionDigits: 1 }).format(value);
const formatExactMetricValue = (value: number) => value.toLocaleString('en-US');

const chartOptions: ChartOptions<'bar' | 'line'> = {
  responsive: true,
  maintainAspectRatio: false,
  interaction: { mode: 'index' as const, intersect: false },
  plugins: {
    legend: {
      position: 'bottom' as const,
      labels: { usePointStyle: true, boxWidth: 8, padding: 18 },
    },
    tooltip: {
      backgroundColor: '#1e3a8a',
      padding: 12,
      cornerRadius: 8,
      callbacks: {
        label: (context) =>
          `${context.dataset.label}: ${formatExactMetricValue(context.parsed.y ?? 0)}`,
      },
    },
  },
  scales: {
    x: {
      grid: { display: false },
      ticks: { maxTicksLimit: 7, color: '#64748b' },
    },
    engagement: {
      beginAtZero: true,
      border: { display: false },
      grid: { color: '#e9eef6' },
      position: 'left',
      ticks: { color: '#64748b', callback: (value) => formatMetricValue(Number(value)) },
    },
    followers: {
      beginAtZero: true,
      border: { display: false },
      grid: { drawOnChartArea: false },
      position: 'right',
      ticks: { color: '#0f766e', callback: (value) => formatMetricValue(Number(value)) },
    },
  },
};

export const MetricTrendChart = ({ points }: MetricTrendChartProps) => {
  const data: ChartData<'bar' | 'line', number[], string> = {
    labels: points.map((point) => format(new Date(point.capturedAt), 'MMM d')),
    datasets: [
      ...METRIC_LABELS.filter(({ key }) => key !== 'followers').map((metric) => ({
        type: 'bar' as const,
        label: metric.label,
        data: points.map((point) => point[metric.key]),
        backgroundColor: metric.color,
        borderRadius: 5,
        borderSkipped: false,
        barPercentage: 0.8,
        categoryPercentage: 0.72,
        yAxisID: 'engagement',
      })),
      ...METRIC_LABELS.filter(({ key }) => key === 'followers').map((metric) => ({
        type: 'line' as const,
        label: metric.label,
        data: points.map((point) => point[metric.key]),
        borderColor: metric.color,
        backgroundColor: `${metric.color}14`,
        fill: true,
        tension: 0.35,
        pointRadius: points.length === 1 ? 4 : 0,
        pointHoverRadius: 5,
        borderWidth: 2,
        yAxisID: 'followers',
      })),
    ],
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
    <div className="h-72 w-full" role="img" aria-label="Engagement metrics trend chart">
      <Chart type="bar" options={chartOptions} data={data} />
    </div>
  );
};
