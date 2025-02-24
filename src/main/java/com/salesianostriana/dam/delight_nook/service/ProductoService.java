package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.producto.CreateProductoDto;
import com.salesianostriana.dam.delight_nook.error.CategoriaNotFoundException;
import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.repository.CategoriaRepository;
import com.salesianostriana.dam.delight_nook.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;


    public Producto create(CreateProductoDto productoDto) {

        return productoRepository.save(
                Producto.builder()
                        .nombre(productoDto.nombre())
                        .precioUnidad(productoDto.precioUnidad())
                        .descripcion(productoDto.descripcion())
                        .imagen("")
                        .categoria(
                                categoriaRepository.findById(productoDto.categoriaId())
                                        .orElseThrow(
                                                () -> new CategoriaNotFoundException("No se ha encontrado la categoría con ID: %d"
                                                        .formatted(productoDto.categoriaId())))
                        )
                        .proveedor(productoDto.proveedor())
                        .build()
        );
    }


}
