package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Producto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long>,
        JpaSpecificationExecutor<Producto> {

    @Query("""
            SELECT p
            FROM Producto p
            LEFT JOIN FETCH p.categoria
            WHERE p.id IN (
                SELECT p.id
                FROM Stock s
                LEFT JOIN s.producto p
            )
            """)
    Page<Producto> findAllProductoStock(Specification<Producto> specification, Pageable pageable);

    @Override
    @Nonnull
    @Query("""
            SELECT p
            FROM Producto p
            LEFT JOIN FETCH p.categoria
            """)
    Page<Producto> findAll(Specification<Producto> spec, @Nullable Pageable pageable);

    @Override
    @Nonnull
    @Query("""
            SELECT p
            FROM Producto p
            LEFT JOIN FETCH p.categoria
            WHERE p.id = :id
            """)
    Optional<Producto> findById(@Nullable Long id);
}
