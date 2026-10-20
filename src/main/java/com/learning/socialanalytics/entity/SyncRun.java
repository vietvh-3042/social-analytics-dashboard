package com.learning.socialanalytics.entity;

import com.learning.socialanalytics.constants.ApplicationConstants;
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

@Entity
@Table(name = "sync_runs")
public class SyncRun {
    @Id @GeneratedValue private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminUser admin;

    @Column(nullable = false, length = 16)
    private String source;

    @Column(nullable = false, length = 16)
    private String status = ApplicationConstants.STATUS_QUEUED;

    @Column(nullable = false)
    private Instant startedAt = Instant.now();

    private Instant completedAt;

    @Column(nullable = false)
    private int snapshotsCreated;

    @Column(length = 500)
    private String errorSummary;

    protected SyncRun() {}

    public SyncRun(AdminUser admin, String source) {
        this.admin = admin;
        this.source = source;
    }

    public void complete(int created, Instant finishedAt) {
        status = ApplicationConstants.STATUS_COMPLETED;
        snapshotsCreated = created;
        completedAt = finishedAt;
    }

    public void fail(String safeSummary, Instant finishedAt) {
        status = ApplicationConstants.STATUS_FAILED;
        errorSummary = safeSummary;
        completedAt = finishedAt;
    }

    public UUID getId() {
        return id;
    }

    public AdminUser getAdmin() {
        return admin;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public int getSnapshotsCreated() {
        return snapshotsCreated;
    }

    public String getErrorSummary() {
        return errorSummary;
    }
}
