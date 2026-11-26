package tn.esprit.autoloc.auditservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 36)
    private String eventId;

    @Column(length = 36)
    private String userId;

    @Column(length = 36)
    private String organizationId;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String action;

    @Column(length = 100)
    private String entityType;

    @Column(length = 36)
    private String entityId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 64)
    private String previousHash;

    @Column(length = 64)
    private String currentHash;

    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = Instant.now();
    }
}