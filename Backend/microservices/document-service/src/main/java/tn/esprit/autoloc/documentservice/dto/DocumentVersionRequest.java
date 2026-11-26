package tn.esprit.autoloc.documentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DocumentVersionRequest(
        @NotNull UUID documentId,
        @NotNull @Positive Integer versionNumber,
        @Size(max = 500) String fileUrl,
        @Size(max = 255) String storageKey,
        @PositiveOrZero Long fileSize,
        @Size(min = 64, max = 64) String sha256Hash,
        UUID uploadedBy,
        @Size(max = 30) String validationStatus,
        Boolean locked) {
}