package tn.esprit.autoloc.documentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DocumentPermissionRequest(
        @NotNull UUID documentId,
        @NotNull UUID userId,
        @NotBlank @Size(max = 30) String permissionType,
        UUID grantedBy) {
}