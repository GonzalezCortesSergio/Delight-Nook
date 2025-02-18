package com.salesianostriana.dam.delight_nook.security.validation;

import com.salesianostriana.dam.delight_nook.user.dto.CreateUsuarioDto;
import com.salesianostriana.dam.delight_nook.user.dto.ValidateUsuarioDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ValidateUsuarioDto> {

    @Override
    public boolean isValid(ValidateUsuarioDto usuarioDto, ConstraintValidatorContext context) {
        return usuarioDto.password() != null && usuarioDto.password().equals(usuarioDto.verifyPassword());
    }
}
