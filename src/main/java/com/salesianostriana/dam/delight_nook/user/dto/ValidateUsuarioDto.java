package com.salesianostriana.dam.delight_nook.user.dto;

import com.salesianostriana.dam.delight_nook.security.validation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;

@PasswordMatches
public record ValidateUsuarioDto(
        @NotBlank
        String password,
        @NotBlank
        String verifyPassword,
        @NotBlank
        String activationToken
) {
}
