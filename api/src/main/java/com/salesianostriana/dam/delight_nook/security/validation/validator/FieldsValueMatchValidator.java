package com.salesianostriana.dam.delight_nook.security.validation.validator;

import com.salesianostriana.dam.delight_nook.security.validation.annotation.FieldsValueMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.PropertyAccessorFactory;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        Object fieldValue = PropertyAccessorFactory
                .forBeanPropertyAccess(object).getPropertyValue(field);
        Object fieldValueMatch = PropertyAccessorFactory
                .forBeanPropertyAccess(object).getPropertyValue(fieldMatch);

        if(fieldValue != null)
            return fieldValue.equals(fieldValueMatch);

        else
            return fieldValueMatch == null;
    }
}
