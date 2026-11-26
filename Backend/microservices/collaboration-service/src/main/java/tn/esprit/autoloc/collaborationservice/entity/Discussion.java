package tn.esprit.autoloc.collaborationservice.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "discussions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discussion {

    @Id
    private String id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotBlank(message = "Le créateur est obligatoire")
    private String createdBy;          // id de l'utilisateur (géré par un autre microservice)

    private LocalDateTime createdAt;

    private boolean closed;
}