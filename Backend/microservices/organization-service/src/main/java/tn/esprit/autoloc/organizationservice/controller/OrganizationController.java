package tn.esprit.autoloc.organizationservice.controller;

import tn.esprit.autoloc.organizationservice.exception.NotFoundException;
import tn.esprit.autoloc.organizationservice.model.Organization;
import tn.esprit.autoloc.organizationservice.repository.OrganizationRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository repository;

    public OrganizationController(OrganizationRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Organization> create(@Valid @RequestBody Organization org) {
        org.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(org));
    }

    @GetMapping
    public List<Organization> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Organization getById(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organisation introuvable : " + id));
    }

    @PutMapping("/{id}")
    public Organization update(@PathVariable UUID id, @Valid @RequestBody Organization data) {
        Organization org = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organisation introuvable : " + id));
        org.setName(data.getName());
        org.setDescription(data.getDescription());
        org.setEmail(data.getEmail());
        org.setPhone(data.getPhone());
        org.setAddress(data.getAddress());
        org.setLogoUrl(data.getLogoUrl());
        if (data.getStatus() != null) org.setStatus(data.getStatus());
        return repository.save(org);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Organisation introuvable : " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}