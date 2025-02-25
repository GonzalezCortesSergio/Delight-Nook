package com.salesianostriana.dam.delight_nook.controller;

import com.salesianostriana.dam.delight_nook.dto.producto.ProductoCantidadDto;
import com.salesianostriana.dam.delight_nook.dto.venta.GetVentaDetailsDto;
import com.salesianostriana.dam.delight_nook.model.Venta;
import com.salesianostriana.dam.delight_nook.service.VentaService;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/venta")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping("/addProducto")
    public ResponseEntity<GetVentaDetailsDto> createVenta(@AuthenticationPrincipal Cajero cajero,
                                                          @RequestBody ProductoCantidadDto productoCantidadDto) {

        Venta venta = ventaService.create(cajero, productoCantidadDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        GetVentaDetailsDto.of(venta)
                );
    }
}
