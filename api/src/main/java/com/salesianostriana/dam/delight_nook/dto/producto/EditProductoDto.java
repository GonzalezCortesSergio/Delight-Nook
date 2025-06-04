package com.salesianostriana.dam.delight_nook.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record EditProductoDto(
        @DecimalMin("0.01")
        double precioUnidad,
        @NotBlank
        String descripcion,
        Long idCategoria,
        @NotBlank
        String proveedor
) {
}
