package com.salesianostriana.dam.delight_nook.dto;

import com.salesianostriana.dam.delight_nook.security.validation.annotation.MoneyNotHigher;

@MoneyNotHigher
public record EditCajaDto(
        Long id,
        double dineroNuevo
) {
}
