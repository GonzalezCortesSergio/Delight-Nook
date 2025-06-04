package com.salesianostriana.dam.delight_nook.security.validation.annotation;

import com.salesianostriana.dam.delight_nook.security.validation.validator.MoneyNotHigherValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = MoneyNotHigherValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MoneyNotHigher {

    String message() default "Intentas sacar más dinero del que hay";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
