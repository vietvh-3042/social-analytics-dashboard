import { ArrowDownRight, ArrowUpRight, Heart, MessageCircle, Share2, UsersRound } from 'lucide-react';
import type { MetricHistory } from '@/features/dashboard/model/types';

type MetricSummaryCardsProps = {
  history: MetricHistory[];
};

const latestAndPreviousPerPost = (history: MetricHistory[]) => {
  const metrics = new Map<string, MetricHistory[]>();
  history.forEach((metric) => {
    const postMetrics = metrics.get(metric.postId) ?? [];
    if (postMetrics.length < 2) metrics.set(metric.postId, [...postMetrics, metric]);
  });
  return Array.from(metrics.values());
};

export const MetricSummaryCards = ({ history }: MetricSummaryCardsProps) => {
  const metricsByPost = latestAndPreviousPerPost(history);
  const definitions = [
    { label: 'Reactions', key: 'likes' as const, icon: Heart },
    { label: 'Comments', key: 'comments' as const, icon: MessageCircle },
    { label: 'Shares', key: 'shares' as const, icon: Share2 },
    { label: 'Followers', key: 'followers' as const, icon: UsersRound },
  ];

  return (
    <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
      {definitions.map(({ label, key, icon: Icon }) => {
        const total = metricsByPost.reduce((sum, values) => sum + (values[0]?.[key] ?? 0), 0);
        const previousTotal = metricsByPost.reduce((sum, values) => sum + (values[1]?.[key] ?? 0), 0);
        const percentChange = previousTotal > 0 ? ((total - previousTotal) / previousTotal) * 100 : null;
        const isUp = percentChange === null || percentChange >= 0;
        const DeltaIcon = isUp ? ArrowUpRight : ArrowDownRight;
        return (
          <article key={key} className="rounded-xl border bg-card p-5 shadow-sm transition-shadow hover:shadow-md">
            <div className="flex items-center justify-between">
              <p className="text-sm font-medium text-muted-foreground">{label}</p>
              <span className="grid size-9 place-items-center rounded-lg bg-blue-50 text-primary">
                <Icon aria-hidden="true" className="size-[18px]" />
              </span>
            </div>
            <div className="mt-4 flex items-end justify-between gap-3">
              <p className="font-mono text-2xl font-semibold tracking-tight">{total.toLocaleString()}</p>
              {percentChange !== null && (
                <span className={`mb-1 inline-flex items-center gap-0.5 text-xs font-medium ${isUp ? 'text-emerald-700' : 'text-amber-700'}`}>
                  <DeltaIcon aria-hidden="true" className="size-3.5" /> {Math.abs(percentChange).toFixed(1)}%
                </span>
              )}
            </div>
            <p className="mt-2 text-xs text-muted-foreground">Latest synthetic totals across posts</p>
          </article>
        );
      })}
    </div>
  );
};
