package com.salesianostriana.dam.delight_nook.security.exceptionhandling;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
