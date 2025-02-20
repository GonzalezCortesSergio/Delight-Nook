package com.salesianostriana.dam.delight_nook.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UsuarioResponseDto(
        UUID id,
        String username,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String activationToken,
        String nombreCompleto,
        String avatar,
        Set<String> roles
) {

    public static UsuarioResponseDto of (Usuario usuario) {

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                null,
                null,
                usuario.getActivationToken(),
                usuario.getNombreCompleto(),
                usuario.getAvatar(),
                usuario.getRoles().stream()
                        .map(UserRole::toString)
                        .collect(Collectors.toSet())
        );
    }

    public static UsuarioResponseDto of (Usuario usuario, String token, String refreshToken) {

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                token,
                refreshToken,
                null,
                usuario.getNombreCompleto(),
                usuario.getAvatar(),
                usuario.getRoles().stream()
                        .map(UserRole::toString)
                        .collect(Collectors.toSet())
        );
    }
}
