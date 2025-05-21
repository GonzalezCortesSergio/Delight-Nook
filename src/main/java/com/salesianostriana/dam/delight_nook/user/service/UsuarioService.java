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
import com.salesianostriana.dam.delight_nook.util.SendGridMailService;
import com.salesianostriana.dam.delight_nook.util.files.model.FileMetadata;
import com.salesianostriana.dam.delight_nook.util.files.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    @Value("${jwt.verification.duration}")
    private int activationDuration;

    private Usuario createUsuario(FileMetadata fileMetadata, CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(Usuario.builder()
                .username(usuarioDto.username())
                .email(usuarioDto.email())
                .nombreCompleto(usuarioDto.nombreCompleto())
                .roles(Set.of(UserRole.ADMIN))
                .activationToken(generateRandomActivationCode())
                .avatar(fileMetadata.getId())
                .build());
    }

    public Usuario saveUsuario(MultipartFile file, CreateUsuarioDto usuarioDto, String userRole) {

        Usuario usuario;

        FileMetadata fileMetadata = storageService.store(file);

        if(userRole.equalsIgnoreCase("almacenero")) {
            usuario = createAlmacenero(fileMetadata, usuarioDto);
        }


        else if(userRole.equalsIgnoreCase("cajero"))
            usuario = createCajero(fileMetadata, usuarioDto);

        else
            usuario = createUsuario(fileMetadata, usuarioDto);

        try {
            mailService.sendMail(usuario.getEmail(), "Activación de cuenta", generateHtmlMessage(usuario.getActivationToken()));
        }catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el email de activación");
        }

        return usuario;

    }

    private Almacenero createAlmacenero(FileMetadata fileMetadata, CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(
                Almacenero.builder()
                        .username(usuarioDto.username())
                        .email(usuarioDto.email())
                        .nombreCompleto(usuarioDto.nombreCompleto())
                        .roles(Set.of(UserRole.ALMACENERO))
                        .activationToken(generateRandomActivationCode())
                        .avatar(fileMetadata.getId())
                        .build()
        );
    }

    private Cajero createCajero(FileMetadata fileMetadata, CreateUsuarioDto usuarioDto) {

        return usuarioRepository.save(
                Cajero.builder()
                        .username(usuarioDto.username())
                        .email(usuarioDto.email())
                        .nombreCompleto(usuarioDto.nombreCompleto())
                        .roles(Set.of(UserRole.CAJERO))
                        .activationToken(generateRandomActivationCode())
                        .avatar(fileMetadata.getId())
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

    public void deleteByUsername(String username) {
        usuarioRepository.deleteByUsername(username);
    }

    private String generateHtmlMessage(String activationToken) {

        StringBuilder contentBuilder = new StringBuilder();

        contentBuilder.append("<!DOCTYPE html>");
        contentBuilder.append("<html lang=\"es\">");
        contentBuilder.append("<head>");
        contentBuilder.append("<meta charset=\"UTF-8\">");
        contentBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        contentBuilder.append("<title>Bienvenido</title>");
        contentBuilder.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">");
        contentBuilder.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>");
        contentBuilder.append("</head>");
        contentBuilder.append("<body class=\"bg-dark\">");
        contentBuilder.append("<h1 class=\"text-center bg-info py-4\">¡¡¡Bienvenido a Delight-Nook!!!</h1>");
        contentBuilder.append("<div class=\"mt-5\">");
        contentBuilder.append("<h4 class=\"text-light text-center\">Para verificar su cuenta</h4>");
        contentBuilder.append("</div>");
        contentBuilder.append("<div class=\"text-center\">");
        contentBuilder.append("<h4 class=\"text-light text-center\">Deberá proporcionar su contraseña, verificarla y este código: %s</h4>".formatted(activationToken));
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
