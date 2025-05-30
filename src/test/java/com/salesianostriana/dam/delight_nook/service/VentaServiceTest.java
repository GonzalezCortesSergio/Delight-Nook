package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.producto.ProductoCantidadDto;
import com.salesianostriana.dam.delight_nook.model.Caja;
import com.salesianostriana.dam.delight_nook.model.Gestiona;
import com.salesianostriana.dam.delight_nook.model.Producto;
import com.salesianostriana.dam.delight_nook.model.Venta;
import com.salesianostriana.dam.delight_nook.repository.CajaRepository;
import com.salesianostriana.dam.delight_nook.repository.ProductoRepository;
import com.salesianostriana.dam.delight_nook.repository.StockRepository;
import com.salesianostriana.dam.delight_nook.repository.VentaRepository;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = VentaServiceTest.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private CajaRepository cajaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private VentaService ventaService;

    private Venta venta;

    private Caja caja;

    private Cajero cajero;

    private Gestiona gestiona;

    private Producto producto;

    private ProductoCantidadDto productoCantidadDto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Zapatos")
                .descripcion("Zapatikos")
                .precioUnidad(12.23)
                .proveedor("Zara")
                .build();
        cajero = Cajero.builder()
                .id(UUID.randomUUID())
                .nombreCompleto("Manolo Fernández")
                .username("manolitox1998")
                .build();
        caja = Caja.builder()
                .id(1L)
                .nombre("Caja 1")
                .dineroCaja(198.25)
                .deleted(false)
                .build();

        gestiona = new Gestiona();
        gestiona.setCajero(cajero);
        gestiona.setCaja(caja);

        venta = Venta.builder()
                .id(UUID.randomUUID())
                .fechaVenta(LocalDateTime.now())
                .nombreCajero(cajero.getNombreCompleto())
                .build();

        productoCantidadDto = new ProductoCantidadDto(1L, 2);
    }

    @Test
    void create() {
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        when(cajaRepository.findByCajeroSesion(cajero.getUsername())).thenReturn(Optional.of(caja));
        when(ventaRepository.findVentaNotFinalizadaByCajaId(caja.getId())).thenReturn(List.of(venta));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(stockRepository.cantidadProductosStock(1L)).thenReturn(Optional.ofNullable(2));

        Venta result = ventaService.create(cajero, productoCantidadDto);

        assertAll(
                () -> verify(cajaRepository, times(0)).save(any(Caja.class)),
                () -> verify(ventaRepository, times(1)).save(any(Venta.class)),
                () -> assertNotNull(result),
                () -> assertEquals("Manolo Fernández", result.getNombreCajero())
        );

    }
}