package com.learning.social_analytics_dashboard.controller;

import com.learning.social_analytics_dashboard.dto.request.MetricRequest;
import com.learning.social_analytics_dashboard.dto.response.ChartPoint;
import com.learning.social_analytics_dashboard.dto.response.DashboardSummary;
import com.learning.social_analytics_dashboard.dto.response.MetricResponse;
import com.learning.social_analytics_dashboard.service.DashboardQuery;
import com.learning.social_analytics_dashboard.service.MetricUseCase;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Metrics")
public class MetricController {
    private final MetricUseCase service;
    private final DashboardQuery aggregationService;

    @PostMapping("/posts/{postId}/metrics")
    @Operation(summary = "Add a point-in-time metric")
    @ResponseStatus(HttpStatus.CREATED)
    public MetricResponse add(@PathVariable UUID postId, @Valid @RequestBody MetricRequest request) {
        return service.add(postId, request);
    }

    @GetMapping("/posts/{postId}/metrics")
    @Operation(summary = "List metrics for a post")
    public List<MetricResponse> list(@PathVariable UUID postId) { return service.listForPost(postId); }

    @GetMapping("/chart-data")
    @Operation(summary = "Read metric trend data")
    public List<ChartPoint> chartData() { return aggregationService.chartData(); }

    @GetMapping("/summary")
    @Operation(summary = "Read dashboard summary")
    public DashboardSummary summary() { return aggregationService.summary(); }
}
