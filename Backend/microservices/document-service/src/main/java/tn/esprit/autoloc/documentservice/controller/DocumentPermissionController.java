package tn.esprit.autoloc.documentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.autoloc.documentservice.dto.DocumentPermissionRequest;
import tn.esprit.autoloc.documentservice.model.DocumentPermission;
import tn.esprit.autoloc.documentservice.service.DocumentPermissionService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/document-permissions")
@Tag(name = "Document permissions", description = "Gestion des permissions des documents")
public class DocumentPermissionController {

    private final DocumentPermissionService documentPermissionService;

    public DocumentPermissionController(DocumentPermissionService documentPermissionService) {
        this.documentPermissionService = documentPermissionService;
    }

    @GetMapping
    @Operation(summary = "Lister les permissions")
    public List<DocumentPermission> findAll() {
        return documentPermissionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une permission")
    public DocumentPermission findById(@PathVariable UUID id) {
        return documentPermissionService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Créer une permission")
    public ResponseEntity<DocumentPermission> create(@Valid @RequestBody DocumentPermissionRequest request) {
        DocumentPermission permission = documentPermissionService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(permission.getId())
                .toUri();
        return ResponseEntity.created(location).body(permission);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une permission")
    public DocumentPermission update(@PathVariable UUID id,
                                    @Valid @RequestBody DocumentPermissionRequest request) {
        return documentPermissionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une permission")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        documentPermissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}