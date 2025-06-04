package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductoRepository extends JpaRepository<Producto, Long>,
        JpaSpecificationExecutor<Producto> {

    @Query("""
            SELECT p
            FROM Producto p
            WHERE EXISTS (
                SELECT s.producto
                FROM Stock s
            )
            """)
    Page<Producto> findAllProductoStock(Specification<Producto> specification, Pageable pageable);
}
