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
@Table(name = "document_versions")
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_versions_document"))
    @JsonIgnore
    private Document document;

    @Column(name = "version_number")
    private Integer versionNumber;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "storage_key", length = 255)
    private String storageKey;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "sha256_hash", length = 64)
    private String sha256Hash;

    @Column(name = "uploaded_by")
    private UUID uploadedBy;

    @Column(name = "validation_status", length = 30)
    private String validationStatus;

    @Column(name = "is_locked")
    private boolean locked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected DocumentVersion() {
    }

    public DocumentVersion(Integer versionNumber, String fileUrl, String storageKey, Long fileSize,
                           String sha256Hash, UUID uploadedBy, String validationStatus, boolean locked) {
        this.versionNumber = versionNumber;
        this.fileUrl = fileUrl;
        this.storageKey = storageKey;
        this.fileSize = fileSize;
        this.sha256Hash = sha256Hash;
        this.uploadedBy = uploadedBy;
        this.validationStatus = validationStatus;
        this.locked = locked;
    }

    public void update(Integer versionNumber, String fileUrl, String storageKey, Long fileSize,
                       String sha256Hash, UUID uploadedBy, String validationStatus, boolean locked) {
        this.versionNumber = versionNumber;
        this.fileUrl = fileUrl;
        this.storageKey = storageKey;
        this.fileSize = fileSize;
        this.sha256Hash = sha256Hash;
        this.uploadedBy = uploadedBy;
        this.validationStatus = validationStatus;
        this.locked = locked;
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

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getSha256Hash() {
        return sha256Hash;
    }

    public UUID getUploadedBy() {
        return uploadedBy;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public boolean isLocked() {
        return locked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}