package com.salesianostriana.dam.delight_nook.util;

import com.salesianostriana.dam.delight_nook.dto.producto.ProductoCantidadDto;
import com.salesianostriana.dam.delight_nook.repository.ProductoRepository;
import com.salesianostriana.dam.delight_nook.repository.StockRepository;
import com.salesianostriana.dam.delight_nook.service.StockService;
import com.salesianostriana.dam.delight_nook.user.model.Almacenero;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import com.salesianostriana.dam.delight_nook.user.model.UserRole;
import com.salesianostriana.dam.delight_nook.user.model.Usuario;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeed {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final StockRepository stockRepository;
    private final ProductoRepository productoRepository;
    private final StockService stockService;

    @PostConstruct
    public void init() {

        Usuario usuario = Usuario.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .nombreCompleto("admin")
                .avatar("admin.png")
                .email("admin@admin.com")
                .roles(Set.of(UserRole.ADMIN))
                .enabled(true)
                .build();

        Cajero cajero1 = Cajero.builder()
                .username("manolitox1998")
                .password(passwordEncoder.encode("1234"))
                .nombreCompleto("Manolo López Guzmán")
                .avatar("")
                .email("manolitox1998@gmail.com")
                .roles(Set.of(UserRole.CAJERO))
                .enabled(true)
                .build();

        Cajero cajero2 = Cajero.builder()
                .username("cajero_admin")
                .password(passwordEncoder.encode("1234"))
                .nombreCompleto("Cajero Admin")
                .avatar("")
                .email("cajero.admin@gmail.com")
                .roles(Set.of(UserRole.CAJERO, UserRole.ADMIN))
                .enabled(true)
                .build();

        Almacenero almacenero1 = Almacenero.builder()
                .username("pedritxauq123")
                .password(passwordEncoder.encode("1234"))
                .nombreCompleto("Pedro López Guzmán")
                .avatar("")
                .email("preql1s23@gmail.com")
                .roles(Set.of(UserRole.ALMACENERO, UserRole.ADMIN))
                .enabled(true)
                .build();

        usuarioRepository.saveAll(List.of(usuario, almacenero1, cajero1, cajero2));

        productoRepository.findAll().forEach(producto -> {
            stockService.create(almacenero1, new ProductoCantidadDto(producto.getId(), 150));
        });
    }
}
