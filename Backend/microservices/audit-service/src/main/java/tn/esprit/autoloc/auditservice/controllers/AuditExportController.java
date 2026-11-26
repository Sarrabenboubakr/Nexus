package tn.esprit.autoloc.auditservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.auditservice.entities.AuditExport;
import tn.esprit.autoloc.auditservice.services.AuditExportService;

import java.util.List;

@RestController
@RequestMapping("/api/audit/exports")
@RequiredArgsConstructor
public class AuditExportController {

    private final AuditExportService service;

    @PostMapping
    public ResponseEntity<AuditExport> create(@Valid @RequestBody AuditExport export) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(export));
    }

    @GetMapping
    public List<AuditExport> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AuditExport findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public AuditExport update(@PathVariable String id, @Valid @RequestBody AuditExport export) {
        return service.update(id, export);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}