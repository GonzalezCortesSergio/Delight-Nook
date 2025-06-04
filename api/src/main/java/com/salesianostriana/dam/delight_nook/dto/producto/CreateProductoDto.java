package com.salesianostriana.dam.delight_nook.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record CreateProductoDto(
        @NotBlank
        String nombre,
        @DecimalMin("0.01")
        double precioUnidad,
        @NotBlank
        String descripcion,
        Long categoriaId,
        @NotBlank
        String proveedor
) {
}
