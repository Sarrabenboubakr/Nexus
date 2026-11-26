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
import tn.esprit.autoloc.documentservice.dto.DocumentVersionRequest;
import tn.esprit.autoloc.documentservice.model.DocumentVersion;
import tn.esprit.autoloc.documentservice.service.DocumentVersionService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/document-versions")
@Tag(name = "Document versions", description = "Gestion des versions de documents")
public class DocumentVersionController {

    private final DocumentVersionService documentVersionService;

    public DocumentVersionController(DocumentVersionService documentVersionService) {
        this.documentVersionService = documentVersionService;
    }

    @GetMapping
    @Operation(summary = "Lister les versions")
    public List<DocumentVersion> findAll() {
        return documentVersionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une version")
    public DocumentVersion findById(@PathVariable UUID id) {
        return documentVersionService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Créer une version")
    public ResponseEntity<DocumentVersion> create(@Valid @RequestBody DocumentVersionRequest request) {
        DocumentVersion version = documentVersionService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(version.getId())
                .toUri();
        return ResponseEntity.created(location).body(version);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une version")
    public DocumentVersion update(@PathVariable UUID id, @Valid @RequestBody DocumentVersionRequest request) {
        return documentVersionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une version")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        documentVersionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}