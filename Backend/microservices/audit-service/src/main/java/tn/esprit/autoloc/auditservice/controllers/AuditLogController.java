package tn.esprit.autoloc.auditservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.auditservice.entities.AuditLog;
import tn.esprit.autoloc.auditservice.services.AuditLogService;

import java.util.List;

@RestController
@RequestMapping("/api/audit/logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    @PostMapping
    public ResponseEntity<AuditLog> create(@Valid @RequestBody AuditLog auditLog) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(auditLog));
    }

    @GetMapping
    public List<AuditLog> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AuditLog findById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<AuditLog> findByUserId(@PathVariable String userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/organization/{organizationId}")
    public List<AuditLog> findByOrganizationId(@PathVariable String organizationId) {
        return service.findByOrganizationId(organizationId);
    }

    @PutMapping("/{id}")
    public AuditLog update(@PathVariable String id, @Valid @RequestBody AuditLog auditLog) {
        return service.update(id, auditLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}