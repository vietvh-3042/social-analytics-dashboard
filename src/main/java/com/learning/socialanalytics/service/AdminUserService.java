package com.learning.socialanalytics.service;

import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.exception.ResourceNotFoundException;
import com.learning.socialanalytics.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {
  private final AdminUserRepository repository;

  @Transactional(readOnly = true)
  public AdminUser requireByFacebookSubject(String subject) {
    return repository
        .findByFacebookSubject(subject)
        .orElseThrow(() -> new ResourceNotFoundException("Signed-in Facebook user was not found"));
  }

  @Transactional
  public AdminUser upsertFacebookUser(
      String subject, String name, String email, String pictureUrl) {
    return repository
        .findByFacebookSubject(subject)
        .map(
            existing -> {
              existing.updateProfile(name, email, pictureUrl);
              return existing;
            })
        .orElseGet(() -> repository.save(new AdminUser(subject, name, email, pictureUrl)));
  }
}
