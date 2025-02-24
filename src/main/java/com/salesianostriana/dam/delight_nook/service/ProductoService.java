package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.producto.CreateProductoDto;
import com.salesianostriana.dam.delight_nook.dto.producto.GetProductoDto;
import com.salesianostriana.dam.delight_nook.error.CategoriaNotFoundException;
import com.salesianostriana.dam.delight_nook.error.ProductoNoEncontradoException;
import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.query.ProductoSpecificationBuilder;
import com.salesianostriana.dam.delight_nook.repository.CategoriaRepository;
import com.salesianostriana.dam.delight_nook.repository.ProductoRepository;
import com.salesianostriana.dam.delight_nook.util.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Page<GetProductoDto> search(List<SearchCriteria> searchCriteriaList, Pageable pageable) {

        ProductoSpecificationBuilder productoSpecificationBuilder
                = new ProductoSpecificationBuilder(searchCriteriaList);

        Specification<Producto> where = productoSpecificationBuilder.build();

        Page<Producto> result = productoRepository.findAll(where, pageable);

        if(result.isEmpty())
            throw new ProductoNoEncontradoException("No se han encontrado productos");

        return result.map(GetProductoDto::of);

    }


}
