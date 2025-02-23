package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.categoria.CreateCategoriaHijaDto;
import com.salesianostriana.dam.delight_nook.error.CategoriaNotFoundException;
import com.salesianostriana.dam.delight_nook.model.Categoria;
import com.salesianostriana.dam.delight_nook.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;


    @Transactional
    public Categoria create(String nombre) {

        return categoriaRepository.save(
                Categoria.builder()
                        .nombre(nombre)
                        .build()
        );
    }

    @Transactional
    public Categoria createCategoriaHija(CreateCategoriaHijaDto categoriaHijaDto) {

        return categoriaRepository.findById(categoriaHijaDto.categoriaMadreId())
                .map(categoria -> {

                    Categoria categoriaHija = Categoria.builder()
                            .nombre(categoriaHijaDto.categoriaNombre())
                            .build();

                    categoriaHija.addToCategoriaPadre(categoria);

                    return categoriaRepository.save(categoriaHija);
                })
                .orElseThrow(() -> new CategoriaNotFoundException("No se ha encontrado la categoría con ID: %d".formatted(categoriaHijaDto.categoriaMadreId())));
    }

    @Transactional
    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }
}
