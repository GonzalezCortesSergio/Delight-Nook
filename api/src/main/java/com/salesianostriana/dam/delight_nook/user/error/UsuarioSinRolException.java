package com.salesianostriana.dam.delight_nook.user.error;

import com.salesianostriana.dam.delight_nook.error.BadRequestException;

public class UsuarioSinRolException extends BadRequestException {
    public UsuarioSinRolException(String message) {
        super(message);
    }
}
