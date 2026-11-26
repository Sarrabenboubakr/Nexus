package tn.esprit.autoloc.userservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.autoloc.userservice.model.User;
import tn.esprit.autoloc.userservice.repository.UserRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:users;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false"
})
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearUsers() {
        userRepository.deleteAll();
    }

    @Test
    void shouldPerformUserCrud() throws Exception {
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Amina",
                                  "lastName": "Ben Ali",
                                  "email": "AMINA@EXAMPLE.COM",
                                  "phone": "+216 20 000 000"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Amina"))
                .andExpect(jsonPath("$.email").value("amina@example.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode createdUser = objectMapper.readTree(response);
        UUID id = UUID.fromString(createdUser.get("id").asText());

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+216 20 000 000"));

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Amina",
                                  "lastName": "Trabelsi",
                                  "email": "amina@example.com",
                                  "phone": null
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Trabelsi"))
                .andExpect(jsonPath("$.phone").doesNotExist());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("amina@example.com"));

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.existsById(id));
    }

    @Test
    void shouldExposeCrudOperationsInOpenApiDocumentation() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/users'].get").exists())
                .andExpect(jsonPath("$.paths['/api/users'].post").exists())
                .andExpect(jsonPath("$.paths['/api/users/{id}'].get").exists())
                .andExpect(jsonPath("$.paths['/api/users/{id}'].put").exists())
                .andExpect(jsonPath("$.paths['/api/users/{id}'].delete").exists());

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Amina",
                                  "lastName": "Ben Ali",
                                  "email": "not-an-email",
                                  "phone": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        userRepository.saveAndFlush(new User("Amina", "Ben Ali", "amina@example.com", null));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Amina",
                                  "lastName": "Ben Ali",
                                  "email": "amina@example.com",
                                  "phone": null
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnNotFoundForUnknownUser() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldEnforceUniqueEmailConstraint() {
        userRepository.saveAndFlush(new User("Amina", "Ben Ali", "amina@example.com", null));

        assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(new User("Other", "User", "amina@example.com", null)));
    }
}