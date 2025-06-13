package com.salesianostriana.dam.delight_nook.security.validation.validator;

import com.salesianostriana.dam.delight_nook.query.ProductoFilterDTO;
import com.salesianostriana.dam.delight_nook.security.validation.annotation.MinNotHigherThanMax;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class MinNotHigherThanMaxValidator implements ConstraintValidator<MinNotHigherThanMax, ProductoFilterDTO> {
    @Override
    public boolean isValid(ProductoFilterDTO value, ConstraintValidatorContext context) {

        if(precioMaxValid(value.getPrecioMax()) && precioMinValid(value.getPrecioMin())) {
            return value.getPrecioMax() > value.getPrecioMin();
        }
        return true;
    }


    private boolean precioMaxValid(Double precioMax) {
        return precioMax != null && precioMax != 0;
    }

    private boolean precioMinValid(Double precioMin) {
        return precioMin != null && precioMin != 0;
    }
}
