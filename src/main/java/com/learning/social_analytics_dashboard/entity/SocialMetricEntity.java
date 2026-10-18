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

@Entity
@Table(name = "social_metric")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMetricEntity {
    @Id
    private UUID id;

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(nullable = false)
    private long likes;

    @Column(nullable = false)
    private long shares;

    @Column(nullable = false)
    private long comments;

    @Column(nullable = false)
    private long followers;

    @Column(name = "measured_at", nullable = false)
    private Instant measuredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SourcePlatform provider;

    public SocialMetricEntity(UUID postId, long likes, long shares, long comments,
                              long followers, Instant measuredAt, SourcePlatform provider) {
        this.id = UUID.randomUUID();
        this.postId = postId;
        this.likes = likes;
        this.shares = shares;
        this.comments = comments;
        this.followers = followers;
        this.measuredAt = measuredAt;
        this.provider = provider;
    }

}
