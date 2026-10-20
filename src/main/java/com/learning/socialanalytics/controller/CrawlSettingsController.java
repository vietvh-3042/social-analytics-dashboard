package com.learning.socialanalytics.controller;

import com.learning.socialanalytics.constants.ApiPaths;
import com.learning.socialanalytics.dto.CrawlSettingDto;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.service.AdminUserService;
import com.learning.socialanalytics.service.CrawlSettingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlSettingsController {
    private final AdminUserService adminUserService;
    private final CrawlSettingService settingService;

    public CrawlSettingsController(
            AdminUserService adminUserService, CrawlSettingService settingService) {
        this.adminUserService = adminUserService;
        this.settingService = settingService;
    }

    @GetMapping(ApiPaths.CRAWL_SETTINGS)
    public CrawlSettingDto get(@AuthenticationPrincipal OAuth2User principal) {
        AdminUser admin = adminUserService.requireByFacebookSubject(principal.getName());
        return settingService.getOrCreate(admin);
    }

    @PutMapping(ApiPaths.CRAWL_SETTINGS)
    public CrawlSettingDto update(
            @AuthenticationPrincipal OAuth2User principal,
            @Valid @RequestBody UpdateCrawlSettingRequest request) {
        AdminUser admin = adminUserService.requireByFacebookSubject(principal.getName());
        return settingService.update(admin, request.intervalMinutes(), request.enabled());
    }

    public record UpdateCrawlSettingRequest(
            @Min(1) @Max(1440) int intervalMinutes, boolean enabled) {}
}
