package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Venta;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VentaRepository extends JpaRepository<Venta, UUID> {

    @Query("""
            SELECT v
            FROM Venta v
            LEFT JOIN FETCH v.lineasVenta lv
            LEFT JOIN FETCH lv.producto p
            LEFT JOIN FETCH p.categoria
            WHERE v.caja.id = :idCaja
            AND v.finalizada = false
            """)
    List<Venta> findVentaNotFinalizadaByCajaId(Long idCaja);

    @Query("""
            SELECT v
            FROM Venta v
            LEFT JOIN FETCH v.lineasVenta lv
            LEFT JOIN FETCH lv.producto
            WHERE v.nombreCajero = :nombreCompleto
            AND v.caja.id = :idCaja
            AND v.finalizada = true
            """)
    Page<Venta> findVentaByCajeroNombreCompleto(String nombreCompleto, Long idCaja, Pageable pageable);

    @Query("""
            SELECT v
            FROM Venta v
            LEFT JOIN FETCH v.lineasVenta lv
            LEFT JOIN FETCH lv.producto
            WHERE v.caja.id = :cajaId
            AND v.fechaVenta <= :dia
            """)
    Page<Venta> findAllByCajaId(Long cajaId, LocalDateTime dia, Pageable pageable);

    @Override
    @Nonnull
    @Query("""
            SELECT v
            FROM Venta v
            LEFT JOIN FETCH v.lineasVenta lv
            LEFT JOIN FETCH lv.producto p
            LEFT JOIN FETCH p.categoria
            LEFT JOIN FETCH v.caja
            WHERE v.id = :id
            """)
    Optional<Venta> findById(@Nullable UUID id);

    int countByNombreCajero(String nombreCajero);
}
