package com.salesianostriana.dam.delight_nook.security.jwt.refresh;

import com.salesianostriana.dam.delight_nook.security.jwt.access.JwtService;
import com.salesianostriana.dam.delight_nook.user.dto.UsuarioResponseDto;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import com.salesianostriana.dam.delight_nook.user.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Value("${jwt.refresh.duration}")
    private int durationInMinutes;


    public RefreshToken create(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .usuario(usuario)
                        .expireAt(Instant.now().plusSeconds(durationInMinutes * 60))
                        .build()
        );
    }

    public RefreshToken verify(RefreshToken refreshToken) {

        if(refreshToken.getExpireAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Token de refresco caducado. Por favor, vuelva a loguearse");
        }

        return refreshToken;
    }


    public UsuarioResponseDto refreshToken(String token) {

        return refreshTokenRepository.findById(UUID.fromString(token))
                .map(this::verify)
                .map(RefreshToken::getUsuario)
                .map(usuario -> {
                    String accessToken = jwtService.generateAccessToken(usuario);
                    RefreshToken refreshToken = this.create(usuario);
                    return UsuarioResponseDto.of(usuario, accessToken, refreshToken.getToken(), usuarioService.getImageUrl(usuario.getAvatar()));
                })
                .orElseThrow(() -> new RefreshTokenException("No se ha podido refrescar el token. Por favor, vuelva a loguearse"));
    }

    public void cerrarSesion(Usuario usuario) {

        refreshTokenRepository.deleteByUsuario(usuario);
    }
}
