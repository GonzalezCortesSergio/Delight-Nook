package com.salesianostriana.dam.delight_nook.security.jwt.refresh;

import com.salesianostriana.dam.delight_nook.security.exceptionhandling.JwtException;

public class RefreshTokenException extends JwtException {
    public RefreshTokenException(String message) {
        super(message);
    }
}
