package com.salesianostriana.dam.delight_nook.dto.caja;

import com.salesianostriana.dam.delight_nook.security.validation.annotation.MoneyNotHigher;

@MoneyNotHigher
public record EditCajaDto(
        Long id,
        double dineroNuevo
) {
}
