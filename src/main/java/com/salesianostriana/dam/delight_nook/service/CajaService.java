package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.CreateCajaDto;
import com.salesianostriana.dam.delight_nook.model.Caja;
import com.salesianostriana.dam.delight_nook.repository.CajaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CajaService {

    private final CajaRepository cajaRepository;

    public Caja create(CreateCajaDto cajaDto) {

        return cajaRepository.save(Caja.builder()
                .nombre(cajaDto.nombre())
                .dineroCaja(cajaDto.dineroCaja())
                .build());
    }
}
