package com.learning.socialanalytics.controller;

import com.learning.socialanalytics.constants.ApiPaths;
import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.ChartPointDto;
import com.learning.socialanalytics.dto.MetricDto;
import com.learning.socialanalytics.dto.SyncRunDto;
import com.learning.socialanalytics.service.MetricSyncService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricController {
    private final MetricSyncService syncService;

    public MetricController(MetricSyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping(ApiPaths.SYNC_NOW)
    public SyncRunDto syncNow(@AuthenticationPrincipal OAuth2User principal) {
        return syncService.syncNow(principal.getName());
    }

    @GetMapping(ApiPaths.SYNC_RUNS)
    public List<SyncRunDto> runs(@AuthenticationPrincipal OAuth2User principal) {
        return syncService.runs(principal.getName());
    }

    @GetMapping(ApiPaths.METRICS_HISTORY)
    public List<MetricDto> history(@AuthenticationPrincipal OAuth2User principal) {
        return syncService.history(principal.getName());
    }

    @GetMapping(ApiPaths.CHART_DATA)
    public List<ChartPointDto> chartData(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam(defaultValue = ApplicationConstants.DEFAULT_CHART_DAYS_TEXT) int days) {
        return syncService.chartData(principal.getName(), days);
    }
}
