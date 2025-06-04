package com.salesianostriana.dam.delight_nook.security.jwt.refresh;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
        String refreshToken
) {
}
