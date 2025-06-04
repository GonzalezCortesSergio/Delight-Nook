package com.salesianostriana.dam.delight_nook.dto.producto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.model.Producto;

public record GetProductoDto(
        Long id,
        String nombre,
        String categoria,
        double precioUnidad,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String imagen
) {

    public static GetProductoDto of (Producto producto, String imagenUrl) {

        return new GetProductoDto(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getNombre(),
                producto.getPrecioUnidad(),
                imagenUrl
        );
    }
}
