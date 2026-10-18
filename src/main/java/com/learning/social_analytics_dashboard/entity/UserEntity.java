package com.learning.social_analytics_dashboard.entity;

import com.learning.social_analytics_dashboard.common.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String username;

    @Column(name = "display_name", nullable = false, length = 160)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public UserEntity(String username, String displayName, UserRole role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.displayName = displayName;
        this.role = role;
        this.createdAt = Instant.now();
    }

}
