package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja, Long> {

    @Query("""
            SELECT c.dineroCaja
            FROM Caja c
            WHERE c.id = :id
            """)
    Optional<Double> findDineroCaja(Long id);
}
