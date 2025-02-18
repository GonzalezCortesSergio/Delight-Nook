package com.salesianostriana.dam.delight_nook.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUsuarioDto(
        @NotBlank
        String username,
        @NotBlank
        String nombreCompleto,
        @Email
        String email,
        @NotBlank
        String avatar
) {
}
