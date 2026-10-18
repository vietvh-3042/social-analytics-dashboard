package com.learning.social_analytics_dashboard.entity;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
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
@Table(name = "social_account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccountEntity {
    @Id
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private SourcePlatform platform;

    @Column(name = "external_id")
    private String externalAccountId;

    @Column(name = "last_synced_at")
    @Getter(AccessLevel.NONE)
    private Instant lastSyncedAt;

    public SocialAccountEntity(UUID userId, SourcePlatform platform, String externalAccountId,
                               String displayName) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.platform = platform;
        this.externalAccountId = externalAccountId;
        this.displayName = displayName;
        this.lastSyncedAt = Instant.now();
    }

    public Instant getConnectedAt() { return lastSyncedAt; }

    @jakarta.persistence.Transient
    private String displayName;
}
