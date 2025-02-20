package com.salesianostriana.dam.delight_nook.user.error;


import com.salesianostriana.dam.delight_nook.error.BadRequestException;

public class BorradoPropioException extends BadRequestException {
    public BorradoPropioException(String message) {
        super(message);
    }
}
