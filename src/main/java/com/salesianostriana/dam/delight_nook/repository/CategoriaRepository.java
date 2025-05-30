package com.salesianostriana.dam.delight_nook.repository;

import com.salesianostriana.dam.delight_nook.model.Categoria;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre);

    @Query("""
            SELECT c
            FROM Categoria c
            WHERE c.categoriaPadre IS NULL
            """)
    Page<Categoria> findAllCategoriaBase(Pageable pageable);

    @Override
    @Nonnull
    @Query("""
            SELECT c
            FROM Categoria c
            LEFT JOIN FETCH c.categoriasHijas
            WHERE c.id = :aLong
            """)
    Optional<Categoria> findById(@Nullable Long aLong);
}
