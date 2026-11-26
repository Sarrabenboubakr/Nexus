package tn.esprit.autoloc.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.documentservice.model.DocumentVersion;

import java.util.UUID;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, UUID> {
}