package com.learning.social_analytics_dashboard.controller;

import com.learning.social_analytics_dashboard.dto.response.ImportResult;
import com.learning.social_analytics_dashboard.service.PostExportUseCase;
import com.learning.social_analytics_dashboard.service.PostImportUseCase;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImportExportController {
    private final PostImportUseCase importService;
    private final PostExportUseCase exportService;

    @Operation(summary = "Import posts from an Excel workbook")
    @PostMapping(value = "/import-posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImportResult importPosts(@RequestPart("file") MultipartFile file) {
        return importService.importPosts(file);
    }

    @Operation(summary = "Export posts as an Excel workbook")
    @GetMapping("/export-report")
    public ResponseEntity<byte[]> exportReport() {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=social-analytics-report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(exportService.exportReport());
    }
}
