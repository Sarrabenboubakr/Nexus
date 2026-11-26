package tn.esprit.autoloc.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.documentservice.model.DocumentPermission;

import java.util.UUID;

public interface DocumentPermissionRepository extends JpaRepository<DocumentPermission, UUID> {
}