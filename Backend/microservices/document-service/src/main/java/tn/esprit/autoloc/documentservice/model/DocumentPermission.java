package tn.esprit.autoloc.documentservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_permissions")
public class DocumentPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_permissions_document"))
    @JsonIgnore
    private Document document;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "permission_type", length = 30)
    private String permissionType;

    @Column(name = "granted_by")
    private UUID grantedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected DocumentPermission() {
    }

    public DocumentPermission(UUID userId, String permissionType, UUID grantedBy) {
        this.userId = userId;
        this.permissionType = permissionType;
        this.grantedBy = grantedBy;
    }

    public void update(UUID userId, String permissionType, UUID grantedBy) {
        this.userId = userId;
        this.permissionType = permissionType;
        this.grantedBy = grantedBy;
    }

    void setDocument(Document document) {
        this.document = document;
    }

    @PrePersist
    private void setCreationTimestamp() {
        createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getDocumentId() {
        return document == null ? null : document.getId();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public UUID getGrantedBy() {
        return grantedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}