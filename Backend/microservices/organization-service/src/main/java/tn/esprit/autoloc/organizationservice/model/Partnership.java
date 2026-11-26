package tn.esprit.autoloc.organizationservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "partnership",
        uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "partner_organization_id"}))
public class Partnership {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @NotNull
    @Column(name = "partner_organization_id", nullable = false)
    private UUID partnerOrganizationId;

    @Column(name = "invited_by")
    private UUID invitedBy;

    @Column(length = 30)
    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getOrganizationId() { return organizationId; }
    public void setOrganizationId(UUID organizationId) { this.organizationId = organizationId; }
    public UUID getPartnerOrganizationId() { return partnerOrganizationId; }
    public void setPartnerOrganizationId(UUID partnerOrganizationId) { this.partnerOrganizationId = partnerOrganizationId; }
    public UUID getInvitedBy() { return invitedBy; }
    public void setInvitedBy(UUID invitedBy) { this.invitedBy = invitedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
}