package com.salesianostriana.dam.delight_nook.error;

import jakarta.persistence.EntityNotFoundException;

public class CajaNotFoundException extends EntityNotFoundException {
    public CajaNotFoundException(String message) {
        super(message);
    }
}
