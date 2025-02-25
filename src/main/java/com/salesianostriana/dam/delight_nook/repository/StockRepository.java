package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("""
            SELECT s
            FROM Stock s
            JOIN fetch s.producto p
            WHERE p.id = :productoId
            """)
    Optional<Stock> findByProductoId(Long productoId);

    @Query("""
            SELECT s.cantidad
            FROM Stock s
            WHERE s.producto.id = :productoId
            """)
    Optional<Integer> cantidadProductosStock(Long productoId);
}
