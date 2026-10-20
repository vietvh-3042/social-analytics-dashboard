"use client";

import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Clock3, LoaderCircle, Save } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { dashboardApi } from "@/features/dashboard/api/dashboardApi";
import { QUERY_KEYS, SYNC_INTERVAL } from "@/shared/config/constants";

export const CrawlSettingsPanel = () => {
  const queryClient = useQueryClient();
  const { data: settings } = useQuery({
    queryKey: QUERY_KEYS.crawlSettings,
    queryFn: dashboardApi.settings,
  });
  const [draft, setDraft] = useState<{
    amount: number;
    unit: "minutes" | "hours";
    enabled: boolean;
  } | null>(null);
  const storedMinutes = settings?.intervalMinutes ?? SYNC_INTERVAL.defaultMinutes;
  const storedUnit = storedMinutes % 60 === 0 ? "hours" : "minutes";
  const amount = draft?.amount ?? (storedUnit === "hours" ? storedMinutes / 60 : storedMinutes);
  const unit = draft?.unit ?? storedUnit;
  const enabled = draft?.enabled ?? settings?.enabled ?? true;

  const saveMutation = useMutation({
    mutationFn: dashboardApi.updateSettings,
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: QUERY_KEYS.crawlSettings });
      setDraft(null);
      toast.success("Sync schedule saved.");
    },
    onError: () => toast.error("Could not save the sync schedule."),
  });

  const intervalMinutes = unit === "hours" ? amount * 60 : amount;
  const valid =
    intervalMinutes >= SYNC_INTERVAL.minimumMinutes &&
    intervalMinutes <= SYNC_INTERVAL.maximumMinutes;

  return (
    <Card id="settings" className="h-full scroll-mt-24">
      <CardHeader className="pb-4">
        <div className="flex items-start justify-between gap-4">
          <div>
            <CardTitle className="text-base">Automatic sync</CardTitle>
            <CardDescription className="mt-1">
              Generate fresh synthetic metrics on your schedule.
            </CardDescription>
          </div>
          <span className="text-primary grid size-9 place-items-center rounded-lg bg-blue-50">
            <Clock3 aria-hidden="true" className="size-[18px]" />
          </span>
        </div>
      </CardHeader>
      <CardContent>
        <div className="flex min-h-11 items-center justify-between rounded-lg border px-3 text-sm">
          <span className="font-medium">Scheduled sync</span>
          <input
            aria-label="Scheduled sync enabled"
            checked={enabled}
            className="accent-primary size-4"
            onChange={(event) => setDraft({ amount, unit, enabled: event.target.checked })}
            type="checkbox"
          />
        </div>
        <div className="mt-4 grid grid-cols-[1fr_1fr] gap-3">
          <div className="space-y-1.5 text-sm font-medium">
            <span id="sync-interval-label">Every</span>
            <Input
              id="sync-interval"
              aria-labelledby="sync-interval-label"
              min={1}
              max={unit === "hours" ? 24 : 1440}
              onChange={(event) => setDraft({ amount: Number(event.target.value), unit, enabled })}
              type="number"
              value={amount}
            />
          </div>
          <div className="space-y-1.5 text-sm font-medium">
            <span id="sync-unit-label">Unit</span>
            <select
              id="sync-unit"
              aria-labelledby="sync-unit-label"
              className="bg-background focus-visible:ring-ring h-9 w-full rounded-md border px-3 text-sm shadow-sm outline-none focus-visible:ring-2"
              onChange={(event) =>
                setDraft({ amount, unit: event.target.value as "minutes" | "hours", enabled })
              }
              value={unit}
            >
              <option value="minutes">Minutes</option>
              <option value="hours">Hours</option>
            </select>
          </div>
        </div>
        <p className="text-muted-foreground mt-2 text-xs">
          Choose 1 minute to 24 hours. Times use UTC.
        </p>
        <Button
          className="mt-5 w-full"
          disabled={!valid || saveMutation.isPending}
          onClick={() => saveMutation.mutate({ intervalMinutes, enabled })}
        >
          {saveMutation.isPending ? (
            <LoaderCircle aria-hidden="true" className="size-4 animate-spin" />
          ) : (
            <Save aria-hidden="true" className="size-4" />
          )}
          Save schedule
        </Button>
        <div className="mt-4 grid grid-cols-2 gap-3 border-t pt-4 text-xs">
          <div>
            <p className="text-muted-foreground">Last sync</p>
            <p className="mt-1 font-medium">
              {settings?.lastRunAt
                ? new Date(settings.lastRunAt).toLocaleString()
                : "Not synced yet"}
            </p>
          </div>
          <div>
            <p className="text-muted-foreground">Next sync</p>
            <p className="mt-1 font-medium">
              {settings?.nextRunAt ? new Date(settings.nextRunAt).toLocaleString() : "Paused"}
            </p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};
