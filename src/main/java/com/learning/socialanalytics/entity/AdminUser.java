package com.learning.socialanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUser {
  @Id @GeneratedValue private UUID id;

  @Column(nullable = false, unique = true, length = 128)
  private String facebookSubject;

  @Column(nullable = false, length = 200)
  private String displayName;

  @Column(length = 320)
  private String email;

  @Column(length = 1000)
  private String pictureUrl;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  public AdminUser(String facebookSubject, String displayName, String email, String pictureUrl) {
    this.facebookSubject = facebookSubject;
    updateProfile(displayName, email, pictureUrl);
  }

  public void updateProfile(String name, String userEmail, String userPictureUrl) {
    this.displayName = name;
    this.email = userEmail;
    this.pictureUrl = userPictureUrl;
  }

  @PrePersist
  void onCreate() {
    createdAt = Instant.now();
    updatedAt = createdAt;
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = Instant.now();
  }
}
