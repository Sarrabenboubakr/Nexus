package tn.esprit.autoloc.auditservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.auditservice.entities.AuditLog;
import tn.esprit.autoloc.auditservice.repositories.AuditLogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLog create(AuditLog auditLog) {
        auditLog.setId(null);
        return repository.save(auditLog);
    }

    public List<AuditLog> findAll() {
        return repository.findAll();
    }

    public AuditLog findById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "AuditLog introuvable : " + id));
    }

    public List<AuditLog> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public List<AuditLog> findByOrganizationId(String organizationId) {
        return repository.findByOrganizationId(organizationId);
    }

    public AuditLog update(String id, AuditLog data) {
        AuditLog existing = findById(id);
        existing.setEventId(data.getEventId());
        existing.setUserId(data.getUserId());
        existing.setOrganizationId(data.getOrganizationId());
        existing.setAction(data.getAction());
        existing.setEntityType(data.getEntityType());
        existing.setEntityId(data.getEntityId());
        existing.setDescription(data.getDescription());
        existing.setPreviousHash(data.getPreviousHash());
        existing.setCurrentHash(data.getCurrentHash());
        return repository.save(existing);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }
}