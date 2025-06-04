package com.salesianostriana.dam.delight_nook.dto.categoria;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.dam.delight_nook.model.Categoria;

public record GetCategoriaDto(
        Long id,
        String nombre,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String categoriaPadre
) {

    public static GetCategoriaDto of (Categoria categoria) {

        if(categoria.getCategoriaPadre() == null) {
            return new GetCategoriaDto(
                    categoria.getId(),
                    categoria.getNombre(),
                    null
            );
        }

        return new GetCategoriaDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getCategoriaPadre().getNombre()
        );
    }
}
