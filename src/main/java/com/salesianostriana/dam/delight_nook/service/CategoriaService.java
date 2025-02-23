package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.model.Categoria;
import com.salesianostriana.dam.delight_nook.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;


    public Categoria create(String nombre) {

        return categoriaRepository.save(
                Categoria.builder()
                        .nombre(nombre)
                        .build()
        );
    }
}
