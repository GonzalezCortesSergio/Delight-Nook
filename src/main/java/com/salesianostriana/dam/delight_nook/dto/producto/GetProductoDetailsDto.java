package com.salesianostriana.dam.delight_nook.dto.producto;

import com.salesianostriana.dam.delight_nook.model.Producto;

public record GetProductoDetailsDto(
        String nombre,
        double precioUnidad,
        String descripcion,
        String imagen,
        String categoria,
        String proveedor
) {

    public static GetProductoDetailsDto of (Producto producto) {

        return new GetProductoDetailsDto(
                producto.getNombre(),
                producto.getPrecioUnidad(),
                producto.getDescripcion(),
                producto.getImagen(),
                producto.getCategoria().getNombre(),
                producto.getProveedor()
        );
    }
}
