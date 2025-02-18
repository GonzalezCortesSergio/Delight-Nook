package com.salesianostriana.dam.delight_nook.util;

import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeed {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        usuarioRepository.save(
                Usuario.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .nombreCompleto("admin")
                        .avatar("admin.png")
                        .email("admin@admin.com")
                        .roles(Set.of(UserRole.ADMIN))
                        .enabled(true)
                        .build()
        );
    }
}
