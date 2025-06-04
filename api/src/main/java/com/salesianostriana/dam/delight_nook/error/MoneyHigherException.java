package com.salesianostriana.dam.delight_nook.error;

public class MoneyHigherException extends BadRequestException {
    public MoneyHigherException(String message) {
        super(message);
    }
}
