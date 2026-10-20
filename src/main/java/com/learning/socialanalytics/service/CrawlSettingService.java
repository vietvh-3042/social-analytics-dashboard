package com.learning.socialanalytics.service;

import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.dto.CrawlSettingDto;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.entity.CrawlSetting;
import com.learning.socialanalytics.repository.AdminUserRepository;
import com.learning.socialanalytics.repository.CrawlSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrawlSettingService {
    private final AdminUserRepository adminUserRepository;
    private final CrawlSettingRepository repository;

    public CrawlSettingService(
            AdminUserRepository adminUserRepository, CrawlSettingRepository repository) {
        this.adminUserRepository = adminUserRepository;
        this.repository = repository;
    }

    @Transactional
    public CrawlSettingDto getOrCreate(AdminUser admin) {
        CrawlSetting setting =
                repository
                        .findByAdmin_Id(admin.getId())
                        .orElseGet(
                                () ->
                                        repository.save(
                                                new CrawlSetting(
                                                        adminUserRepository.getReferenceById(
                                                                admin.getId()))));
        return toDto(setting);
    }

    @Transactional
    public CrawlSettingDto update(AdminUser admin, int intervalMinutes, boolean enabled) {
        if (intervalMinutes < ApplicationConstants.MIN_SYNC_INTERVAL_MINUTES
                || intervalMinutes > ApplicationConstants.MAX_SYNC_INTERVAL_MINUTES) {
            throw new IllegalArgumentException("Interval must be between 1 and 1,440 minutes");
        }
        CrawlSetting setting =
                repository
                        .findByAdmin_Id(admin.getId())
                        .orElseGet(
                                () ->
                                        new CrawlSetting(
                                                adminUserRepository.getReferenceById(
                                                        admin.getId())));
        setting.update(intervalMinutes, enabled);
        return toDto(repository.save(setting));
    }

    public CrawlSettingDto toDto(CrawlSetting setting) {
        return new CrawlSettingDto(
                setting.getIntervalMinutes(),
                setting.isEnabled(),
                setting.getLastRunAt(),
                setting.getNextRunAt());
    }
}
