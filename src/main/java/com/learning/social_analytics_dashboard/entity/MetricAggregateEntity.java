package com.learning.social_analytics_dashboard.entity;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metric_aggregate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MetricAggregateEntity {
    @EmbeddedId
    private MetricAggregateId id;

    @Column(nullable = false)
    private long likes;

    @Column(nullable = false)
    private long shares;

    @Column(nullable = false)
    private long followers;

    public MetricAggregateEntity(Instant bucket, SourcePlatform source, long likes,
                                 long shares, long followers) {
        this.id = new MetricAggregateId(bucket, source);
        this.likes = likes;
        this.shares = shares;
        this.followers = followers;
    }
}
