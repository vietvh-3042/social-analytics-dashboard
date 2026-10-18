package com.learning.social_analytics_dashboard.service;

import com.learning.social_analytics_dashboard.dto.response.ImportResult;

import org.springframework.web.multipart.MultipartFile;

public interface PostImportUseCase {
    ImportResult importPosts(MultipartFile file);
}
