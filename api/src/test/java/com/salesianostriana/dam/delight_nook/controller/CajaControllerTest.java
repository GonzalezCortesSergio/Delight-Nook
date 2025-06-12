package com.salesianostriana.dam.delight_nook.controller;

import com.salesianostriana.dam.delight_nook.error.GlobalErrorController;
import com.salesianostriana.dam.delight_nook.model.Caja;
import com.salesianostriana.dam.delight_nook.security.exceptionhandling.JwtControllerAdvice;
import com.salesianostriana.dam.delight_nook.security.jwt.access.JwtService;
import com.salesianostriana.dam.delight_nook.security.jwt.refresh.RefreshTokenService;
import com.salesianostriana.dam.delight_nook.service.CajaService;
import com.salesianostriana.dam.delight_nook.service.GestionaService;
import com.salesianostriana.dam.delight_nook.user.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CajaControllerTest {

    private MockMvc mvc;

    private static final String BASE_URL = "/api/caja";

    @Mock
    private CajaService cajaService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private GestionaService gestionaService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private CajaController cajaController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(cajaController)
                .setControllerAdvice(new GlobalErrorController())
                .build();
    }

    @Test
    void testFindByIdOk() throws Exception{
        when(cajaService.findById(anyLong())).thenReturn(new Caja());

        mvc.perform(get(BASE_URL + "/admin/detalles/%d".formatted(1L)))
                .andExpect(status().isOk());

        verify(cajaService, times(1)).findById(anyLong());
    }
}