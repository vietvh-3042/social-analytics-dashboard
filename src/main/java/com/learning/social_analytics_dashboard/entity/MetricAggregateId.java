package com.learning.social_analytics_dashboard.entity;

import com.learning.social_analytics_dashboard.common.SourcePlatform;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class MetricAggregateId implements Serializable {
    private Instant bucket;

    @Enumerated(EnumType.STRING)
    private SourcePlatform source;

    public MetricAggregateId(Instant bucket, SourcePlatform source) {
        this.bucket = bucket;
        this.source = source;
    }
}
