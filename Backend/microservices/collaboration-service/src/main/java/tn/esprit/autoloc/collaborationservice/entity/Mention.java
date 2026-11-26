package tn.esprit.autoloc.collaborationservice.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mention {

    @NotBlank(message = "L'utilisateur mentionné est obligatoire")
    private String mentionedUserId;

    private LocalDateTime mentionedAt;
}