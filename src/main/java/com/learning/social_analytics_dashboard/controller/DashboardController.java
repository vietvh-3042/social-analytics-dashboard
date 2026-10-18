package com.learning.social_analytics_dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "dashboard";
    }
}
