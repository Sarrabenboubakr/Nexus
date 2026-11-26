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
import tn.esprit.autoloc.documentservice.dto.DocumentRequest;
import tn.esprit.autoloc.documentservice.model.Document;
import tn.esprit.autoloc.documentservice.service.DocumentService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Gestion des métadonnées des documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    @Operation(summary = "Lister les documents")
    public List<Document> findAll() {
        return documentService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un document")
    public Document findById(@PathVariable UUID id) {
        return documentService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Créer un document")
    public ResponseEntity<Document> create(@Valid @RequestBody DocumentRequest request) {
        Document document = documentService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(document.getId())
                .toUri();
        return ResponseEntity.created(location).body(document);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un document")
    public Document update(@PathVariable UUID id, @Valid @RequestBody DocumentRequest request) {
        return documentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un document")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}