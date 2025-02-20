package com.salesianostriana.dam.delight_nook.user.service;

import com.salesianostriana.dam.delight_nook.user.dto.CreateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.dto.UsuarioResponseDto;
import com.salesianostriana.dam.delight_nook.user.dto.ValidateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.error.ActivationExpiredException;
import com.salesianostriana.dam.delight_nook.user.error.UsuarioNotFoundException;
import com.salesianostriana.dam.delight_nook.user.error.UsuarioSinRolException;
import com.salesianostriana.dam.delight_nook.user.model.Almacenero;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.verification.duration}")
    private int activationDuration;

    private Usuario createUsuario(CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(Usuario.builder()
                .username(usuarioDto.username())
                .email(usuarioDto.email())
                .nombreCompleto(usuarioDto.nombreCompleto())
                .avatar(usuarioDto.avatar())
                .roles(Set.of(UserRole.ADMIN))
                .activationToken(generateRandomActivationCode())
                .build());
    }

    public Usuario saveUsuario(CreateUsuarioDto usuarioDto, String userRole) {

        if(userRole.equalsIgnoreCase("almacenero"))
            return createAlmacenero(usuarioDto);

        if(userRole.equalsIgnoreCase("cajero"))

            return createCajero(usuarioDto);

        return createUsuario(usuarioDto);

    }

    private Almacenero createAlmacenero(CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(
                Almacenero.builder()
                        .username(usuarioDto.username())
                        .email(usuarioDto.email())
                        .nombreCompleto(usuarioDto.nombreCompleto())
                        .avatar(usuarioDto.avatar())
                        .roles(Set.of(UserRole.ALMACENERO))
                        .activationToken(generateRandomActivationCode())
                        .build()
        );
    }

    private Cajero createCajero(CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(
                Cajero.builder()
                        .username(usuarioDto.username())
                        .email(usuarioDto.email())
                        .nombreCompleto(usuarioDto.nombreCompleto())
                        .avatar(usuarioDto.avatar())
                        .roles(Set.of(UserRole.CAJERO))
                        .activationToken(generateRandomActivationCode())
                        .build()
        );
    }

    private String generateRandomActivationCode() {

        return UUID.randomUUID().toString();
    }

    public Usuario activateAccount(ValidateUsuarioDto validateUsuarioDto) {

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

    public Page<UsuarioResponseDto> findAllByUserRole(Pageable pageable, String userRole) {

        Page<Usuario> result;

        if(userRole.equalsIgnoreCase("admin"))
            result = usuarioRepository.findByUserRole(UserRole.ADMIN, pageable);

        else if (userRole.equalsIgnoreCase("almacenero"))
            result = usuarioRepository.findByUserRole(UserRole.ALMACENERO, pageable);

        else if (userRole.equalsIgnoreCase("cajero"))
            result = usuarioRepository.findByUserRole(UserRole.CAJERO, pageable);

        else {
            result = usuarioRepository.findAll(pageable);
        }

        if(result.isEmpty())
            throw new UsuarioNotFoundException("No se ha encontrado ningún usuario");

        return result.map(UsuarioResponseDto::of);
    }

    public Usuario addRoleAdmin(String username) {

        return usuarioRepository.findFirstByUsername(username)
                .map(usuario -> {
                    usuario.getRoles().add(UserRole.ADMIN);

                    return usuarioRepository.save(usuario);
                }).orElseThrow(() -> new UsuarioNotFoundException("No se ha encontrado el usuario: %s".formatted(username)));
    }

    public Usuario removeRoleAdmin(String username) {

        return usuarioRepository.findFirstByUsername(username)
                .map(usuario -> {

                    if (usuario.getRoles().stream().anyMatch(userRole -> userRole.equals(UserRole.ADMIN))
                    && usuario.getRoles().size() == 1)
                        throw new UsuarioSinRolException("No se puede dejar un usuario sin rol");

                    usuario.getRoles().remove(UserRole.ADMIN);

                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new UsuarioNotFoundException("No se ha encontrado el usuario: %s".formatted(username)));
    }
}
