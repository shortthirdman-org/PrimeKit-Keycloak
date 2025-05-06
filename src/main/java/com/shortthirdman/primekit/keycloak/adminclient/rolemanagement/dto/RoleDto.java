package com.shortthirdman.primekit.keycloak.adminclient.rolemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RoleDto(@NotNull @NotBlank String name, String description) {
}
