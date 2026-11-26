package tn.esprit.autoloc.documentservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.documentservice.dto.DocumentPermissionRequest;
import tn.esprit.autoloc.documentservice.model.Document;
import tn.esprit.autoloc.documentservice.model.DocumentPermission;
import tn.esprit.autoloc.documentservice.repository.DocumentPermissionRepository;
import tn.esprit.autoloc.documentservice.repository.DocumentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentPermissionService {

    private final DocumentPermissionRepository documentPermissionRepository;
    private final DocumentRepository documentRepository;

    public DocumentPermissionService(DocumentPermissionRepository documentPermissionRepository,
                                     DocumentRepository documentRepository) {
        this.documentPermissionRepository = documentPermissionRepository;
        this.documentRepository = documentRepository;
    }

    public List<DocumentPermission> findAll() {
        return documentPermissionRepository.findAll();
    }

    public DocumentPermission findById(UUID id) {
        return documentPermissionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Document permission not found"));
    }

    @Transactional
    public DocumentPermission create(DocumentPermissionRequest request) {
        Document document = findDocument(request.documentId());
        DocumentPermission permission = new DocumentPermission(request.userId(), request.permissionType(),
                request.grantedBy());
        document.addPermission(permission);
        return documentPermissionRepository.save(permission);
    }

    public DocumentPermission update(UUID id, DocumentPermissionRequest request) {
        DocumentPermission permission = findById(id);
        if (!permission.getDocumentId().equals(request.documentId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentId cannot be changed");
        }
        permission.update(request.userId(), request.permissionType(), request.grantedBy());
        return documentPermissionRepository.save(permission);
    }

    public void delete(UUID id) {
        documentPermissionRepository.delete(findById(id));
    }

    private Document findDocument(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }
}