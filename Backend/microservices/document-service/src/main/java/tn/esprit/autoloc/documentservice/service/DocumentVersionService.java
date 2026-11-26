package tn.esprit.autoloc.documentservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.documentservice.dto.DocumentVersionRequest;
import tn.esprit.autoloc.documentservice.model.Document;
import tn.esprit.autoloc.documentservice.model.DocumentVersion;
import tn.esprit.autoloc.documentservice.repository.DocumentRepository;
import tn.esprit.autoloc.documentservice.repository.DocumentVersionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentVersionService {

    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentRepository documentRepository;

    public DocumentVersionService(DocumentVersionRepository documentVersionRepository,
                                  DocumentRepository documentRepository) {
        this.documentVersionRepository = documentVersionRepository;
        this.documentRepository = documentRepository;
    }

    public List<DocumentVersion> findAll() {
        return documentVersionRepository.findAll();
    }

    public DocumentVersion findById(UUID id) {
        return documentVersionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Document version not found"));
    }

    @Transactional
    public DocumentVersion create(DocumentVersionRequest request) {
        Document document = findDocument(request.documentId());
        DocumentVersion version = new DocumentVersion(request.versionNumber(), request.fileUrl(),
                request.storageKey(), request.fileSize(), request.sha256Hash(), request.uploadedBy(),
                request.validationStatus(), Boolean.TRUE.equals(request.locked()));
        document.addVersion(version);
        return documentVersionRepository.save(version);
    }

    public DocumentVersion update(UUID id, DocumentVersionRequest request) {
        DocumentVersion version = findById(id);
        if (!version.getDocumentId().equals(request.documentId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentId cannot be changed");
        }
        version.update(request.versionNumber(), request.fileUrl(), request.storageKey(), request.fileSize(),
                request.sha256Hash(), request.uploadedBy(), request.validationStatus(),
                Boolean.TRUE.equals(request.locked()));
        return documentVersionRepository.save(version);
    }

    @Transactional
    public void delete(UUID id) {
        DocumentVersion version = findById(id);
        Document document = findDocument(version.getDocumentId());
        if (id.equals(document.getCurrentVersionId())) {
            document.setCurrentVersion(null);
            documentRepository.saveAndFlush(document);
        }
        documentVersionRepository.delete(version);
    }

    private Document findDocument(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }
}