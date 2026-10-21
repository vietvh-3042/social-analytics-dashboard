package com.learning.socialanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metric_snapshots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MetricSnapshot {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "post_id", nullable = false)
  private SocialPost post;

  @Column(nullable = false)
  private long likes;

  @Column(nullable = false)
  private long comments;

  @Column(nullable = false)
  private long shares;

  @Column(nullable = false)
  private long followers;

  @Column(nullable = false, updatable = false)
  private Instant capturedAt;

  public MetricSnapshot(
      SocialPost post, long likes, long comments, long shares, long followers, Instant capturedAt) {
    this.post = post;
    this.likes = likes;
    this.comments = comments;
    this.shares = shares;
    this.followers = followers;
    this.capturedAt = capturedAt;
  }
}
