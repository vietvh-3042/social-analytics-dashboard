"use client";

import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { FileUp, LoaderCircle, Upload } from "lucide-react";
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { postsApi } from "@/features/posts/api/postsApi";
import type { ImportResult } from "@/features/posts/model/types";
import { QUERY_KEYS, SUPPORTED_IMPORT } from "@/shared/config/constants";

export const PostImportPanel = () => {
  const queryClient = useQueryClient();
  const [file, setFile] = useState<File | null>(null);
  const [result, setResult] = useState<ImportResult | null>(null);
  const importMutation = useMutation({
    mutationFn: postsApi.importWorkbook,
    onSuccess: async (data) => {
      setResult(data);
      await queryClient.invalidateQueries({ queryKey: QUERY_KEYS.posts });
      toast.success(`${data.imported} post${data.imported === 1 ? "" : "s"} imported.`);
    },
    onError: () => toast.error("Import failed. Check the workbook format and try again."),
  });

  const importFile = () => {
    if (!file) return;
    if (file.size > SUPPORTED_IMPORT.maximumSizeBytes) {
      toast.error("Workbook size must be 10 MB or less.");
      return;
    }
    importMutation.mutate(file);
  };

  return (
    <Card>
      <CardHeader className="pb-4">
        <div className="flex items-start justify-between gap-3">
          <div>
            <CardTitle className="text-base">Import Facebook posts</CardTitle>
            <CardDescription className="mt-1">
              Upload a workbook with post_url and optional title columns.
            </CardDescription>
          </div>
          <FileUp aria-hidden="true" className="text-primary mt-1 size-5" />
        </div>
      </CardHeader>
      <CardContent>
        <Input
          accept={SUPPORTED_IMPORT.extension}
          aria-label="Choose Excel workbook"
          onChange={(event) => setFile(event.target.files?.[0] ?? null)}
          type="file"
        />
        <p className="text-muted-foreground mt-2 text-xs">XLSX · Up to 10 MB and 5,000 rows</p>
        <Button
          className="mt-4 w-full"
          disabled={!file || importMutation.isPending}
          onClick={importFile}
          variant="outline"
        >
          {importMutation.isPending ? (
            <LoaderCircle aria-hidden="true" className="size-4 animate-spin" />
          ) : (
            <Upload aria-hidden="true" className="size-4" />
          )}
          Import workbook
        </Button>
        {result && (
          <div className="mt-4 space-y-2 border-t pt-3">
            <div className="flex gap-2 text-xs">
              <Badge variant="secondary">{result.imported} imported</Badge>
              <Badge variant={result.rejected ? "destructive" : "outline"}>
                {result.rejected} rejected
              </Badge>
            </div>
            <div className="max-h-36 space-y-2 overflow-y-auto">
              {result.rows.map((row) => (
                <div key={row.rowNumber} className="flex items-start justify-between gap-3 text-xs">
                  <span className="text-muted-foreground">
                    Row {row.rowNumber}: {row.message}
                  </span>
                  <span
                    className={
                      row.status === "ACCEPTED"
                        ? "font-medium text-emerald-700"
                        : "text-destructive font-medium"
                    }
                  >
                    {row.status}
                  </span>
                </div>
              ))}
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
};
