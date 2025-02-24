package com.salesianostriana.dam.delight_nook.dto.producto;

import com.salesianostriana.dam.delight_nook.model.Producto;

public record GetProductoDto(
        Long id,
        String nombre,
        String categoria,
        double precioUnidad,
        String imagen
) {

    public static GetProductoDto of (Producto producto) {

        return new GetProductoDto(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getNombre(),
                producto.getPrecioUnidad(),
                producto.getImagen()
        );
    }
}
