package com.salesianostriana.dam.delight_nook.dto.categoria;

import com.salesianostriana.dam.delight_nook.security.validation.annotation.UniqueCategoryName;

public record CreateCategoriaHijaDto(
        Long categoriaMadreId,
        @UniqueCategoryName
        String categoriaNombre
) {
}
