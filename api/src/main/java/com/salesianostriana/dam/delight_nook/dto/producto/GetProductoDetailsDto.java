package com.salesianostriana.dam.delight_nook.dto.producto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.dto.categoria.GetCategoriaDto;
import com.salesianostriana.dam.delight_nook.model.Producto;

public record GetProductoDetailsDto(
        String nombre,
        double precioUnidad,
        String descripcion,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String imagen,
        GetCategoriaDto categoria,
        String proveedor
) {

    public static GetProductoDetailsDto of (Producto producto, String imageUrl) {

        return new GetProductoDetailsDto(
                producto.getNombre(),
                producto.getPrecioUnidad(),
                producto.getDescripcion(),
                imageUrl,
                GetCategoriaDto.of(producto.getCategoria()),
                producto.getProveedor()
        );
    }
}
