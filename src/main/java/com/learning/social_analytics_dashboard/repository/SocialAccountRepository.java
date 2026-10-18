package com.learning.social_analytics_dashboard.repository;

import com.learning.social_analytics_dashboard.entity.SocialAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SocialAccountRepository extends JpaRepository<SocialAccountEntity, UUID> {
    List<SocialAccountEntity> findByUserId(UUID userId);
}
