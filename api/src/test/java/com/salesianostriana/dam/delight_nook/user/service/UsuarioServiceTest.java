package com.salesianostriana.dam.delight_nook.user.service;

import com.salesianostriana.dam.delight_nook.user.error.UsuarioNotFoundException;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import com.salesianostriana.dam.delight_nook.util.SendGridMailService;
import com.salesianostriana.dam.delight_nook.util.files.service.StorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private AutoCloseable closeable;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StorageService storageService;

    @Mock
    private SendGridMailService sendGridMailService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .username("manolitox1998")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception{
        closeable.close();
    }

    @Test
    void testDisable() {
        when(usuarioRepository.findFirstByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.disable("manolitox1998");

        assertAll(
                () -> assertNotNull(result),
                () -> verify(usuarioRepository, times(1)).findFirstByUsername(anyString()),
                () -> verify(usuarioRepository, times(1)).save(any(Usuario.class))
        );
    }

    @Test
    void testDisableUsuarioNotFound() {
        when(usuarioRepository.findFirstByUsername(anyString())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrowsExactly(UsuarioNotFoundException.class, () -> usuarioService.disable("manolitox1998")),
                () -> verify(usuarioRepository, times(1)).findFirstByUsername(anyString()),
                () -> verify(usuarioRepository, times(0)).save(any(Usuario.class))
        );
    }
}