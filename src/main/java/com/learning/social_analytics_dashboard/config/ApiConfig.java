package com.learning.social_analytics_dashboard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Bean
    OpenAPI socialAnalyticsApi() {
        return new OpenAPI().info(new Info().title("Social Analytics Dashboard API")
                .version("1.0.0")
                .description("Posts, social metrics, chart data, and Excel reporting APIs."));
    }
}
