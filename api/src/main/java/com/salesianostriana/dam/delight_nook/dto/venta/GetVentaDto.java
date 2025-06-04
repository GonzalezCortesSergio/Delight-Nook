package com.salesianostriana.dam.delight_nook.dto.venta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.model.Venta;

import java.time.LocalDateTime;

public record GetVentaDto(
        String id,
        double precioFinal,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime fechaVenta
) {

    public static GetVentaDto of (Venta venta) {

        return new GetVentaDto(
                venta.getId().toString(),
                venta.getPrecioFinal(),
                venta.getFechaVenta()
        );
    }
}
