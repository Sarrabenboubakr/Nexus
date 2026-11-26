package tn.esprit.autoloc.auditservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "audit_security")
public class AuditSecurity {
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
    private String securityType;       // ex. LOGIN_FAILED

    @Column(length = 20)
    private String severity;           // LOW, MEDIUM, HIGH, CRITICAL

    @Column(length = 45)
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String details;

    private Instant detectedAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
        if (detectedAt == null) detectedAt = Instant.now();
    }
}