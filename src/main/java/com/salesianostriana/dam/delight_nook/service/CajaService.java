package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.dto.CreateCajaDto;
import com.salesianostriana.dam.delight_nook.dto.EditCajaDto;
import com.salesianostriana.dam.delight_nook.dto.GetCajaDto;
import com.salesianostriana.dam.delight_nook.error.CajaNotFoundException;
import com.salesianostriana.dam.delight_nook.error.MoneyHigherException;
import com.salesianostriana.dam.delight_nook.model.Caja;
import com.salesianostriana.dam.delight_nook.repository.CajaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<GetCajaDto> findAll(Pageable pageable) {

        Page<Caja> result = cajaRepository.findAll(pageable);

        if(result.isEmpty())
            throw new CajaNotFoundException("No se han encontrado cajas");

        return result.map(GetCajaDto::of);
    }

    public Caja editDineroCaja(EditCajaDto editCajaDto) {

        return cajaRepository.findById(editCajaDto.id())
                .map(caja -> {
                    double dineroCaja = caja.getDineroCaja();
                    caja.setDineroCaja(dineroCaja + editCajaDto.dineroNuevo());

                    return cajaRepository.save(caja);
                })
                .orElseThrow(() -> new CajaNotFoundException("No se ha encontrado la caja con ID: %d".formatted(editCajaDto.id())));
    }

    public void deleteById(Long id) {
        cajaRepository.deleteById(id);
    }
}
