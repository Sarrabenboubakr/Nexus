package tn.esprit.autoloc.collaborationservice.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private String id;

    @Indexed
    private String discussionId;       // association N-1 vers Discussion

    @NotBlank(message = "L'auteur est obligatoire")
    private String authorId;

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;

    private LocalDateTime sentAt;

    @Valid
    private List<Mention> mentions = new ArrayList<>();   // association 1-N embarquée
}