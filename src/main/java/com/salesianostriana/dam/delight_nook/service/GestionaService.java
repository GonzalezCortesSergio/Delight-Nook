package com.salesianostriana.dam.delight_nook.service;

import com.salesianostriana.dam.delight_nook.error.CajaNotFoundException;
import com.salesianostriana.dam.delight_nook.error.CajaOcupadaException;
import com.salesianostriana.dam.delight_nook.model.Gestiona;
import com.salesianostriana.dam.delight_nook.repository.CajaRepository;
import com.salesianostriana.dam.delight_nook.repository.GestionaRepository;
import com.salesianostriana.dam.delight_nook.user.model.Cajero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GestionaService {

    private final GestionaRepository gestionaRepository;
    private final CajaRepository cajaRepository;


    @Transactional
    public Gestiona create(Cajero cajero, Long idCaja) {

        if(!cajaRepository.findByCajeroSesion(cajero.getUsername()).isEmpty())
            throw new CajaOcupadaException("Ya estás ocupando una caja, cierra sesión");

        Optional<Gestiona> optionalGestiona = gestionaRepository.findByCajeroUsernameAndCajaId(cajero.getUsername(), idCaja);

        if(optionalGestiona.isPresent()) {

            Gestiona antigua = optionalGestiona.get();

            if(antigua.getFechaDejaGestionar() == null)
                throw new CajaOcupadaException("La caja se encuentra ocupada");

            antigua.setFechaGestiona(LocalDateTime.now());
            antigua.setFechaDejaGestionar(null);

            return gestionaRepository.save(antigua);
        }

        Gestiona nueva = new Gestiona();

        nueva.setCaja(
                cajaRepository.findById(idCaja)
                        .orElseThrow(() -> new CajaNotFoundException("No se ha encontrado la caja con ID: %d".formatted(idCaja)))
        );
        nueva.setCajero(cajero);
        nueva.setFechaGestiona(LocalDateTime.now());

        return gestionaRepository.save(nueva);

    }
}
