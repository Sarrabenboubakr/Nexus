package tn.esprit.autoloc.auditservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "audit_events")
public class AuditEvent {
    @Id
    @Column(length = 36)
    private String id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String eventType;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String sourceService;

    @Column(length = 36)
    private String organizationId;

    @Column(length = 100)
    private String entityType;

    @Column(length = 36)
    private String entityId;

    @Column(columnDefinition = "TEXT")
    private String eventData;

    private Instant occurredAt;
    private Instant receivedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        if (occurredAt == null) occurredAt = now;
        if (receivedAt == null) receivedAt = now;
    }
}