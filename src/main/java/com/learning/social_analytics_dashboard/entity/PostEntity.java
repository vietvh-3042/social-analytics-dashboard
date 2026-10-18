package com.learning.social_analytics_dashboard.entity;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
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
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {
    @Id
    private UUID id;

    @Column(name = "social_account_id")
    @Setter
    private UUID socialAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SourcePlatform source;

    @Column(name = "external_id", length = 180)
    private String externalId;

    @Column(nullable = false, length = 5000)
    @Setter
    private String content;

    @Column(name = "published_at", nullable = false)
    @Setter
    private Instant publishedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public PostEntity(SourcePlatform source, String externalId, String content, Instant publishedAt) {
        this.id = UUID.randomUUID();
        this.source = source;
        this.externalId = externalId;
        this.content = content;
        this.publishedAt = publishedAt;
        this.createdAt = Instant.now();
    }

}
