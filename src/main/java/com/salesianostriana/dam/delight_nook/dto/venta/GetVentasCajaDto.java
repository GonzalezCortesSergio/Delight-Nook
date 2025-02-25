package com.salesianostriana.dam.delight_nook.dto.venta;

import com.salesianostriana.dam.delight_nook.model.Venta;
import org.springframework.data.domain.Page;

public record GetVentasCajaDto(
        Page<GetVentaDto> result,
        double totalRecaudado
) {

    public static GetVentasCajaDto of (Page<Venta> ventas) {

        return new GetVentasCajaDto(
                ventas.map(GetVentaDto::of),
                ventas.stream()
                        .mapToDouble(Venta::getPrecioFinal)
                        .sum()
        );
    }
}
