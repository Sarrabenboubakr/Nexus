package tn.esprit.autoloc.auditservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.auditservice.entities.AuditSecurity;
import tn.esprit.autoloc.auditservice.services.AuditSecurityService;

import java.util.List;

@RestController
@RequestMapping("/api/audit/security")
@RequiredArgsConstructor
public class AuditSecurityController {

    private final AuditSecurityService service;

    @PostMapping
    public ResponseEntity<AuditSecurity> create(@Valid @RequestBody AuditSecurity security) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(security));
    }

    @GetMapping
    public List<AuditSecurity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AuditSecurity findById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/severity/{severity}")
    public List<AuditSecurity> findBySeverity(@PathVariable String severity) {
        return service.findBySeverity(severity);
    }

    @PutMapping("/{id}")
    public AuditSecurity update(@PathVariable String id, @Valid @RequestBody AuditSecurity security) {
        return service.update(id, security);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}