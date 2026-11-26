package tn.esprit.autoloc.auditservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.auditservice.entities.AuditExport;
import tn.esprit.autoloc.auditservice.repositories.AuditExportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditExportService {

    private final AuditExportRepository repository;

    public AuditExport create(AuditExport export) {
        export.setId(null);
        return repository.save(export);
    }

    public List<AuditExport> findAll() {
        return repository.findAll();
    }

    public AuditExport findById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "AuditExport introuvable : " + id));
    }

    public AuditExport update(String id, AuditExport data) {
        AuditExport existing = findById(id);
        existing.setOrganizationId(data.getOrganizationId());
        existing.setRequestedBy(data.getRequestedBy());
        existing.setExportFormat(data.getExportFormat());
        existing.setFilters(data.getFilters());
        existing.setFileUrl(data.getFileUrl());
        if (data.getStatus() != null) existing.setStatus(data.getStatus());
        existing.setCompletedAt(data.getCompletedAt());
        return repository.save(existing);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }
}