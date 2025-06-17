package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.producto.ProductoCantidadDto;
import com.salesianostriana.dam.delight_nook.dto.venta.GetVentaDto;
import com.salesianostriana.dam.delight_nook.dto.venta.GetVentasCajaDto;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final CajaRepository cajaRepository;
    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;

    private static final String CAJA_NO_OCUPADA_POR_USUARIO = "No has iniciado sesión en una caja para realizar esta operación";
    private static final String VENTA_NOT_FOUND = "No se ha encontrado la venta";

    @Transactional
    public Venta create(Cajero cajero, ProductoCantidadDto productoCantidadDto) {

        Caja caja = cajaRepository.findByCajeroSesion(cajero.getUsername())
                .orElseThrow(() -> new CajaNotFoundException(CAJA_NO_OCUPADA_POR_USUARIO));

        Optional<Venta> optionalVenta = ventaRepository.findVentaNotFinalizadaByCajaId(caja.getId())
                .stream().findFirst();

        if(optionalVenta.isPresent()) {
            return addLineaVenta(optionalVenta.get(), productoCantidadDto);
        }

        Venta nueva = Venta.builder()
                .nombreCajero(cajero.getNombreCompleto())
                .caja(caja)
                .build();

        nueva.addLineaVenta(
                createLineaVenta(productoCantidadDto)
        );

        return ventaRepository.save(nueva);

    }

    @Transactional
    public Venta removeLineaVenta(Cajero cajero, UUID idLineaVenta) {

        Caja caja = cajaRepository.findByCajeroSesion(cajero.getUsername())
                .stream()
                .findFirst()
                .orElseThrow(() -> new CajaNotFoundException(CAJA_NO_OCUPADA_POR_USUARIO));

        Venta venta = ventaRepository.findVentaNotFinalizadaByCajaId(caja.getId())
                .stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException(VENTA_NOT_FOUND));

        venta.getLineasVenta().stream()
                .filter(lineaVenta -> lineaVenta.getId().equals(idLineaVenta))
                .findFirst()
                .ifPresent(lineaVenta -> {
                    lineaVenta.setProducto(null);
                    venta.getLineasVenta().remove(lineaVenta);
                });

        if(venta.getLineasVenta().isEmpty()) {
            ventaRepository.delete(venta);
            return null;
        }
        return ventaRepository.save(venta);
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

    @Transactional
    public Venta finalizarVenta(Cajero cajero) {

        Caja caja = cajaRepository.findByCajeroSesion(cajero.getUsername())
                .stream()
                .findFirst()
                .orElseThrow(() -> new CajaNotFoundException(CAJA_NO_OCUPADA_POR_USUARIO));

        Venta venta = ventaRepository.findVentaNotFinalizadaByCajaId(caja.getId())
                .stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException(VENTA_NOT_FOUND));

        venta.setFechaVenta(LocalDateTime.now());
        venta.setFinalizada(true);

        venta.getLineasVenta().forEach(lineaVenta ->
            stockRepository.findByProductoId(lineaVenta.getProducto().getId())
                    .ifPresent(stock -> {
                        stock.setCantidad(stock.getCantidad() - lineaVenta.getCantidad());
                        stockRepository.save(stock);
                    })
        );
        caja.setDineroCaja(caja.getDineroCaja() + venta.getPrecioFinal());

        return ventaRepository.save(venta);
    }

    public GetVentasCajaDto findAllByCajeroAndCaja(Cajero cajero, Pageable pageable) {

        Caja caja = cajaRepository.findByCajeroSesion(cajero.getUsername())
                .stream()
                .findFirst()
                .orElseThrow(() -> new CajaNotFoundException(CAJA_NO_OCUPADA_POR_USUARIO));

        Page<Venta> result = ventaRepository.findVentaByCajeroNombreCompleto(cajero.getNombreCompleto(), caja.getId(), pageable);

        if(result.isEmpty())
            throw new EntityNotFoundException("No se han encontrado ventas");

        return GetVentasCajaDto.of(result);
    }

    public Venta findById(UUID idVenta) {

        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new EntityNotFoundException(VENTA_NOT_FOUND));
    }

    public Page<Venta> findAllVentasCaja(Long idCaja, Pageable pageable) {

        Page<Venta> result = ventaRepository.findAllByCajaId(idCaja, LocalDateTime.now().plusDays(1L), pageable);

        if(result.isEmpty())
            throw new EntityNotFoundException("Ventas no encontradas");

        return result;
    }

    public int countByNombreCajero(Cajero cajero) {
        return ventaRepository.countByNombreCajero(cajero.getNombreCompleto());
    }
}
