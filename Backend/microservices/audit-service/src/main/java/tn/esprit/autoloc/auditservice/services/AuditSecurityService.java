package tn.esprit.autoloc.auditservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.auditservice.entities.AuditSecurity;
import tn.esprit.autoloc.auditservice.repositories.AuditSecurityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditSecurityService {

    private final AuditSecurityRepository repository;

    public AuditSecurity create(AuditSecurity security) {
        security.setId(null);
        return repository.save(security);
    }

    public List<AuditSecurity> findAll() {
        return repository.findAll();
    }

    public AuditSecurity findById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "AuditSecurity introuvable : " + id));
    }

    public List<AuditSecurity> findBySeverity(String severity) {
        return repository.findBySeverity(severity);
    }

    public AuditSecurity update(String id, AuditSecurity data) {
        AuditSecurity existing = findById(id);
        existing.setEventId(data.getEventId());
        existing.setUserId(data.getUserId());
        existing.setOrganizationId(data.getOrganizationId());
        existing.setSecurityType(data.getSecurityType());
        existing.setSeverity(data.getSeverity());
        existing.setIpAddress(data.getIpAddress());
        existing.setDetails(data.getDetails());
        return repository.save(existing);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }
}