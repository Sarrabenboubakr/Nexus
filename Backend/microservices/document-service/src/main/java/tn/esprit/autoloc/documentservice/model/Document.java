package tn.esprit.autoloc.documentservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "project_id")
    private UUID projectId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "uploaded_by")
    private UUID uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_version_id",
            foreignKey = @ForeignKey(name = "fk_documents_current_version"))
    @JsonIgnore
    private DocumentVersion currentVersion;

    @Column(length = 30)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<DocumentVersion> versions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<DocumentPermission> permissions = new LinkedHashSet<>();

    protected Document() {
    }

    public Document(UUID projectId, String name, String description, String fileType,
                    UUID uploadedBy, String status) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.fileType = fileType;
        this.uploadedBy = uploadedBy;
        this.status = status;
    }

    public void update(UUID projectId, String name, String description, String fileType,
                       UUID uploadedBy, String status) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.fileType = fileType;
        this.uploadedBy = uploadedBy;
        this.status = status;
    }

    public void setCurrentVersion(DocumentVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void addVersion(DocumentVersion version) {
        versions.add(version);
        version.setDocument(this);
    }

    public void addPermission(DocumentPermission permission) {
        permissions.add(permission);
        permission.setDocument(this);
    }

    @PrePersist
    private void setCreationTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    private void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFileType() {
        return fileType;
    }

    public UUID getUploadedBy() {
        return uploadedBy;
    }

    public UUID getCurrentVersionId() {
        return currentVersion == null ? null : currentVersion.getId();
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}