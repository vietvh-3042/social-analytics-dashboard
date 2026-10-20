package com.learning.socialanalytics.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.repository.AdminUserRepository;
import com.learning.socialanalytics.repository.CrawlSettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CrawlSettingServiceIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired private AdminUserRepository adminUserRepository;
    @Autowired private CrawlSettingRepository crawlSettingRepository;
    @Autowired private TestEntityManager entityManager;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Test
    void createsDefaultSettingForExistingAdminWithoutOne() {
        AdminUser admin =
                adminUserRepository.saveAndFlush(
                        new AdminUser("test-facebook-subject", "Test Admin", null, null));
        entityManager.detach(admin);
        CrawlSettingService service =
                new CrawlSettingService(adminUserRepository, crawlSettingRepository);

        var result = service.getOrCreate(admin);

        assertThat(result.intervalMinutes()).isEqualTo(60);
        assertThat(result.enabled()).isTrue();
        assertThat(result.nextRunAt()).isNotNull();
        assertThat(crawlSettingRepository.findByAdmin_Id(admin.getId())).isPresent();
    }

    @Test
    void updatesSettingForExistingAdminWithoutOne() {
        AdminUser admin =
                adminUserRepository.saveAndFlush(
                        new AdminUser("another-test-subject", "Test Admin", null, null));
        entityManager.detach(admin);
        CrawlSettingService service =
                new CrawlSettingService(adminUserRepository, crawlSettingRepository);

        var result = service.update(admin, 120, true);

        assertThat(result.intervalMinutes()).isEqualTo(120);
        assertThat(result.enabled()).isTrue();
        assertThat(result.nextRunAt()).isNotNull();
        assertThat(crawlSettingRepository.findByAdmin_Id(admin.getId())).isPresent();
    }
}
