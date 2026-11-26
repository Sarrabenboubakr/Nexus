package tn.esprit.autoloc.auditservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.auditservice.entities.AuditEvent;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, String> {
}