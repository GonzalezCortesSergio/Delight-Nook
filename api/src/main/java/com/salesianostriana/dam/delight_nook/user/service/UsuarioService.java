package com.salesianostriana.dam.delight_nook.user.service;

import com.salesianostriana.dam.delight_nook.error.BadRequestException;
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
import com.salesianostriana.dam.delight_nook.util.SendGridMailService;
import com.salesianostriana.dam.delight_nook.util.files.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final SendGridMailService mailService;

    private static final String NOT_FOUND_MESSAGE = "No se ha encontrado el usuario: %s";

    @Value("${jwt.verification.duration}")
    private int activationDuration;

    private Usuario createUsuario(CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(Usuario.builder()
                .username(usuarioDto.username())
                .email(usuarioDto.email())
                .nombreCompleto(usuarioDto.nombreCompleto())
                .roles(Set.of(UserRole.ADMIN))
                .activationToken(generateRandomActivationCode())
                .build());
    }

    public Usuario saveUsuario(CreateUsuarioDto usuarioDto, String userRole) {

        Usuario usuario;
        if(userRole.equalsIgnoreCase("almacenero")) {
            usuario = createAlmacenero(usuarioDto);
        }


        else if(userRole.equalsIgnoreCase("cajero"))
            usuario = createCajero(usuarioDto);

        else
            usuario = createUsuario(usuarioDto);

        try {
            mailService.sendMail(usuario.getEmail(), "Activación de cuenta", generateHtmlMessage(usuario.getActivationToken()));
        }catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el email de activación");
        }

        return usuario;

    }

    private Almacenero createAlmacenero(CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(
                Almacenero.builder()
                        .username(usuarioDto.username())
                        .email(usuarioDto.email())
                        .nombreCompleto(usuarioDto.nombreCompleto())
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

        return result.map(usuario -> UsuarioResponseDto.of(usuario, getImageUrl(usuario.getAvatar())));
    }

    public Usuario addRoleAdmin(String username) {

        return usuarioRepository.findFirstByUsername(username)
                .map(usuario -> {
                    usuario.getRoles().add(UserRole.ADMIN);

                    return usuarioRepository.save(usuario);
                }).orElseThrow(() -> new UsuarioNotFoundException(NOT_FOUND_MESSAGE.formatted(username)));
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
                .orElseThrow(() -> new UsuarioNotFoundException(NOT_FOUND_MESSAGE.formatted(username)));
    }

    public void deleteByUsername(String username) {
        usuarioRepository.deleteByUsername(username);
    }

    public Usuario disable(String username) {
        return usuarioRepository.findFirstByUsername(username)
                .map(usuario -> {
                    usuario.setEnabled(false);
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new UsuarioNotFoundException(NOT_FOUND_MESSAGE.formatted(username)));
    }

    public Usuario enable(String username) {
        return usuarioRepository.findFirstByUsername(username)
                .map(usuario -> {
                    if(!StringUtils.hasText(usuario.getPassword()))
                        throw new BadRequestException("El usuario no ha sido verificado todavía");

                    usuario.setEnabled(true);
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new UsuarioNotFoundException(NOT_FOUND_MESSAGE.formatted(username)));
    }

    private String generateHtmlMessage(String activationToken) {

        StringBuilder contentBuilder = new StringBuilder();

        contentBuilder.append("<!DOCTYPE html>");
        contentBuilder.append("<html lang=\"es\">");
        contentBuilder.append("<head>");
        contentBuilder.append("<meta charset=\"UTF-8\">");
        contentBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        contentBuilder.append("<title>Bienvenido</title>");
        contentBuilder.append("</head>");
        contentBuilder.append("<body>");
        contentBuilder.append("<h1 style=\"text-align: center; color: white; background-color: cadetblue; padding-top: 4%; padding-bottom: 4%;\">¡¡¡Bienvenido a Delight-Nook!!!</h1>");
        contentBuilder.append("<div style=\"margin-top: 5%;\">");
        contentBuilder.append("<h2 style=\"text-align: center;\">Para verificar su cuenta</h2>");
        contentBuilder.append("</div>");
        contentBuilder.append("<div style=\"text-align: center;\">");
        contentBuilder.append("<h3 style=\"text-align: center;\">Deberá proporcionar su contraseña, verificarla y este código: %s</h3>".formatted(activationToken));
        contentBuilder.append("</div>");
        contentBuilder.append("<div style=\"text-align: center;\">");
        contentBuilder.append("<a href=\"http://localhost/usuario/validar\"><h2>En el siguiente enlace</h2</a>");
        contentBuilder.append("</div>");
        contentBuilder.append("</body>");
        contentBuilder.append("</html>");

        return contentBuilder.toString();
    }

    public String getImageUrl(String filename) {

        if(filename == null || filename.isEmpty())
            return null;

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/usuario/download/")
                .path(filename)
                .toUriString();
    }
}
