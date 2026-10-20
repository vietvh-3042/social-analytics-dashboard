"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Download, LoaderCircle, LogOut, RefreshCw } from "lucide-react";
import { toast } from "sonner";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { authApi } from "@/features/auth/api/authApi";
import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser";
import { dashboardApi } from "@/features/dashboard/api/dashboardApi";
import { CrawlSettingsPanel } from "@/features/dashboard/components/CrawlSettingsPanel";
import { DashboardSidebar } from "@/features/dashboard/components/DashboardSidebar";
import { MetricSummaryCards } from "@/features/dashboard/components/MetricSummaryCards";
import { MetricTrendChart } from "@/features/dashboard/components/MetricTrendChart";
import { PostsTable } from "@/features/dashboard/components/PostsTable";
import { SyncRunList } from "@/features/dashboard/components/SyncRunList";
import { useMetricsRealtime } from "@/features/dashboard/hooks/useMetricsRealtime";
import { useDashboardStore } from "@/features/dashboard/model/dashboardStore";
import { PostImportPanel } from "@/features/posts/components/PostImportPanel";
import { postsApi } from "@/features/posts/api/postsApi";
import {
  DASHBOARD_SECTION_TARGETS,
  DATE_RANGE_DAYS,
  QUERY_KEYS,
  REPORT_FILENAME,
  DASHBOARD_SECTIONS,
} from "@/shared/config/constants";

