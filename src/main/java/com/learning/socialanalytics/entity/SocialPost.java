package com.learning.socialanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "posts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "post_url"}))
public class SocialPost {
    @Id @GeneratedValue private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private AdminUser owner;

    @Column(name = "post_url", nullable = false, length = 2048)
    private String postUrl;

    @Column(length = 240)
    private String title;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant importedAt = Instant.now();

    protected SocialPost() {}

    public SocialPost(AdminUser owner, String postUrl, String title) {
        this.owner = owner;
        this.postUrl = postUrl;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public AdminUser getOwner() {
        return owner;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getImportedAt() {
        return importedAt;
    }
}
