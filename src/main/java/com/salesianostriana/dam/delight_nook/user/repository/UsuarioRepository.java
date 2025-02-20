package com.salesianostriana.dam.delight_nook.user.repository;

import com.salesianostriana.dam.delight_nook.user.dto.UsuarioResponseDto;
import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByUsername(String username);

    Optional<Usuario> findByActivationToken(String token);

    @Query("""
            SELECT u
            FROM Usuario u
            INNER JOIN u.roles r
            WHERE r = :userRole
            """)
    Page<Usuario> findByUserRole(UserRole userRole, Pageable pageable);

    boolean existsByUsername(String username);

    @Modifying
    @Query("""
            UPDATE Usuario u
            set u.deleted = true
            where upper(u.username) = upper(:username)
            """)
    @Transactional
    void deleteByUsername(String username);
}
