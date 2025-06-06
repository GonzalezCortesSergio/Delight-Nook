package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Gestiona;
import com.salesianostriana.dam.delight_nook.model.GestionaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GestionaRepository extends JpaRepository<Gestiona, GestionaPK> {

    @Query("""
            SELECT g
            FROM Gestiona g
            LEFT JOIN g.cajero c
            WHERE c.username = :username
            AND g.caja.id = :cajaId
            """)
    Optional<Gestiona> findByCajeroUsernameAndCajaId(String username, Long cajaId);
}
