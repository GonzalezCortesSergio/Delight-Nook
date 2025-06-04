package com.salesianostriana.dam.delight_nook.security.validation.annotation;

import com.salesianostriana.dam.delight_nook.security.validation.validator.UniqueCategoryNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueCategoryNameValidator.class)
public @interface UniqueCategoryName {

    String message() default "La categoría con ese nombre ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
