package tn.esprit.autoloc.auditservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.auditservice.entities.AuditEvent;
import tn.esprit.autoloc.auditservice.repositories.AuditEventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditEventService {

    private final AuditEventRepository repository;

    public AuditEvent create(AuditEvent event) {
        event.setId(null);
        return repository.save(event);
    }

    public List<AuditEvent> findAll() {
        return repository.findAll();
    }

    public AuditEvent findById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "AuditEvent introuvable : " + id));
    }

    public AuditEvent update(String id, AuditEvent data) {
        AuditEvent existing = findById(id);
        existing.setEventType(data.getEventType());
        existing.setSourceService(data.getSourceService());
        existing.setOrganizationId(data.getOrganizationId());
        existing.setEntityType(data.getEntityType());
        existing.setEntityId(data.getEntityId());
        existing.setEventData(data.getEventData());
        if (data.getOccurredAt() != null) existing.setOccurredAt(data.getOccurredAt());
        return repository.save(existing);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }
}