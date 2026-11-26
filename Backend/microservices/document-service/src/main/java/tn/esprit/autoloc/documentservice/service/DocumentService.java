package tn.esprit.autoloc.documentservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.documentservice.dto.DocumentRequest;
import tn.esprit.autoloc.documentservice.model.Document;
import tn.esprit.autoloc.documentservice.repository.DocumentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document findById(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    public Document create(DocumentRequest request) {
        Document document = new Document(
                request.projectId(),
                request.name(),
                request.description(),
                request.fileType(),
                request.uploadedBy(),
                request.status());
        return documentRepository.save(document);
    }

    public Document update(UUID id, DocumentRequest request) {
        Document document = findById(id);
        document.update(request.projectId(), request.name(), request.description(), request.fileType(),
                request.uploadedBy(), request.status());
        return documentRepository.save(document);
    }

    @Transactional
    public void delete(UUID id) {
        Document document = findById(id);
        document.setCurrentVersion(null);
        documentRepository.flush();
        documentRepository.delete(document);
    }
}