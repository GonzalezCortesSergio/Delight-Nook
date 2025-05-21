package com.salesianostriana.dam.delight_nook.user.dto;

import com.salesianostriana.dam.delight_nook.security.validation.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUsuarioDto(
        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        @UniqueUsername
        String username,
        @NotBlank
        String nombreCompleto,
        @Email
        @NotBlank
        String email
) {
}
