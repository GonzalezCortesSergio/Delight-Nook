package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.producto.ProductoCantidadDto;
import com.salesianostriana.dam.delight_nook.error.ProductoNoEncontradoException;
import com.salesianostriana.dam.delight_nook.model.Stock;
import com.salesianostriana.dam.delight_nook.repository.ProductoRepository;
import com.salesianostriana.dam.delight_nook.repository.StockRepository;
import com.salesianostriana.dam.delight_nook.user.model.Almacenero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository stockRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public Stock create(Almacenero almacenero, ProductoCantidadDto stockDto) {
        Optional<Stock> optionalStock = stockRepository.findByProductoId(stockDto.idProducto());

        if(optionalStock.isPresent()) {

            Stock antiguo = optionalStock.get();
            antiguo.setCantidad(antiguo.getCantidad() + stockDto.cantidad());
            antiguo.setAlmacenero(almacenero);

            return stockRepository.save(antiguo);
        }
        Stock nuevo = new Stock();

        nuevo.setAlmacenero(almacenero);
        nuevo.setProducto(
                productoRepository.findById(stockDto.idProducto())
                        .orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con ID: %d".formatted(stockDto.idProducto())))
        );
        nuevo.setCantidad(stockDto.cantidad());

        return stockRepository.save(nuevo);
    }

    @Transactional
    public void deleteById(Long idProducto) {
        stockRepository.findByProductoId(idProducto).ifPresent(stockRepository::delete);
    }
}
