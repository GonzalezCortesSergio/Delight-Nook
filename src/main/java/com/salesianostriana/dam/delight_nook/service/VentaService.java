package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.producto.ProductoCantidadDto;
import com.salesianostriana.dam.delight_nook.error.BadRequestException;
import com.salesianostriana.dam.delight_nook.error.CajaNotFoundException;
import com.salesianostriana.dam.delight_nook.error.ProductoNoEncontradoException;
import com.salesianostriana.dam.delight_nook.model.Caja;
import com.salesianostriana.dam.delight_nook.model.LineaVenta;
import com.salesianostriana.dam.delight_nook.model.Venta;
import com.salesianostriana.dam.delight_nook.repository.CajaRepository;
import com.salesianostriana.dam.delight_nook.repository.ProductoRepository;
import com.salesianostriana.dam.delight_nook.repository.StockRepository;
import com.salesianostriana.dam.delight_nook.repository.VentaRepository;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final CajaRepository cajaRepository;
    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Venta create(Cajero cajero, ProductoCantidadDto productoCantidadDto) {

        Caja caja = cajaRepository.findByCajeroSesion(cajero.getUsername())
                .stream()
                .findFirst()
                .orElseThrow(() -> new CajaNotFoundException("No has iniciado sesión en una caja para realizar esta operación"));

        Optional<Venta> optionalVenta = ventaRepository.findVentaNotFinalizadaByCajaId(caja.getId())
                .stream().findFirst();

        if(optionalVenta.isPresent()) {
            return addLineaVenta(optionalVenta.get(), productoCantidadDto);
        }

        Venta nueva = Venta.builder()
                .nombreCajero(cajero.getNombreCompleto())
                .build();

        nueva.addToCaja(caja);

        nueva.addLineaVenta(
                createLineaVenta(productoCantidadDto)
        );

        return ventaRepository.save(nueva);

    }

    private LineaVenta createLineaVenta(ProductoCantidadDto productoCantidadDto) {

        Optional<Integer> optionalCantidad = stockRepository.cantidadProductosStock(productoCantidadDto.idProducto());

        if(optionalCantidad.isEmpty())
            throw new ProductoNoEncontradoException("El producto no se encuentra en stock");
        if(optionalCantidad.get() < productoCantidadDto.cantidad())
            throw new BadRequestException("No puedes vender una cantidad mayor a la que se encuentra en stock");

        return LineaVenta.builder()
                .producto(
                        productoRepository.findById(productoCantidadDto.idProducto())
                                .orElseThrow(() -> new ProductoNoEncontradoException("No se ha encontrado el producto con ID: %d".formatted(productoCantidadDto.idProducto())))
                )
                .cantidad(productoCantidadDto.cantidad())
                .build();
    }

    private Optional<LineaVenta> lineaVentaExists(Venta venta, LineaVenta lv) {

        return venta.getLineasVenta().stream()
                .filter(lineaVenta -> Objects.equals(lineaVenta.getProducto().getId(), lv.getProducto().getId()))
                .findFirst();
    }

    private Venta addLineaVenta(Venta antigua, ProductoCantidadDto productoCantidadDto) {

        LineaVenta lineaVenta = createLineaVenta(productoCantidadDto);

        Optional<LineaVenta> optionalLineaVenta = lineaVentaExists(antigua, lineaVenta);

        if(optionalLineaVenta.isPresent()) {
            LineaVenta lineaVentaAntigua = optionalLineaVenta.get();
            lineaVentaAntigua.setCantidad(productoCantidadDto.cantidad());
        }

        else {
            antigua.addLineaVenta(lineaVenta);
        }

        return ventaRepository.save(antigua);
    }
}
