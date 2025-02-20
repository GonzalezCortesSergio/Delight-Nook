package com.salesianostriana.dam.delight_nook.dto;

import com.salesianostriana.dam.delight_nook.model.Caja;

public record GetCajaDto(
        Long id,
        String nombre,
        double dineroCaja
) {

    public static GetCajaDto of (Caja caja) {

        return new GetCajaDto(
                caja.getId(),
                caja.getNombre(),
                caja.getDineroCaja()
        );
    }
}
