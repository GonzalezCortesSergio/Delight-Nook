package com.salesianostriana.dam.delight_nook.user.service;

import com.salesianostriana.dam.delight_nook.error.BadRequestException;
import com.salesianostriana.dam.delight_nook.user.dto.CreateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.dto.UsuarioResponseDto;
import com.salesianostriana.dam.delight_nook.user.dto.ValidateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.error.ActivationExpiredException;
import com.salesianostriana.dam.delight_nook.user.error.BorradoPropioException;
import com.salesianostriana.dam.delight_nook.user.error.UsuarioNotFoundException;
import com.salesianostriana.dam.delight_nook.user.error.UsuarioSinRolException;
import com.salesianostriana.dam.delight_nook.user.model.Almacenero;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private final JavaMailSender mailSender;

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

        Usuario usuario;

        if(userRole.equalsIgnoreCase("almacenero")) {
            usuario = createAlmacenero(usuarioDto);
        }


        else if(userRole.equalsIgnoreCase("cajero"))
            usuario = createCajero(usuarioDto);

        else
            usuario = createUsuario(usuarioDto);

        try {
            sendSimpleMessage(usuario.getEmail(), "Autenticación", "Su código de autenticación es: %s".formatted(usuario.getActivationToken()));
        }catch (MessagingException ex) {
            throw new BadRequestException("Illo lascagao");
        }
        return usuario;

    }

    private void sendSimpleMessage(String to, String subject, String text) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setFrom("noreply@delight-nook.com");
        helper.setSubject(subject);
        helper.setText(text);

        mailSender.send(message);
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

    private void sendMail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@delight-nook.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
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

    public void deleteByUsername(Usuario usuario, String username) {
        if(usuario.getUsername().equals(username))
            throw new BorradoPropioException("Un usuario no se puede borrar a si mismo");

        usuarioRepository.deleteByUsername(username);
    }
}
