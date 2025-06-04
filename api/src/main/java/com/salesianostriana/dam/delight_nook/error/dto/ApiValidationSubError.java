package com.salesianostriana.dam.delight_nook.error.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Optional;

public record ApiValidationSubError(
        String object,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String field,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object rejectedValue
) {
    public static ApiValidationSubError from(ObjectError error) {

        if (error instanceof FieldError fieldError) {
            return new ApiValidationSubError(
                    fieldError.getObjectName(),
                    fieldError.getDefaultMessage(),
                    fieldError.getField(),
                    fieldError.getRejectedValue()
            );
        } else {
            return new ApiValidationSubError(
                    error.getObjectName(),
                    error.getDefaultMessage(),
                    null,
                    null
            );

        }

    }

    public static ApiValidationSubError from(ConstraintViolation v) {

        return new ApiValidationSubError(
                v.getRootBean().getClass().getSimpleName(),
                v.getMessage(),
                Optional.ofNullable(v.getPropertyPath())
                        .map(PathImpl.class::cast)
                        .map(PathImpl::getLeafNode)
                        .map(NodeImpl::asString)
                        .orElse("unknown"),
                v.getInvalidValue()
        );
    }


}
