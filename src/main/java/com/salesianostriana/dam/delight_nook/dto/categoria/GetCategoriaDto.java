package com.salesianostriana.dam.delight_nook.dto.categoria;

import com.salesianostriana.dam.delight_nook.model.Categoria;

public record GetCategoriaDto(
        Long id,
        String nombre
) {

    public static GetCategoriaDto of (Categoria categoria) {

        return new GetCategoriaDto(
                categoria.getId(),
                categoria.getNombre()
        );
    }
}
