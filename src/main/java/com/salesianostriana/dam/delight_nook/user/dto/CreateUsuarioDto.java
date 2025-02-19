package com.salesianostriana.dam.delight_nook.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUsuarioDto(
        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        String username,
        @NotBlank
        String nombreCompleto,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String avatar
) {
}
