package com.salesianostriana.dam.delight_nook.security.validation.validator;

import com.salesianostriana.dam.delight_nook.security.validation.annotation.UniqueUsername;
import com.salesianostriana.dam.delight_nook.user.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UsuarioRepository repo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.hasText(value) && !repo.existsByUsername(value);
    }
}
