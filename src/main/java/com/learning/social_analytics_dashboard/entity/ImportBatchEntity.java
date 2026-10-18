package com.learning.social_analytics_dashboard.entity;

import com.learning.social_analytics_dashboard.common.ImportBatchStatus;
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
@Table(name = "import_batch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImportBatchEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String filename;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ImportBatchStatus status;

    @Column(name = "imported_rows", nullable = false)
    private int importedRows;

    @Column(name = "rejected_rows", nullable = false)
    private int rejectedRows;

    @Column(name = "completed_at")
    private Instant completedAt;

    public ImportBatchEntity(String filename) {
        this.id = UUID.randomUUID();
        this.filename = filename;
        this.status = ImportBatchStatus.PROCESSING;
    }

    public void complete(int importedRows, int rejectedRows) {
        this.importedRows = importedRows;
        this.rejectedRows = rejectedRows;
        this.status = ImportBatchStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

}
