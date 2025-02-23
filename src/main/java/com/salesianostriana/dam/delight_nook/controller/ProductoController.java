package com.salesianostriana.dam.delight_nook.controller;

import com.salesianostriana.dam.delight_nook.dto.producto.CreateProductoDto;
import com.salesianostriana.dam.delight_nook.dto.producto.GetProductoDetailsDto;
import com.salesianostriana.dam.delight_nook.service.ProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
@Tag(name = "Producto",
description = "Controlador para gestionar productos")
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping("/admin/create")
    public ResponseEntity<GetProductoDetailsDto> create(@RequestBody @Validated CreateProductoDto productoDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        GetProductoDetailsDto.of(productoService.create(productoDto))
                );
    }
}
