package com.salesianostriana.dam.delight_nook.security.validation.annotation;

import com.salesianostriana.dam.delight_nook.security.validation.validator.MinNotHigherThanMaxValidator;
import com.salesianostriana.dam.delight_nook.security.validation.validator.MoneyNotHigherValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = MinNotHigherThanMaxValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MinNotHigherThanMax {

    String message() default "El precio máximo no puede ser superior al mínimo";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
