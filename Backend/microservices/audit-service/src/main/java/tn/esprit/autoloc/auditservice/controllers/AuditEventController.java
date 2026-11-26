package tn.esprit.autoloc.auditservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.auditservice.entities.AuditEvent;
import tn.esprit.autoloc.auditservice.services.AuditEventService;

import java.util.List;

@RestController
@RequestMapping("/api/audit/events")
@RequiredArgsConstructor
public class AuditEventController {

    private final AuditEventService service;

    @PostMapping
    public ResponseEntity<AuditEvent> create(@Valid @RequestBody AuditEvent event) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(event));
    }

    @GetMapping
    public List<AuditEvent> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AuditEvent findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public AuditEvent update(@PathVariable String id, @Valid @RequestBody AuditEvent event) {
        return service.update(id, event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}