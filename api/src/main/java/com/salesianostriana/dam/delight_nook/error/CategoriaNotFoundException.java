package com.salesianostriana.dam.delight_nook.error;

import jakarta.persistence.EntityNotFoundException;

public class CategoriaNotFoundException extends EntityNotFoundException {
    public CategoriaNotFoundException(String message) {
        super(message);
    }
}
