package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.response.ChartPoint;
import com.learning.social_analytics_dashboard.dto.response.DashboardSummary;

import java.util.List;

public interface DashboardQuery {
    List<ChartPoint> chartData();
    DashboardSummary summary();
}
