package com.salesianostriana.dam.delight_nook.dto.categoria;

import com.salesianostriana.dam.delight_nook.model.Categoria;

import java.util.List;

public record GetCategoriaDetailsDto(
        GetCategoriaDto categoria,
        List<GetCategoriaDto> categoriasHijas
) {

    public static GetCategoriaDetailsDto of (Categoria categoria) {

        return new GetCategoriaDetailsDto(
                GetCategoriaDto.of(categoria),
                categoria.getCategoriasHijas().stream()
                        .map(GetCategoriaDto::of)
                        .toList()
        );
    }
}
