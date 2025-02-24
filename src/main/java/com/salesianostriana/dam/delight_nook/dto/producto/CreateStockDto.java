package com.salesianostriana.dam.delight_nook.dto.producto;

import jakarta.validation.constraints.Min;

public record CreateStockDto(
        Long idProducto,
        @Min(1)
        int cantidad
) {
}
