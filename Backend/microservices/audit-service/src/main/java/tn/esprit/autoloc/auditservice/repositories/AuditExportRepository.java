package tn.esprit.autoloc.auditservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.auditservice.entities.AuditExport;

@Repository
public interface AuditExportRepository extends JpaRepository<AuditExport, String> {
}