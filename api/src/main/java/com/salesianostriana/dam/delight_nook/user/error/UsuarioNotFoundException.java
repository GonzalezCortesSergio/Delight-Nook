package com.salesianostriana.dam.delight_nook.user.error;

import jakarta.persistence.EntityNotFoundException;

public class UsuarioNotFoundException extends EntityNotFoundException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
