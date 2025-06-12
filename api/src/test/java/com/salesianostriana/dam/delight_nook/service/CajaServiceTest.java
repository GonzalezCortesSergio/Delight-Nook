package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.error.CajaNotFoundException;
import com.salesianostriana.dam.delight_nook.model.Caja;
import com.salesianostriana.dam.delight_nook.repository.CajaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CajaServiceTest {

    private AutoCloseable closeable;

    @Mock
    private CajaRepository cajaRepository;

    @InjectMocks
    private CajaService cajaService;

    private Caja caja;

    @BeforeEach
    void setUp() {

        closeable = MockitoAnnotations.openMocks(this);

        caja = Caja.builder()
                .id(1L)
                .nombre("Caja")
                .dineroCaja(200.23)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception{
        closeable.close();
    }

    @Test
    void testFindById() {
        when(cajaRepository.findById(anyLong())).thenReturn(Optional.of(caja));

        Caja result = cajaService.findById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(caja.getId(), result.getId()),
                () -> assertEquals(caja.getNombre(), result.getNombre()),
                () -> assertEquals(caja.getDineroCaja(), result.getDineroCaja()),
                () -> verify(cajaRepository, times(1)).findById(anyLong())
        );
    }

    @Test
    void testFindByIdNotFound() {
        when(cajaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrowsExactly(CajaNotFoundException.class, () -> cajaService.findById(1L)),
                () -> verify(cajaRepository, times(1)).findById(anyLong())
        );
    }
}