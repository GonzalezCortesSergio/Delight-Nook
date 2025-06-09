package com.salesianostriana.dam.delight_nook.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public record UsuarioResponseDto(
        String username,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken,
        String nombreCompleto,
        String avatar,
        Set<String> roles,
        boolean enabled
) {

    public static UsuarioResponseDto of (Usuario usuario, String avatar) {

        return new UsuarioResponseDto(
                usuario.getUsername(),
                null,
                null,
                usuario.getNombreCompleto(),
                avatar,
                usuario.getRoles().stream()
                        .map(UserRole::toString)
                        .collect(Collectors.toSet()),
                usuario.isEnabled()
        );
    }

    public static UsuarioResponseDto of (Usuario usuario, String token, String refreshToken, String avatar) {

        return new UsuarioResponseDto(
                usuario.getUsername(),
                token,
                refreshToken,
                usuario.getNombreCompleto(),
                avatar,
                usuario.getRoles().stream()
                        .map(UserRole::toString)
                        .collect(Collectors.toSet()),
                usuario.isEnabled()
        );
    }
}
