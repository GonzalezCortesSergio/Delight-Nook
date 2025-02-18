package com.salesianostriana.dam.delight_nook.user.repository;

import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByUsername(String username);

    Optional<Usuario> findByActivationToken(String token);
}
