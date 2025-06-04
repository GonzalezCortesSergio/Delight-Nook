package com.salesianostriana.dam.delight_nook.dto.venta;

import com.salesianostriana.dam.delight_nook.dto.producto.GetProductoDto;
import com.salesianostriana.dam.delight_nook.model.LineaVenta;

public record GetLineaVentaDto(
        String id,
        GetProductoDto producto,
        int cantidad,
        double subTotal
) {

    public static GetLineaVentaDto of (LineaVenta lineaVenta) {

        return new GetLineaVentaDto(
                lineaVenta.getId().toString(),
                GetProductoDto.of(lineaVenta.getProducto(), null),
                lineaVenta.getCantidad(),
                lineaVenta.getSubTotal()
        );
    }
}