export const DashboardScreen = () => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const { data: user, isLoading: userLoading, isError: userError } = useCurrentUser();
  const days = useDashboardStore((state) => state.selectedRangeDays);
  const setDays = useDashboardStore((state) => state.setSelectedRangeDays);
  useMetricsRealtime(Boolean(user));

  useEffect(() => {
    if (userError) router.replace("/");
  }, [router, userError]);

  useEffect(() => {
    if (!user) return;

    const section = DASHBOARD_SECTION_TARGETS.find(
      ({ id }) => id === window.location.hash.slice(1),
    );
    if (section) document.getElementById(section.id)?.scrollIntoView({ block: "start" });
  }, [user]);

  const postsQuery = useQuery({
    queryKey: QUERY_KEYS.posts,
    queryFn: postsApi.list,
    enabled: Boolean(user),
  });
  const historyQuery = useQuery({
    queryKey: QUERY_KEYS.metricHistory,
    queryFn: dashboardApi.history,
    enabled: Boolean(user),
  });
  const runsQuery = useQuery({
    queryKey: QUERY_KEYS.syncRuns,
    queryFn: dashboardApi.runs,
    enabled: Boolean(user),
  });
  const chartQuery = useQuery({
    queryKey: [...QUERY_KEYS.chartData, days],
    queryFn: () => dashboardApi.chartData(days),
    enabled: Boolean(user),
  });

  const syncMutation = useMutation({
    mutationFn: dashboardApi.syncNow,
    onSuccess: async () => {
      toast.success("Sync queued. New synthetic metrics will appear shortly.");
      await queryClient.invalidateQueries({ queryKey: QUERY_KEYS.syncRuns });
    },
    onError: () => toast.error("Could not start a sync. Check your connection and try again."),
  });

  const logoutMutation = useMutation({
    mutationFn: authApi.logout,
    onSuccess: async () => {
      await queryClient.clear();
      router.replace("/");
    },
    onError: () => toast.error("Could not sign out. Please try again."),
  });

  const exportReport = async () => {
    try {
      const blob = await dashboardApi.exportReport();
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = REPORT_FILENAME;
      link.click();
      URL.revokeObjectURL(url);
    } catch {
      toast.error("Could not export the report. Try again shortly.");
    }
  };

  if (userLoading || (!user && !userError)) {
    return (
      <div
        aria-live="polite"
        className="text-muted-foreground grid min-h-screen place-items-center text-sm"
      >
        Loading your workspace…
      </div>
    );
  }

  if (!user)
    return (
      <div className="text-muted-foreground grid min-h-screen place-items-center text-sm">
        Returning to sign in…
      </div>
    );

  const postCount = postsQuery.data?.length ?? 0;
  const history = historyQuery.data ?? [];

  return (
    <div className="bg-background min-h-screen md:flex">
      <DashboardSidebar />
      <main className="min-w-0 flex-1">
        <header className="sticky top-0 z-20 flex min-h-[72px] items-center justify-between gap-4 border-b bg-white/95 px-4 backdrop-blur sm:px-7 lg:px-9">
          <div>
            <p className="text-muted-foreground font-mono text-[10px] tracking-[0.18em] uppercase">
              Facebook analytics
            </p>
            <h1 className="mt-1 text-lg font-semibold tracking-tight">
              {DASHBOARD_SECTIONS.overview.label}
            </h1>
          </div>
          <div className="flex items-center gap-2 sm:gap-3">
            <Button className="hidden sm:inline-flex" onClick={exportReport} variant="outline">
              <Download aria-hidden="true" className="size-4" /> Export report
            </Button>
            <Button disabled={syncMutation.isPending} onClick={() => syncMutation.mutate()}>
              {syncMutation.isPending ? (
                <LoaderCircle aria-hidden="true" className="size-4 animate-spin" />
              ) : (
                <RefreshCw aria-hidden="true" className="size-4" />
              )}
              <span className="hidden sm:inline">Sync now</span>
              <span className="sm:hidden">Sync</span>
            </Button>
            <Avatar className="size-9">
              <AvatarImage alt="" src={user.pictureUrl ?? undefined} />
              <AvatarFallback>{user.name.slice(0, 1).toUpperCase()}</AvatarFallback>
            </Avatar>
            <Button
              aria-label="Sign out"
              className="size-9 px-0"
              onClick={() => logoutMutation.mutate()}
              variant="ghost"
            >
              <LogOut aria-hidden="true" className="size-4" />
            </Button>
          </div>
        </header>

        <div className="mx-auto max-w-[1600px] space-y-6 px-4 py-6 sm:px-7 lg:px-9 lg:py-8">
          <section
            id="overview"
            className="flex scroll-mt-24 flex-col justify-between gap-4 sm:flex-row sm:items-end"
          >
            <div>
              <h2 className="text-2xl font-semibold tracking-tight">
                Good to see you, {user.name.split(" ")[0]}
              </h2>
              <p className="text-muted-foreground mt-1 text-sm">
                A clear view of your imported Facebook posts and engagement history.
              </p>
            </div>
            <p className="text-muted-foreground text-sm">
              {postCount.toLocaleString()} tracked {postCount === 1 ? "post" : "posts"}
            </p>
          </section>

          <section aria-label="Latest post metrics">
            <MetricSummaryCards history={history} />
          </section>

          <section className="grid gap-5 xl:grid-cols-[minmax(0,1.65fr)_minmax(320px,0.85fr)]">
            <Card>
              <CardHeader className="flex flex-row items-start justify-between gap-4 pb-2">
                <div>
                  <CardTitle className="text-base">Engagement over time</CardTitle>
                  <CardDescription className="mt-1">
                    Daily totals across your tracked posts.
                  </CardDescription>
                </div>
                <select
                  aria-label="Chart date range"
                  className="bg-background focus-visible:ring-ring h-9 rounded-md border px-3 text-sm outline-none focus-visible:ring-2"
                  onChange={(event) => setDays(Number(event.target.value))}
                  value={days}
                >
                  <option value={7}>Last 7 days</option>
                  <option value={DATE_RANGE_DAYS}>Last 30 days</option>
                  <option value={90}>Last 90 days</option>
                </select>
              </CardHeader>
              <CardContent className="pt-3">
                <MetricTrendChart points={chartQuery.data ?? []} />
                <p className="text-muted-foreground mt-3 text-xs">
                  Synthetic values generated with Datafaker. They are not sourced from Facebook.
                </p>
              </CardContent>
            </Card>
            <CrawlSettingsPanel />
          </section>

          <section
            id="posts"
            className="grid scroll-mt-24 gap-5 xl:grid-cols-[minmax(0,1.35fr)_minmax(320px,0.8fr)]"
          >
            <PostsTable posts={postsQuery.data ?? []} />
            <PostImportPanel />
          </section>

          <SyncRunList runs={runsQuery.data ?? []} />
          <div className="sm:hidden">
            <Button className="w-full" onClick={exportReport} variant="outline">
              <Download aria-hidden="true" className="size-4" /> Export report
            </Button>
          </div>
          <footer className="text-muted-foreground border-t pt-4 text-xs">
            Private workspace · All timestamps are shown in your local time.
          </footer>
        </div>
      </main>
    </div>
  );
};
