package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VentaRepository extends JpaRepository<Venta, UUID> {

    @Query("""
            SELECT v
            FROM Venta v
            WHERE v.caja.id = :idCaja
            AND v.finalizada = false
            """)
    List<Venta> findVentaNotFinalizadaByCajaId(Long idCaja);

    @Query("""
            SELECT v
            FROM Venta v
            WHERE v.nombreCajero = :nombreCompleto
            AND v.caja.id = :idCaja
            """)
    Page<Venta> findVentaByCajeroNombreCompleto(String nombreCompleto, Long idCaja, Pageable pageable);
}
