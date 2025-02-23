package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre);
}
