package com.salesianostriana.dam.delight_nook.dto.venta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.dto.caja.GetCajaDto;
import com.salesianostriana.dam.delight_nook.model.Venta;

import java.time.LocalDateTime;
import java.util.List;

public record GetVentaDetailsDto(
        String id,
        String nombreCajero,
        GetCajaDto caja,
        List<GetLineaVentaDto> lineasVenta,
        double precioFinal,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime fechaVenta
) {

    public static GetVentaDetailsDto of (Venta venta) {

        return new GetVentaDetailsDto(
                venta.getId().toString(),
                venta.getNombreCajero(),
                GetCajaDto.of(venta.getCaja()),
                venta.getLineasVenta().stream()
                        .map(GetLineaVentaDto::of)
                        .toList(),
                venta.getPrecioFinal(),
                venta.getFechaVenta()
        );
    }
}
