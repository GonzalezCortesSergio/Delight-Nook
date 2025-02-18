package com.salesianostriana.dam.delight_nook.user.service;

import com.salesianostriana.dam.delight_nook.user.dto.CreateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.dto.ValidateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.error.ActivationExpiredException;
import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.verification.duration}")
    private int activationDuration;

    public Usuario createUsuario(CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(
                Usuario.builder()
                        .username(usuarioDto.username())
                        .email(usuarioDto.email())
                        .nombreCompleto(usuarioDto.nombreCompleto())
                        .avatar(usuarioDto.avatar())
                        .roles(Set.of(UserRole.ADMIN))
                        .activationToken(generateRandomActivationCode())
                        .build()
        );
    }

    private String generateRandomActivationCode() {

        return UUID.randomUUID().toString();
    }

    public Usuario acitvateAccount(ValidateUsuarioDto validateUsuarioDto) {

        return usuarioRepository.findByActivationToken(validateUsuarioDto.activationToken())
                .filter(usuario -> ChronoUnit.MINUTES.between(Instant.now(), usuario.getCreatedAt()) - activationDuration < 0)
                .map(usuario -> {
                    usuario.setPassword(passwordEncoder.encode(validateUsuarioDto.password()));
                    usuario.setEnabled(true);
                    usuario.setActivationToken(null);

                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new ActivationExpiredException("El código de activación no existe o ha caducado"));
    }
}
