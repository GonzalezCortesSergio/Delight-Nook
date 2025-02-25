package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CajaRepository extends JpaRepository<Caja, Long> {

    @Query("""
            SELECT c.dineroCaja
            FROM Caja c
            WHERE c.id = :id
            """)
    Optional<Double> findDineroCaja(Long id);

    @Query("""
            SELECT c
            FROM Caja c
            WHERE EXISTS (
                SELECT g.caja
                FROM Gestiona g
                WHERE g.cajero.username = :username
                AND g.fechaDejaGestionar IS NULL
            )
            """)
    List<Caja> findByCajeroSesion(String username);
}
