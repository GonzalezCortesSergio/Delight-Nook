package com.salesianostriana.dam.delight_nook.security.validation.validator;

import com.salesianostriana.dam.delight_nook.dto.caja.EditCajaDto;
import com.salesianostriana.dam.delight_nook.repository.CajaRepository;
import com.salesianostriana.dam.delight_nook.security.validation.annotation.MoneyNotHigher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class MoneyNotHigherValidator implements ConstraintValidator<MoneyNotHigher, EditCajaDto> {

    @Autowired
    private CajaRepository cajaRepository;

    @Override
    public boolean isValid(EditCajaDto value, ConstraintValidatorContext context) {
        Optional<Double> dineroCaja = cajaRepository.findDineroCaja(value.id());

        if(dineroCaja.isEmpty()) {
            context.buildConstraintViolationWithTemplate("La caja con ID: %d no existe".formatted(value.id()))
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }

        return - (dineroCaja.get()) < value.dineroNuevo();

    }
}
