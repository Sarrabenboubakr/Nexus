package tn.esprit.autoloc.auditservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.auditservice.entities.AuditSecurity;

import java.util.List;

@Repository
public interface AuditSecurityRepository extends JpaRepository<AuditSecurity, String> {

    List<AuditSecurity> findBySeverity(String severity);
}