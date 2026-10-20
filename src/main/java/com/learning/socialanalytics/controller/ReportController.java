package com.learning.socialanalytics.controller;

import com.learning.socialanalytics.constants.ApiPaths;
import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.service.AdminUserService;
import com.learning.socialanalytics.service.ReportService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    private final AdminUserService adminUserService;
    private final ReportService reportService;

    public ReportController(AdminUserService adminUserService, ReportService reportService) {
        this.adminUserService = adminUserService;
        this.reportService = reportService;
    }

    @GetMapping(value = ApiPaths.REPORT_EXPORT, produces = ApplicationConstants.XLSX_MEDIA_TYPE)
    public ResponseEntity<byte[]> export(@AuthenticationPrincipal OAuth2User principal) {
        AdminUser admin = adminUserService.requireByFacebookSubject(principal.getName());
        byte[] report = reportService.exportMetrics(admin);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ApplicationConstants.XLSX_MEDIA_TYPE));
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(ApplicationConstants.REPORT_FILENAME)
                        .build());
        return ResponseEntity.ok().headers(headers).body(report);
    }
}
