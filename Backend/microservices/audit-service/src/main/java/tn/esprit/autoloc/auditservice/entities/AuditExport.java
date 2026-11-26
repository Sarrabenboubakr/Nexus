package tn.esprit.autoloc.auditservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "audit_exports")
public class AuditExport {
    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 36)
    private String organizationId;

    @NotBlank
    @Column(nullable = false, length = 36)
    private String requestedBy;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String exportFormat;       // CSV, PDF...

    @Column(columnDefinition = "TEXT")
    private String filters;

    @Column(length = 500)
    private String fileUrl;

    @Column(length = 30)
    private String status;             // PENDING, COMPLETED, FAILED

    private Instant createdAt;
    private Instant completedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = Instant.now();
        if (status == null) status = "PENDING";
    }
}