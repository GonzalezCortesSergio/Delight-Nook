package com.salesianostriana.dam.delight_nook.dto.producto;

import com.salesianostriana.dam.delight_nook.model.Stock;
import com.salesianostriana.dam.delight_nook.user.dto.UsuarioResponseDto;

public record GetStockDto(
        GetProductoDto producto,
        UsuarioResponseDto almacenero,
        int cantidad
) {

    public static GetStockDto of (Stock stock, String imageUrl, String avatarUrl) {

        return new GetStockDto(
                GetProductoDto.of(stock.getProducto(), imageUrl),
                UsuarioResponseDto.of(stock.getAlmacenero(), avatarUrl),
                stock.getCantidad()
        );
    }
}
