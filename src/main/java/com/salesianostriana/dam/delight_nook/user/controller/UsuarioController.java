package com.salesianostriana.dam.delight_nook.user.controller;

import com.salesianostriana.dam.delight_nook.security.jwt.access.JwtService;
import com.salesianostriana.dam.delight_nook.security.jwt.refresh.RefreshToken;
import com.salesianostriana.dam.delight_nook.security.jwt.refresh.RefreshTokenService;
import com.salesianostriana.dam.delight_nook.user.dto.CreateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.dto.LoginRequest;
import com.salesianostriana.dam.delight_nook.user.dto.UsuarioResponseDto;
import com.salesianostriana.dam.delight_nook.user.dto.ValidateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/auth/register")
    public ResponseEntity<UsuarioResponseDto> register(@RequestBody @Validated CreateUsuarioDto usuarioDto) {

        Usuario usuario = usuarioService.createUsuario(usuarioDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponseDto.of(usuario));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UsuarioResponseDto> login(@RequestBody LoginRequest loginRequest) {

        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Usuario usuario = (Usuario) auth.getPrincipal();

        String accessToken = jwtService.generateAccessToken(usuario);
        RefreshToken refreshToken = refreshTokenService.create(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponseDto.of(usuario, accessToken, refreshToken.getToken()));
    }

    @PostMapping("/auth/validate")
    public UsuarioResponseDto validateAccount(@RequestBody @Validated ValidateUsuarioDto validateUsuarioDto) {

        return UsuarioResponseDto.of(usuarioService.acitvateAccount(validateUsuarioDto));
    }
}
