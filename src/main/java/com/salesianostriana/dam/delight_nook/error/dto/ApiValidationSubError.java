package com.salesianostriana.dam.delight_nook.error.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public record ApiValidationSubError(
        String object,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String field,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object rejectedValue
) {
    public static ApiValidationSubError fromError(ObjectError error) {

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
}
