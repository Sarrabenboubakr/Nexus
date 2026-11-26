package tn.esprit.autoloc.documentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DocumentRequest(
        UUID projectId,
        @NotBlank @Size(max = 255) String name,
        String description,
        @Size(max = 50) String fileType,
        UUID uploadedBy,
        @Size(max = 30) String status) {
}