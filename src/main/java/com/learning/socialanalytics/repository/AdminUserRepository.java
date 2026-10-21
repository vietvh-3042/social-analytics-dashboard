package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.AdminUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, UUID> {
  Optional<AdminUser> findByFacebookSubject(String facebookSubject);
}
