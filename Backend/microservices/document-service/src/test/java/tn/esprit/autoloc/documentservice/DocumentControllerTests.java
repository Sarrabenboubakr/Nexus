package tn.esprit.autoloc.documentservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.autoloc.documentservice.model.Document;
import tn.esprit.autoloc.documentservice.model.DocumentPermission;
import tn.esprit.autoloc.documentservice.model.DocumentVersion;
import tn.esprit.autoloc.documentservice.repository.DocumentPermissionRepository;
import tn.esprit.autoloc.documentservice.repository.DocumentRepository;
import tn.esprit.autoloc.documentservice.repository.DocumentVersionRepository;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:documents;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "eureka.client.enabled=false"
})
@AutoConfigureMockMvc
class DocumentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentVersionRepository documentVersionRepository;

    @Autowired
    private DocumentPermissionRepository documentPermissionRepository;

    @BeforeEach
    void clearDocuments() {
        documentRepository.deleteAll();
    }

    @Test
    void shouldPerformDocumentCrud() throws Exception {
        String response = mockMvc.perform(post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "projectId": "d8e60a7e-41b1-42eb-889a-d2001e3ba111",
                                  "name": "Contrat",
                                  "description": "Contrat de location",
                                  "fileType": "pdf",
                                  "uploadedBy": "3f99ae36-e97d-4c85-9157-7b98ca95f740",
                                  "status": "ACTIVE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Contrat"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode createdDocument = objectMapper.readTree(response);
        String id = createdDocument.get("id").asText();

        mockMvc.perform(get("/api/documents/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Contrat de location"))
                .andExpect(jsonPath("$.fileType").value("pdf"));

        mockMvc.perform(put("/api/documents/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "projectId": "d8e60a7e-41b1-42eb-889a-d2001e3ba111",
                                  "name": "Contrat mis à jour",
                                  "description": "Version signée",
                                  "fileType": "pdf",
                                  "uploadedBy": "3f99ae36-e97d-4c85-9157-7b98ca95f740",
                                  "status": "ACTIVE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Contrat mis à jour"));

        mockMvc.perform(get("/api/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Contrat mis à jour"));

        mockMvc.perform(delete("/api/documents/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/documents/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPerformDocumentVersionCrudAndClearCurrentVersionOnDelete() throws Exception {
        Document document = documentRepository.saveAndFlush(new Document(UUID.randomUUID(), "Plan", null,
                "pdf", UUID.randomUUID(), "ACTIVE"));
        String documentId = document.getId().toString();
        String request = """
                {
                  "documentId": "%s",
                  "versionNumber": 1,
                  "fileUrl": "https://example.test/plan.pdf",
                  "storageKey": "plan/1",
                  "fileSize": 2048,
                  "sha256Hash": "%s",
                  "uploadedBy": "%s",
                  "validationStatus": "PENDING",
                  "locked": false
                }
                """.formatted(documentId, "a".repeat(64), UUID.randomUUID());

        String response = mockMvc.perform(post("/api/document-versions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentId").value(documentId))
                .andExpect(jsonPath("$.versionNumber").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String versionId = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/api/document-versions/{id}", versionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storageKey").value("plan/1"));

        String updatedRequest = request.replace("\"versionNumber\": 1", "\"versionNumber\": 2")
                .replace("https://example.test/plan.pdf", "https://example.test/plan-v2.pdf")
                .replace("\"locked\": false", "\"locked\": true");
        mockMvc.perform(put("/api/document-versions/{id}", versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versionNumber").value(2))
                .andExpect(jsonPath("$.locked").value(true));

        mockMvc.perform(get("/api/document-versions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].versionNumber").value(2));

        Document persistedDocument = documentRepository.findById(document.getId()).orElseThrow();
        persistedDocument.setCurrentVersion(documentVersionRepository.findById(UUID.fromString(versionId))
                .orElseThrow());
        documentRepository.saveAndFlush(persistedDocument);

        mockMvc.perform(delete("/api/document-versions/{id}", versionId))
                .andExpect(status().isNoContent());
        assertNull(documentRepository.findById(document.getId()).orElseThrow().getCurrentVersionId());
        mockMvc.perform(get("/api/document-versions/{id}", versionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPerformDocumentPermissionCrud() throws Exception {
        Document document = documentRepository.saveAndFlush(new Document(UUID.randomUUID(), "Budget", null,
                "xlsx", UUID.randomUUID(), "ACTIVE"));
        String documentId = document.getId().toString();
        String userId = UUID.randomUUID().toString();
        String request = """
                {
                  "documentId": "%s",
                  "userId": "%s",
                  "permissionType": "READ",
                  "grantedBy": "%s"
                }
                """.formatted(documentId, userId, UUID.randomUUID());

        String response = mockMvc.perform(post("/api/document-permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentId").value(documentId))
                .andExpect(jsonPath("$.permissionType").value("READ"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String permissionId = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/api/document-permissions/{id}", permissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId));

        String updatedRequest = request.replace("\"permissionType\": \"READ\"", "\"permissionType\": \"WRITE\"");
        mockMvc.perform(put("/api/document-permissions/{id}", permissionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permissionType").value("WRITE"));

        mockMvc.perform(get("/api/document-permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].permissionType").value("WRITE"));

        mockMvc.perform(delete("/api/document-permissions/{id}", permissionId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/document-permissions/{id}", permissionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectChildrenForUnknownDocumentsAndInvalidPermissions() throws Exception {
        mockMvc.perform(post("/api/document-versions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "documentId": "00000000-0000-0000-0000-000000000001",
                                  "versionNumber": 1
                                }
                                """))
                .andExpect(status().isNotFound());

        Document document = documentRepository.saveAndFlush(new Document(UUID.randomUUID(), "Plan", null,
                "pdf", UUID.randomUUID(), "ACTIVE"));
        mockMvc.perform(post("/api/document-permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "documentId": "%s",
                                  "userId": "5dc6f0f3-e5d8-47d7-a110-30294e5ac11c",
                                  "permissionType": " "
                                }
                                """.formatted(document.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldPersistDocumentVersionAndPermissionRelations() {
        UUID projectId = UUID.randomUUID();
        UUID uploaderId = UUID.randomUUID();
        Document document = documentRepository.saveAndFlush(
                new Document(projectId, "Plan", "Plan du projet", "pdf", uploaderId, "ACTIVE"));

        DocumentVersion version = new DocumentVersion(1, "https://example.test/plan.pdf", "plan/1",
                2048L, "a".repeat(64), uploaderId, "APPROVED", false);
        document.addVersion(version);
        documentVersionRepository.saveAndFlush(version);

        document.setCurrentVersion(version);
        document.addPermission(new DocumentPermission(UUID.randomUUID(), "READ", uploaderId));
        documentRepository.saveAndFlush(document);

        Document persistedDocument = documentRepository.findById(document.getId()).orElseThrow();
        assertNotNull(version.getId());
        assertEquals(version.getId(), persistedDocument.getCurrentVersionId());
        assertEquals(document.getId(), documentVersionRepository.findById(version.getId())
                .orElseThrow().getDocumentId());
        assertEquals(document.getId(), documentPermissionRepository.findAll().get(0).getDocumentId());
    }

    @Test
    void shouldRejectDocumentWithoutName() throws Exception {
        mockMvc.perform(post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "  "
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldExposeOpenApiDocumentation() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/documents']").exists())
                .andExpect(jsonPath("$.paths['/api/document-versions']").exists())
                .andExpect(jsonPath("$.paths['/api/document-permissions']").exists());
    }
}