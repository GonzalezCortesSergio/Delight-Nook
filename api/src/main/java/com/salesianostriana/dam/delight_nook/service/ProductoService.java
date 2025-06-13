package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.producto.CreateProductoDto;
import com.salesianostriana.dam.delight_nook.dto.producto.EditProductoDto;
import com.salesianostriana.dam.delight_nook.dto.producto.GetProductoDto;
import com.salesianostriana.dam.delight_nook.error.CategoriaNotFoundException;
import com.salesianostriana.dam.delight_nook.error.ProductoNoEncontradoException;
import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.model.Stock;
import com.salesianostriana.dam.delight_nook.query.ProductoFilterDTO;
import com.salesianostriana.dam.delight_nook.repository.CategoriaRepository;
import com.salesianostriana.dam.delight_nook.repository.ProductoRepository;
import com.salesianostriana.dam.delight_nook.util.files.model.FileMetadata;
import com.salesianostriana.dam.delight_nook.util.files.service.StorageService;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final StorageService storageService;
    private final StockService stockService;

    private static final String NOT_FOUND_MESSAGE = "No se han encontrado productos";

    @Transactional
    public Producto create(CreateProductoDto productoDto) {

        return productoRepository.save(
                Producto.builder()
                        .nombre(productoDto.nombre())
                        .precioUnidad(productoDto.precioUnidad())
                        .descripcion(productoDto.descripcion())
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

    public Page<GetProductoDto> search(ProductoFilterDTO filterDTO, Pageable pageable) {

        Page<Producto> productos = productoRepository.findAll(filterDTO.obtainFilterSpecification(), pageable);

        if(productos.isEmpty())
            throw new ProductoNoEncontradoException(NOT_FOUND_MESSAGE);

        return productos.map(producto -> GetProductoDto.of(producto, getImageUrl(producto.getImagen())));
    }

    public Page<GetProductoDto> searchStock(ProductoFilterDTO filterDTO, Pageable pageable) {

        Specification<Producto> specification = ((root, query, cb) -> {

            Subquery<Long> stockSubquery = query.subquery(Long.class);
            Root<Stock> stockRoot = stockSubquery.from(Stock.class);

            stockSubquery.select(stockRoot.get("producto").get("id"));
            return cb.in(root.get("id")).value(stockSubquery);
        });

        specification = specification.and(filterDTO.obtainFilterSpecification());

        Page<Producto> productos = productoRepository.findAll(specification, pageable);

        if(productos.isEmpty())
            throw new ProductoNoEncontradoException(NOT_FOUND_MESSAGE);

        return productos.map(producto -> GetProductoDto.of(producto, getImageUrl(producto.getImagen())));
    }

    @Transactional
    public Producto edit(EditProductoDto productoDto, Long id) {

        return productoRepository.findById(id)
                .map(producto -> {

                    producto.setPrecioUnidad(productoDto.precioUnidad());
                    producto.setDescripcion(productoDto.descripcion());
                    producto.setProveedor(productoDto.proveedor());
                    producto.setCategoria(categoriaRepository.findById(
                            productoDto.idCategoria()
                    )
                            .orElseThrow(() -> new CategoriaNotFoundException("No se ha encontrado la categoría con ID: %d".formatted(productoDto.idCategoria()))));

                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado un producto con ID: %d".formatted(id)));
    }

    @Transactional
    public Producto changeImage(MultipartFile file, Long id) {

        return productoRepository.findById(id)
                .map(producto -> {
                    FileMetadata fileMetadata = storageService.store(file);

                    producto.setImagen(fileMetadata.getId());

                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con ID: %d".formatted(id)));
    }

    public String getImageUrl(String filename) {

        if(filename == null || filename.isEmpty())
            return null;

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/producto/download/")
                .path(filename)
                .toUriString();
    }

    public Producto findById(Long id) {

        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con ID: %d".formatted(id)));
    }

    @Transactional
    public void deleteById(Long id) {

        stockService.deleteById(id);

        productoRepository.deleteById(id);
    }


}
