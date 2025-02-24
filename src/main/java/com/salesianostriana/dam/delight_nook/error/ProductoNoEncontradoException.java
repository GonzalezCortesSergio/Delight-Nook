package com.salesianostriana.dam.delight_nook.error;

import jakarta.persistence.EntityNotFoundException;

public class ProductoNoEncontradoException extends EntityNotFoundException {
    public ProductoNoEncontradoException(String message) {
        super(message);
    }
}
