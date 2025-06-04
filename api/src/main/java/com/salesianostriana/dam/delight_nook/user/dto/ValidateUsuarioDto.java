package com.salesianostriana.dam.delight_nook.user.dto;

import com.salesianostriana.dam.delight_nook.security.validation.annotation.FieldsValueMatch;
import jakarta.validation.constraints.NotBlank;

@FieldsValueMatch(fieldMatch = "verifyPassword", field = "password", message = "Los valores de password y verifyPassword no coinciden")
public record ValidateUsuarioDto(
        @NotBlank
        String password,
        @NotBlank
        String verifyPassword,
        @NotBlank
        String activationToken
) {
}
