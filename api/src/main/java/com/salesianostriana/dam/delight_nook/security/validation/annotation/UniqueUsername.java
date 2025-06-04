package com.salesianostriana.dam.delight_nook.security.validation.annotation;

import com.salesianostriana.dam.delight_nook.security.validation.validator.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {

    String message() default "El nombre de usuario ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
