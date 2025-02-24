package com.salesianostriana.dam.delight_nook.dto.producto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.model.Producto;

public record GetProductoDetailsDto(
        String nombre,
        double precioUnidad,
        String descripcion,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String imagen,
        String categoria,
        String proveedor
) {

    public static GetProductoDetailsDto of (Producto producto, String imageUrl) {

        return new GetProductoDetailsDto(
                producto.getNombre(),
                producto.getPrecioUnidad(),
                producto.getDescripcion(),
                imageUrl,
                producto.getCategoria().getNombre(),
                producto.getProveedor()
        );
    }
}
