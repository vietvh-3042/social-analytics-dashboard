import { Badge } from '@/components/ui/badge';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import type { SyncRun } from '@/features/dashboard/model/types';

type SyncRunListProps = {
  runs: SyncRun[];
};

const statusVariant = (status: SyncRun['status']): 'secondary' | 'destructive' | 'outline' => {
  if (status === 'COMPLETED') return 'secondary';
  if (status === 'FAILED') return 'destructive';
  return 'outline';
};

export const SyncRunList = ({ runs }: SyncRunListProps) => (
  <Card>
    <CardHeader className="pb-3">
      <CardTitle className="text-base">Recent sync activity</CardTitle>
      <CardDescription>Latest scheduled and manual runs.</CardDescription>
    </CardHeader>
    <CardContent>
      {runs.length === 0 ? (
        <p className="py-4 text-sm text-muted-foreground">Your sync history will show here.</p>
      ) : (
        <div className="divide-y">
          {runs.slice(0, 5).map((run) => (
            <div key={run.id} className="flex items-center justify-between gap-4 py-3 first:pt-0 last:pb-0">
              <div className="min-w-0">
                <p className="text-sm font-medium">{run.source === 'MANUAL' ? 'Manual sync' : 'Scheduled sync'}</p>
                <p className="mt-1 text-xs text-muted-foreground">
                  {new Date(run.startedAt).toLocaleString()} · {run.snapshotsCreated} snapshots
                </p>
              </div>
              <Badge variant={statusVariant(run.status)}>
                {run.status.toLowerCase()}
              </Badge>
            </div>
          ))}
        </div>
      )}
    </CardContent>
  </Card>
);
